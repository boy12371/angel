package test.com.feinno.runtime.bytecode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import test.com.feinno.serialization.protobuf.DataCreater;
import test.com.feinno.serialization.protobuf.bean.FullElementsBean;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoEntityEnhancer;

/**
 * 
 * <b>描述: </b>这是一个修改Class的测试用例<br>
 * 第一个测试用例将FullElementsBean类中的set方法进行拦截，动态的增加指令码，
 * 使之put标识符到Field中,通过hasValue判断是否put成功<br>
 * 第二个用例遍历com和test包下全部继承自ProtoEntity的类，均为之加入拦截代码
 * <p>
 * <b>功能: </b>这是一个修改Class的测试用例
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class TestModifyClassFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new TestModifyClassFile().test1();
		new TestModifyClassFile().test2();
	}

	@Test
	public void test1() throws Exception {
		FullElementsBean fullElementsBean = ProtoEntityEnhancer.getEnhanceProtoEntityClass(FullElementsBean.class)
				.newInstance();
		DataCreater.fillToFullElementsBean(fullElementsBean);
		Assert.assertTrue(fullElementsBean.hasValue(101));
		Assert.assertTrue(fullElementsBean.hasValue(201));
		Assert.assertTrue(fullElementsBean.hasValue(301));
		Assert.assertTrue(fullElementsBean.hasValue(401));
		Assert.assertTrue(fullElementsBean.hasValue(501));
		Assert.assertTrue(fullElementsBean.hasValue(601));
		Assert.assertTrue(fullElementsBean.hasValue(701));
	}

	@Test
	public void test2() throws Exception {
		for (Class<?> clazz : getAllAssignedClass(ProtoEntity.class, "com.feinno")) {
			System.out.println(clazz.getName());
			ProtoEntityEnhancer.getEnhanceProtoEntityClass((Class<ProtoEntity>) clazz);
		}
		for (Class<?> clazz : getAllAssignedClass(ProtoEntity.class, "test")) {
			System.out.println(clazz.getName());
			ProtoEntityEnhancer.getEnhanceProtoEntityClass((Class<ProtoEntity>) clazz);
		}
	}

	/**
	 * 获取同一路径下所有子类或接口实现类
	 * 
	 * @param intf
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> getAllAssignedClass(Class<?> cls, String packageName) throws IOException,
			ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (Class<?> c : getClasses(cls, packageName)) {
			if (cls.isAssignableFrom(c) && !cls.equals(c)) {
				classes.add(c);
			}
		}
		return classes;
	}

	/**
	 * 取得当前类路径下的所有类
	 * 
	 * @param cls
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> getClasses(Class<?> cls, String packageName) throws IOException,
			ClassNotFoundException {
		String path = packageName.replace('.', '/');
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		URL url = classloader.getResource(path);
		if (url != null) {
			return getClasses(new File(url.getFile()), packageName);
		} else {
			return new ArrayList<Class<?>>();
		}
	}

	/**
	 * 迭代查找类
	 * 
	 * @param dir
	 * @param pk
	 * @return
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!dir.exists()) {
			return classes;
		}
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				classes.addAll(getClasses(f, pk + "." + f.getName()));
			}
			String name = f.getName();
			if (name.endsWith(".class")) {
				try {
					classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
				} catch (Throwable e) {
					// 允许部分类没有默认的构造方法
				}
			}
		}
		return classes;
	}
}
