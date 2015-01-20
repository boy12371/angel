package com.feinno.appengine.resource.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * 调用DAL时需传入的参数
 * 
 * @author lichunlei
 *
 */
public class SqlArgs extends ProtoEntity {


	//DB.index: UPDB.1
	@ProtoMember(1)
	private String dbName;
	
	@ProtoMember(2)
	private String sql;
	
	//Object[] java序列化后的byte[]数组
	@ProtoMember(3)
	private byte[] values;
	
	@ProtoMember(4)
	private String spName;
	
	@ProtoMember(5)
	private String[] params;	
	

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}	
	
	public byte[] getValues() {
		return values;
	}

	public void setValues(byte[] values) {
		this.values = values;
	}
		
	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}
	

	public static byte[] encode(Object... os) throws IOException
	{
		if(os == null)
			return null;
		ByteArrayOutputStream   baos=new   ByteArrayOutputStream(); 
        ObjectOutputStream oos = null;		
        try
        {
        	oos = new ObjectOutputStream(baos);
        	
	        oos.writeObject(os.length);	        
		    for(int i=0;i<os.length;i++)
		    	oos.writeObject(os[i]);

		    byte[] bytes = baos.toByteArray();
		    oos.flush();
		    return bytes;
        }finally
        {
        	if(oos!=null)
        		oos.close();
        	if(baos!=null)
        		baos.close();
        }
	        
	    
	}
	
	public static List<Object> decode(byte[] bytes) throws IOException, ClassNotFoundException
	{
		if(bytes == null)
			return null;
		List<Object> list = new ArrayList<Object>();
		ByteArrayInputStream   bais=new   ByteArrayInputStream(bytes); 
        ObjectInputStream ois = null;
        try
        {
        	ois = new ObjectInputStream(bais);
	        int listSize = Integer.parseInt(ois.readObject().toString());
        	
	        for(int i=0;i<listSize;i++)
	        {
	        	Object o = (Object)ois.readObject();
	        	list.add(o);
	        	
	        }
	        return list;
        }finally
        {
        	if(ois!=null)
        		ois.close();
        	if(bais!=null)
        		bais.close();
        }
	}
	
	public static byte[] encodeRowSet(List<CachedRowSet> crss) throws IOException
	{
		if(crss == null)
			return null;
		ByteArrayOutputStream   baos=new   ByteArrayOutputStream(); 
        ObjectOutputStream oos = null;
        try
        {
        	oos = new ObjectOutputStream(baos);
        	oos.writeObject(crss.size());
        	for(int i=0;i<crss.size();i++)
        		oos.writeObject(crss.get(i));
        	byte[] bytes = baos.toByteArray();
        	oos.flush();
        	return bytes;
        }
        finally
        {
        	if(oos!=null)
        		oos.close();
        	if(baos!=null)
        		baos.close();
        }
        
	}
	
	public static List<CachedRowSet> decodeRowSet(byte[] bytes) throws ClassNotFoundException, IOException
	{
		if(bytes == null)
			return null;
		ByteArrayInputStream   bais=new   ByteArrayInputStream(bytes); 
		ObjectInputStream ois = null;
        try {
        	ois = new ObjectInputStream(bais);
        	int size =  Integer.parseInt(ois.readObject().toString()); //ois.readInt();
        	List<CachedRowSet> list = new ArrayList<CachedRowSet>();
        	for(int i=0;i<size;i++)
        	{
        		CachedRowSet rowSet = (CachedRowSet)ois.readObject();
        		list.add(rowSet);
        	}
			return list;
		} finally {
			if(ois!=null)
				ois.close();
			if(bais!=null)
				bais.close();
		}
	}
	
}
