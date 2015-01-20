package test.com.feinno.runtime.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.Test;

import com.feinno.runtime.bytecode.type.ClassFile;
import com.feinno.runtime.bytecode.type.InvalidByteCodeException;
import com.feinno.runtime.bytecode.type.MethodInfo;
import com.feinno.runtime.bytecode.util.InstructionUtil;

/**
 * 
 * <b>描述: </b>这是一个用于保证ClassFile正确性的测试用例，它将当前工程中全部的类及类中的方法区指令字段解析一遍
 * <p>
 * <b>功能: </b>这是一个用于保证ClassFile正确性的测试用例
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class TestClassFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new TestClassFile().test1();
	}

	@Test
	public void test1() throws Exception {
		analyzePackage("com");
		analyzePackage("org");
		analyzePackage("net");
	}

	public static void analyzePackage(String packageName) throws IOException, InvalidByteCodeException {
		if (packageName == null) {
			return;
		}
		packageName = packageName.replaceAll("\\.", "/");
		if (!packageName.endsWith("/")) {
			packageName += "/";
		}
		try {
			Enumeration<URL> enumeration = Thread.currentThread().getContextClassLoader().getResources(packageName);
			while (enumeration.hasMoreElements()) {
				URL url = (URL) enumeration.nextElement();
				String protocol = url.getProtocol();
				if (protocol.equals("jar")) {
					analyzeJAR(url);
				} else {
					analyzeFile(url, packageName);
				}
			}

		} catch (Exception e) {
			throw new RuntimeException("loadPackage", e);
		}
	}

	/**
	 * 处理jar中的class
	 * 
	 * @param url
	 * @throws IOException
	 */
	private static void analyzeJAR(URL url) throws ClassNotFoundException, IOException, InvalidByteCodeException {
		JarURLConnection con = (JarURLConnection) url.openConnection();
		JarFile file = con.getJarFile();
		Enumeration<?> enumeration = file.entries();
		while (enumeration.hasMoreElements()) {
			JarEntry element = (JarEntry) enumeration.nextElement();
			String entryName = element.getName();
			if (entryName != null && entryName.endsWith(".class")) {
				analyzeClass(entryName);
			}
		}
	}

	/**
	 * 处理文件目录中的class
	 * 
	 * @param url
	 * @throws URISyntaxException
	 */
	private static void analyzeFile(URL url, String packageName) throws ClassNotFoundException, URISyntaxException,
			IOException, InvalidByteCodeException, MalformedURLException {
		File file = new File(new URI(url.toExternalForm()));
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				analyzeFile(files[i].toURI().toURL(), packageName + files[i].getName() + "/");
			} else {
				String entryName = packageName + files[i].getName();
				if (entryName != null && entryName.endsWith(".class")) {
					analyzeClass(entryName);
				}
			}
		}
	}

	private static void analyzeClass(String path) throws IOException, InvalidByteCodeException {
		path = path.startsWith("/") ? path : "/" + path;
		InputStream input = TestClassFile.class.getResourceAsStream(path);
		analyzeClass(input);
	}

	public static void analyzeClass(File file) throws IOException, InvalidByteCodeException {
		InputStream input = new FileInputStream(file);
		analyzeClass(input);
	}

	public static void analyzeClass(InputStream input) throws IOException, InvalidByteCodeException {
		byte[] buffer = toBytes(input);
		ClassFile classFile = null;
		try {
			classFile = ClassFile.valueOf(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (classFile != null) {
			analyzeMethod(classFile);
		}
	}

	private static int counter = 0;
	
	public static void analyzeMethod(ClassFile classFile) {
		System.out.println("正在解析第"+(counter++)+"个Class :  "+classFile.getConstant_pool()[classFile.getThis_class()]);
		classFile.toString();
		classFile.toByteArray();
		for (MethodInfo method : classFile.getMethods()) {
			if (method.getCodeAttribute() != null) {
				InstructionUtil.toInstructionString(classFile, method.getCodeAttribute().getCodes());
			} else {
				// 可能为接口,因此没有方法区
				// System.out.println("CodeAttribute is null.");
			}

		}
	}

	public static byte[] toBytes(InputStream input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = 0;
		byte[] b = new byte[1024];
		while ((len = input.read(b, 0, b.length)) != -1) {
			baos.write(b, 0, len);
		}
		byte[] buffer = baos.toByteArray();
		return buffer;
	}

	public static void writeByteToFile(String path, byte[] buffer) throws Exception {
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(buffer);
		fos.close();
	}
}
