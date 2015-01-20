/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft 2011-2-1
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.resource.redis;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import com.feinno.appengine.AppContext;
import com.feinno.appengine.resource.ResourceProxy;
import com.feinno.serialization.Serializer;

/**
 * 提供基本的内存缓存服务，来存取用户的数据。
 * 
 * 该缓存服务是基于AppContext来存取的，即一个AppContext相关的所有数据会以某种关联的方式缓存起来。这样
 * 客户端可以以一个命令来清除一个AppContext相关的所有数据，这个针对需要清除某个用户的缓存时比较有用。
 * 
 * 目前的实现，有以下几个特点：
 * 1.对一个AppContext，所有的数据都是以对象来存取的，其key简单的基于对象的类名，这样可以极大的
 * 简化客户端的工作（即不需要指定key值），而且也可以存取多个key/value值。
 * 2.对特定AppContext中某个对象类型的值，因为只会有一个key，因此只能存一个值，后续的值会覆盖以前的值。
 * 3.非集合类型的对象，直接存放在AppContext对应的hash表中，而且是以序列化的方式进行存取的，因此我们只能将对象作为一个整体进行访问，而不可能只存取对象中的一个属性值，
 * 4.集合类型的对象，则有多种方式存储；每种方式都有不同的key。
 *  A.可以将集合类型对象作为一个不可分割的对象，直接存放在AppContext对应的hash表中。
 *  B.将集合类型的对象，以List的方式存放在AppContext对应的hash表之外，但其key值由ctx和集合的元素类型组成，存放在AppContext对应的hash表中。
 *  C.将集合类型的对象，以Hash的方式存放在AppContext对应的hash表之外，但其key值由ctx和集合的元素类型组成，存放在AppContext对应的hash表中。
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RedisProxy extends ResourceProxy
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisProxy.class);
	
	/**
	 * 如果遇到连接不可用，重试的次数。
	 */
	private static final int TRY_TIMES = 3;
	
	/**
	 * 用来缓存所有需要保存的值的键值的字节数组。
	 */
	private static final HashMap<String,byte[]> BYTES_MAP = new HashMap<String,byte[]>();
	
    /**
     * 值为map的key值的前缀。
     */
    private static final String MAP_VALUE_KEY_PREFIX = "Map:";

    /**
     * 值为List的key值的前缀。
     */
    private static final String LIST_VALUE_KEY_PREFIX = "List:";

    /**
	 * 用来保存所有需要存放在AppContext对应的hash表之外的值的key值，key值之间用‘;’分隔。
	 */
	private static final String OUTSIDE_KEYS = "$OutsideKeys$";
	
	/**
	 * OUTSIDE_KEYS的字节数组
	 */
	private static final byte[] OUTSIDE_KEYS_BYTES = getBytes(OUTSIDE_KEYS);

    /**
     * '-'的字节数组
     */
    private static final byte[] MINUS_SEPARATOR_BYTES = getBytes("-");

    /**
     * 'true'的字节数组
     */
    private static final byte[] TRUE_BYTES = getBytes("true");

    /**
     * Lock key的字节数组
     */
    private static final byte[] LOCK_KEY_BYTES = getBytes("$lock$");

    /**
     * 表示null值的字符串。
     */
    private static final String NULL = "nil";
    
    /**
     * NULL的字节数组
     */
    private static final byte[] NULL_BYTES = getBytes(NULL);
    
    /**
     * 临时外部键后缀的字节数组长度
     */
    private static final int TMP_OUTSIDE_KEY_POSTFIX_LENGTH = getBytes("00").length;


    public RedisProxy()
	{
		super();
		LOGGER.info("create RedisProxy");
	}
	
	/**
	 * 清除与ctx相关的所有缓存的数据。
	 * @param ctx 
	 * @throws IOException
	 */
	public void clearAll(AppContext ctx) throws IOException,RedisUnvailableException {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;
        Set<byte[]> outsideKeySet = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        //获取所有的外部键
        byte[] outsideKeySetKey = getOutsideKeySetKey(ctxKey);

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                outsideKeySet = c.smembers(outsideKeySetKey);
                //清除外部键对应的数据。
                for(byte[] outsideKey : outsideKeySet) {
                    c.del(outsideKey);
                }
                //清除外部键set
                c.del(outsideKeySetKey);
                //清除AppContext对应的hash表
                c.del(ctxKey);
                return;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
	}

	/**
	 * 将对象e保存到redis服务器中。
	 * @param <E>
	 * @param ctx
	 * @param e
	 * @throws IOException
	 */
	public <E> void save(AppContext ctx, E e) throws IOException,RedisUnvailableException
	{
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;
        
        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] field = RedisProxy.<E>getFieldKey(e);
        byte[] value = Serializer.encode(e); 
        
        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                c.hset(ctxKey , field , value);
                return;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
	}
	
	/**
	 * 
	 * @param <E>
	 * @param ctx
	 * @param clazz
	 * @return 返回之前保存到redis服务器中的类型为clazz的对象。
	 * @throws IOException
	 */
	public <E>  E get(AppContext ctx, Class<E> clazz) throws IOException,RedisUnvailableException
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        
        Jedis c = null;
        byte[] value = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] field = RedisProxy.<E>getFieldKey(clazz);

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                value = normalize(c.hget(ctxKey , field));
                if(value == null) {
                    return null;
                } else {
                    return Serializer.<E>decode(clazz , value);
                }
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
        //不可能到达这儿
        return null;
    }


   /**
     * 将对象e从redis服务器中删除。
     * @param <E>
     * @param ctx
     * @param clazz
     * @throws IOException
     */
    public <E> void remove(AppContext ctx, Class<E> clazz) throws IOException,RedisUnvailableException
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());

        Jedis c = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] field = RedisProxy.<E>getFieldKey(clazz);
        
        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                c.hdel(ctxKey , field);
                return;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
    }

	//////////////////////////////////////////////////////////////////////////////////
    //////////// 下面的方法用来存取一个map   ////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////
	/**
	 * 将map中的数据以key,value的方式保存到redis中。
	 * 如果对当前ctx,之前已经保存过具有相同键类型和值类型的map，则之前的map数据将会被清除。
	 * 对map的操作是原子的；即同一时刻只会有一个客户端（进程）会写这个map。
	 * @param <K> map中key的类型
	 * @param <V> map中value的类型
	 * @param ctx 
	 * @param keyClazz map中key的类型，该值用来生成保存map的键值
	 * @param valueClazz map中value的类型，该值用来生成保存map的键值
	 * @param map
	 * @throws IOException
	 */
	public <K,V> void saveMap(AppContext ctx,Class<K> keyClazz,Class<V> valueClazz,Map<K,V> map) throws IOException,RedisUnvailableException 
	{
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKeySetKey = getOutsideKeySetKey(ctxKey);
        byte[] outsideKey = RedisProxy.<K,V>getMapFieldKey(ctxKey,keyClazz,valueClazz);

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                //将外部键值放入键值set中,目的是为了做整体清除。
                c.sadd(outsideKeySetKey , outsideKey);
                //找到一个临时键用来存放map数据。
                byte[] tmpKey = Arrays.copyOf(outsideKey , outsideKey.length + TMP_OUTSIDE_KEY_POSTFIX_LENGTH);
                int keyPostfix = 10;
                while(true) {
                    System.arraycopy(getCachedBytes(String.valueOf(keyPostfix)) , 0 , tmpKey , outsideKey.length , TMP_OUTSIDE_KEY_POSTFIX_LENGTH);
                    //检查tmpKey是否被占用
                    if(c.hsetnx(tmpKey , LOCK_KEY_BYTES , TRUE_BYTES) == 1) {
                        break;
                    }
                    keyPostfix += 1;
                    if(keyPostfix == 100) {
                        //有89个客户端在同时set同一个map，休息10毫秒，然后继续。
                        keyPostfix = 10;                   
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                        }
                    }
                }
                //将map数据存放到临时键。
                HashMap<byte[],byte[]> bMap = new HashMap<byte[],byte[]>(map.size(),1);
                for(Map.Entry<K, V> entry : map.entrySet()) {
                    bMap.put( Serializer.encode(entry.getKey()) , Serializer.encode(entry.getValue()));
                }
                c.hmset(tmpKey , bMap);
                //将临时键改名成正式的外部键
                c.rename(tmpKey , outsideKey);
                //将Lock键从map中删除
                c.hdel(outsideKey , LOCK_KEY_BYTES);
               return;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
	}
	
    /**
     * 删除当前ctx中保存的指定键和值类型的map
     * 如果map在redis中不存在，则删除操作会继续，只是不会有任何影响。
     * @param <K> map中key的类型
     * @param <V> map中value的类型
     * @param ctx 
     * @param keyClazz map中key的类型，该值用来生成保存map的键值
     * @param valueClazz map中value的类型，该值用来生成保存map的键值
     * @param map
     * @throws IOException
     */
    public <K,V> void removeMap(AppContext ctx,Class<K> keyClazz,Class<V> valueClazz,Map<K,V> map) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKeySetKey = getOutsideKeySetKey(ctxKey);
        byte[] outsideKey = RedisProxy.<K,V>getMapFieldKey(ctxKey,keyClazz,valueClazz);

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                //将外部键值从set中清除
                c.srem(outsideKeySetKey , outsideKey);
                //将由外部键保存的map数据删除。
                c.del(outsideKey);
                
                return;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
    }

    /**
     * 将保存在 redis中的map数据，取出来并放入参数map中。
     * 如果map中已有数据，则该方法不会清除已有数据，只是将新数据追加进去
     * 如果map在redis中不存在，则不会改变参数map的值
     * @param <K> map中key的类型
     * @param <V> map中value的类型
     * @param ctx 
     * @param keyClazz map中key的类型，该值用来生成保存map的键值
     * @param valueClazz map中value的类型，该值用来生成保存map的键值
     * @param map
     * @return 返回true,如果redis中保存有map数据；否则，返回false。
     * @throws IOException
     */
    public <K,V> boolean getMap(AppContext ctx,Class<K> keyClazz,Class<V> valueClazz,Map<K,V> map) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;
        Map<byte[],byte[]> bMap = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKey = RedisProxy.<K,V>getMapFieldKey(ctxKey,keyClazz,valueClazz);

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                
                if(!c.exists(outsideKey).booleanValue()) {
                    //没有保存数据
                    return false;
                }
                //将map数据从redis中取出，并放入map中。
                bMap = c.hgetAll(outsideKey);
                for(Map.Entry<byte[], byte[]> entry : bMap.entrySet()) {
                    map.put( Serializer.decode(keyClazz,entry.getKey()) , Serializer.decode(valueClazz,entry.getValue()));
                }
                return true;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
        //不可能达到这儿。
        return false;
    }

    /**
     * 将map中的数据以key,value的方式追加到redis中。
     * 如果对当前ctx,之前已经保存过具有相同键类型和值类型的map，则之前的map数据将会被保留或被新数据覆盖。
     * 如果对当前ctx,之前没有保存过具有相同键类型和值类型的map，则一个新的map将会生成。
     * 对map的操作是原子的；即同一时刻只会有一个客户端（进程）会写这个map。
     * @param <K> map中key的类型
     * @param <V> map中value的类型
     * @param ctx 
     * @param keyClazz map中key的类型，该值用来生成保存map的键值
     * @param valueClazz map中value的类型，该值用来生成保存map的键值
     * @param map
     * @throws IOException
     */
    public <K,V> void putMapValues(AppContext ctx,Class<K> keyClazz,Class<V> valueClazz,Map<K,V> map) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKeySetKey = getOutsideKeySetKey(ctxKey);
        byte[] outsideKey = RedisProxy.<K,V>getMapFieldKey(ctxKey,keyClazz,valueClazz);

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                //将外部键值放入键值set中,目的是为了做整体清除；如果map之前已经保存过，则该外部键虽然会重复增加一次，但在redis中只保存一份。
                c.sadd(outsideKeySetKey , outsideKey);
                //将map数据追加到map中。
                HashMap<byte[],byte[]> bMap = new HashMap<byte[],byte[]>(map.size(),1);
                for(Map.Entry<K, V> entry : map.entrySet()) {
                    bMap.put( Serializer.encode(entry.getKey()) , Serializer.encode(entry.getValue()));
                }
                c.hmset(outsideKey , bMap);
                
                return;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
    }

    /**
     * 从redis中获取map中多个key的值。
     * 如果map不存在，则返回一个空的map。
     * 如果某些map key不存在，则直接忽略。
     * @param <K> map中key的类型
     * @param <V> map中value的类型
     * @param ctx 
     * @param keyClazz map中key的类型，该值用来生成保存map的键值
     * @param valueClazz map中value的类型，该值用来生成保存map的键值
     * @param keys
     * @return 以map的方式返回中多个key对应的值。
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public <K,V> Map<K,V> getMapValues(AppContext ctx,Class<K> keyClazz,Class<V> valueClazz,Collection<K> keys) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Map<K,V> valuesMap = new HashMap<K,V>();
        List<byte[]> ValueBytesList = null;
        Jedis c = null;
        int pos = 0;
        byte[] valueBytes = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKey = RedisProxy.<K,V>getMapFieldKey(ctxKey,keyClazz,valueClazz);
        //从 redis中一次获取所有key值的value。
        K[] keyArray =  (K[])Array.newInstance(keyClazz , keys.size());
        byte[][] keyBytesArray = new byte[keys.size()][];

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                pos = 0;
                for(K key : keys) {
                    keyArray[pos] = key;
                    keyBytesArray[pos] = Serializer.encode(key);
                    pos += 1;
                }
                ValueBytesList = c.hmget(outsideKey , keyBytesArray);
                for(int index = 0;index < keyArray.length;index++) {
                    valueBytes = normalize(ValueBytesList.get(index));
                    if(valueBytes == null) {
                        continue;
                    }
                    valuesMap.put(keyArray[index] , Serializer.decode(valueClazz ,valueBytes));
                }
                
                return valuesMap;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
        
        //不可能达到这儿。
        
        return null;
    }

	
    /**
     * 从redis中获取map中某个key的值。
     * @param <K> map中key的类型
     * @param <V> map中value的类型
     * @param ctx 
     * @param key
     * @param valueClazz map中value的类型，该值用来生成保存map的键值
     * @return 返回map中key对应的值；如果map没有保存或key不存在，则返回null。
     * @throws IOException
     */
    public <K,V> V getMapValue(AppContext ctx,Class<K> keyClazz,Class<V> valueClazz,K key) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;
        byte[] valueBytes = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKey = RedisProxy.<K,V>getMapFieldKey(ctxKey,keyClazz,valueClazz);

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                //将外部键值放入键值set中,目的是为了做整体清除；如果map之前已经保存过，则该外部键虽然会重复增加一次，但在redis中只保存一份。
                valueBytes = normalize(c.hget(outsideKey , Serializer.encode(key)));
                if(valueBytes == null) {
                    return null;
                } else {
                    return Serializer.decode(valueClazz , valueBytes);
                }
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
        
        //不可能到达这儿
        return null;
    }

    /**
     * 从redis中获取map中某个key的值。
     * 如果map之前不存在，则会在redis中创建，并将map的键值放入键值set中。
     * @param <K> map中key的类型
     * @param <V> map中value的类型
     * @param ctx 
     * @param keyClazz
     * @param valueClazz
     * @param key
     * @param value
     * @throws IOException
     */
    public <K,V> void putMapValue(AppContext ctx,Class<K> keyClazz,Class<V> valueClazz,K key,V value) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;
        
        byte[] keyBytes = Serializer.encode(key);
        byte[] valueBytes = Serializer.encode(value);
        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKey = RedisProxy.<K,V>getMapFieldKey(ctxKey,keyClazz,valueClazz);

        
        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                
                if(c.hset(outsideKey , keyBytes , valueBytes) == 1) {
                    //该条目是新创建的，则可能是key不存在，也有可能是map也不存在，如果是后者，则需要将存放map的键放入set中
                    byte[] outsideKeySetKey = getOutsideKeySetKey(ctxKey);
                    c.sadd(outsideKeySetKey,outsideKey);
                }
                return;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
    }

    /**
     * 从redis中删除map中某个key的值。
     * @param <K> map中key的类型
     * @param ctx 
     * @param keyClazz map中key的类型，该值用来生成保存map的键值
     * @param valueClazz map中value的类型，该值用来生成保存map的键值
     * @param key
     * @return 返回true,如果key被删除；否则（map没有被保存或key不存在），则返回false.
     * @throws IOException
     */
    public <K,V> boolean removeMapKey(AppContext ctx,Class<K> keyClazz,Class<V> valueClazz,K key) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKey = RedisProxy.<K,V>getMapFieldKey(ctxKey,keyClazz,valueClazz);
        byte[] keyBytes = Serializer.encode(key);

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                if(c.hdel(outsideKey , keyBytes) == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
        
        //不可能到达这儿。
        return false;
    }


    //////////////////////////////////////////////////////////////////////////////////
    //////////// 下面的方法用来存取一个list////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////
    /**
     * 将list中的数据以列表的方式保存到redis中。
     * 如果对当前ctx,之前已经保存过具有相同元素类型的list，则之前的list数据将会被清除。
     * @param <E> list中的元素类型
     * @param ctx 
     * @param elementClazz list中的元素类型，该值用来生成保存list的键值
     * @param list
     * @throws IOException
     */
    public <E> void saveList(AppContext ctx,Class<E> elementClazz,List<E> list) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;
        int keyPostfix = 0;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKeySetKey = getOutsideKeySetKey(ctxKey);
        byte[] outsideKey = RedisProxy.<E>getListFieldKey(ctxKey,elementClazz);
        byte[] tmpKey = Arrays.copyOf(outsideKey , outsideKey.length + TMP_OUTSIDE_KEY_POSTFIX_LENGTH);

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                //将外部键值放入键值set中,目的是为了做整体清除。
                c.sadd(outsideKeySetKey , outsideKey);
                //找到一个临时键用来存放list数据。
                keyPostfix = 10;
                while(true) {
                    System.arraycopy(getCachedBytes(String.valueOf(keyPostfix)) , 0 , tmpKey , outsideKey.length , TMP_OUTSIDE_KEY_POSTFIX_LENGTH);
                    //检查tmpKey是否被占用
                    if(c.hsetnx(tmpKey , LOCK_KEY_BYTES , TRUE_BYTES) == 1) {
                        break;
                    }
                    keyPostfix += 1;
                    if(keyPostfix == 100) {
                        //有89个客户端在同时set同一个map，休息10毫秒，然后继续。
                        keyPostfix = 10;                   
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                        }
                    }
                }
                //将list数据存放到临时键。
                for(E element : list) {
                    c.rpush(tmpKey , Serializer.encode(element));
                }
                //将临时键改名成正式的外部键
                c.rename(tmpKey , outsideKey);
                //将Lock键从map中删除
                c.hdel(outsideKey , LOCK_KEY_BYTES);
               
                return;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
    }

    /**
     * 从redis中将ctx中的list数据移除。
     * 如果list不存在，删除操作就会继续执行，只是不会有任何影响。
     * @param <E> list中的元素类型
     * @param ctx 
     * @param elementClazz list中的元素类型，该值用来生成保存list的键值
     * @throws IOException
     */
    public <E> void removeList(AppContext ctx,Class<E> elementClazz) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKeySetKey = getOutsideKeySetKey(ctxKey);
        byte[] outsideKey = RedisProxy.<E>getListFieldKey(ctxKey,elementClazz);

        
        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                //将外部键值从键值set移除
                c.srem(outsideKeySetKey , outsideKey);
                //将list数据从redis中移除。
                c.del(outsideKey);
               
                return;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
    }
    
    /**
     * 从redis中将list中的数据以列表的方式取回，
     * 如果参数list中已经有数据了，则新的数据会直接追加到列表后面。
     * 如果list不存在，则参数list不会被修改。
     * @param <E> list中的元素类型
     * @param ctx 
     * @param elementClazz list中的元素类型，该值用来生成保存list的键值
     * @param list 用来存放list数据
     * @return 返回true，如果list存在；否则返回false。
     * @throws IOException
     */
    public <E> boolean getList(AppContext ctx,Class<E> elementClazz,List<E> list) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKey = RedisProxy.<E>getListFieldKey(ctxKey,elementClazz);

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                if(c.exists(outsideKey)) {
                    //从redis中取回list数据
                    List<byte[]> bList = c.lrange(outsideKey , 0 , -1);
                    //将数据存放到list参数中
                    for(byte[] bytes : bList) {
                        list.add(Serializer.decode(elementClazz , bytes));
                    }
                    return true;
                } else {
                    return false;
                }
               
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
        
        //不可能达到这儿。
        return false;
    }

    /**
     * 从redis中将list中的数据以列表的方式取回，
     * 如果参数list中已经有数据了，则新的数据会直接追加到列表后面。
     * 如果index指向一个不合法的位置或list不存在，将返回null。
     * @param <E> list中的元素类型
     * @param ctx 
     * @param elementClazz list中的元素类型，该值用来生成保存list的键值
     * @param index 
     * @return 返回列表中指定位置的对象;如果不存在，则返回null。
     * @throws IOException
     */
    public <E> E getListValue(AppContext ctx,Class<E> elementClazz,int index) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKey = RedisProxy.<E>getListFieldKey(ctxKey,elementClazz);

        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                //从redis中取回list数据
                byte[] vBytes = normalize(c.lindex(outsideKey , index));
                if(vBytes == null) {
                    return null;
                } else {
                    return Serializer.decode(elementClazz , vBytes);
                }
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
        }
        
        //不可能到达这儿。
        return null;
    }
    
    /**
     * 将对象存到redis中list的指定位置上。
     * 如果index指向一个不合法的位置，或者list不存在，则会抛出意外。
     * @param <E> list中的元素类型
     * @param ctx 
     * @param elementClazz list中的元素类型，该值用来生成保存list的键值
     * @param index 
     * @param value 
     * @throws IOException
     */
    public <E> void setListValue(AppContext ctx,Class<E> elementClazz,int index,E value) throws IOException,RedisUnvailableException 
    {
        RedisResource r = (RedisResource)locateResource(ctx.getContextUri());
        Jedis c = null;

        byte[] ctxKey = ctx.getContextUri().getValue().getBytes();
        byte[] outsideKey = RedisProxy.<E>getListFieldKey(ctxKey,elementClazz);
        
        for(int runTime = 1;runTime <= TRY_TIMES;runTime++) {
            c = r.getClient();
            try {
                //设置list中的数据。
                c.lset(outsideKey , index , Serializer.encode(value));
                
                return;
            } catch(JedisConnectionException ex) {
                r.returnBrokenClient(c);
                c = null;
                if(runTime == TRY_TIMES) {
                    throw new RedisUnvailableException(ex.getMessage(),ex);
                }
            } catch(JedisException ex) {
                throw new IOException(ex.getMessage(),ex);
            } finally {
                if(c != null) {
                    r.returnClient(c);
                }
            }
            
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    //////////// 实现一些基本的工具类方法   //////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    /**
     * 
     * @param str
     * @return 返回字符串以UTF-8编码的字节数组。
     */
    private static byte[] getBytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            // impossible to go here
            return null;
        }
    }
    
    /**
     * 
     * @param src
     * @param target
     * @return 返回true，如果两个字节数组相同；否则，返回false。
     */
    private static boolean isEqual(byte[] src,byte[] target) {
        if(src.length == target.length) {
            for(int index = 0;index < src.length;index++) {
                if(src[index] == target[index]) {
                    continue;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @param bytes
     * @return 返回null，如果bytes表示一个null值；否则，直接返回bytes.
     */
    private static byte[] normalize(byte[] bytes) {
        if(bytes == null) {
            return null;
        } else if(bytes.length == 0) {
            return null;
        } else {
            if(isEqual(bytes,NULL_BYTES)) {
                return null;
            } else {
                return bytes;
            }
        }
        
    }
    
    /**
     * 
     * @param bytes
     * @return 返回true，如果由Redis返回的bytes表示null值；否则返回false。
    private static boolean isNull(byte[] bytes) {
        if(bytes == null) {
            return true;
        } else if(bytes.length == 0) {
            return true;
        } else {
            return isEqual(bytes,NULL_BYTES);
        }
    }
     */
    
    
    /**
     * 
     * @param <E>
     * @param obj
     * @return 返回一个对象保存时对应的键值的字节数组。
     */
    private static <E> byte[] getFieldKey(E obj) {
        return getFieldKey(obj.getClass());
    }
    
    /**
     * 
     * @param <E>
     * @param obj
     * @return 返回一个对象保存时对应的键值的字节数组。
     */
    private static <E> byte[] getFieldKey(Class<E> clazz) {
        String className = clazz.getName();
        return getCachedBytes(className);
    }

    /**
     * 
     * @param <E>
     * @param obj
     * @return 返回一个对象保存时对应的键值的字节数组。
     */
    private static <E> byte[] getCachedBytes(String keyStr) {
        byte[] key = BYTES_MAP.get(keyStr);
        if(key == null) {
            synchronized(BYTES_MAP) {
                key = BYTES_MAP.get(keyStr);
                if(key == null) {
                    key = getBytes(keyStr);
                    BYTES_MAP.put(keyStr , key);
                }
            }
        }
        return key;
    }

    /**
     * 
     * @param <K>
     * @param <V>
     * @param ctxKey
     * @param keyClazz
     * @param valueClazz
     * @return 返回特定context的用来存储外部键值的键值字节数组。
     */
    private static <K,V> byte[] getOutsideKeySetKey(byte[] ctxKey) {
        byte[] key = Arrays.copyOf(ctxKey , ctxKey.length + OUTSIDE_KEYS_BYTES.length + MINUS_SEPARATOR_BYTES.length);
        int keyPos = ctxKey.length;
        System.arraycopy(MINUS_SEPARATOR_BYTES , 0 , key , keyPos , MINUS_SEPARATOR_BYTES.length);
        System.arraycopy(OUTSIDE_KEYS_BYTES , 0 , key , keyPos , OUTSIDE_KEYS_BYTES.length);
        return key;
    }


    /**
     * 
     * @param <K>
     * @param <V>
     * @param ctxKey
     * @param keyClazz
     * @param valueClazz
     * @return 返回特定context下的，key和value类型固定的map的键值字节数组。
     */
    private static <K,V> byte[] getMapFieldKey(byte[] ctxKey,Class<K> keyClazz,Class<V> valueClazz) {
        String keyClassName = keyClazz.getName();
        String valueClassName = valueClazz.getName();
        String keyStr = MAP_VALUE_KEY_PREFIX + keyClassName  + "=" + valueClassName;
        byte[] key = BYTES_MAP.get(keyStr);
        if(key == null) {
            synchronized(BYTES_MAP) {
                key = BYTES_MAP.get(keyStr);
                if(key == null) {
                    key = getBytes(keyStr);
                    BYTES_MAP.put(keyStr , key);
                }
            }
        }
        return key;
    }

    /**
     * @param <K>
     * @param <V>
     * @param ctxKey
     * @param keyClazz
     * @param valueClazz
     * @return 返回特定context下的，元素类型固定的列表的键值字节数组。
     */
    private static <E> byte[] getListFieldKey(byte[] ctxKey,Class<E> elementClazz) {
        String elementClassName = elementClazz.getName();
        String keyStr = LIST_VALUE_KEY_PREFIX + elementClassName;
        byte[] key = BYTES_MAP.get(keyStr);
        if(key == null) {
            synchronized(BYTES_MAP) {
                key = BYTES_MAP.get(keyStr);
                if(key == null) {
                    key = getBytes(keyStr);
                    BYTES_MAP.put(keyStr , key);
                }
            }
        }
        return key;
    }
}

