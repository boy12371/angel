package test.com.feinno.runtime.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.feinno.runtime.bytecode.instruction.OpcodeEnum;
import com.feinno.runtime.bytecode.type.AttributeType;
import com.feinno.runtime.bytecode.type.CPInfo;
import com.feinno.runtime.bytecode.type.ClassFile;
import com.feinno.runtime.bytecode.type.CodeAttribute;
import com.feinno.runtime.bytecode.type.ConstantClassInfo;
import com.feinno.runtime.bytecode.type.ConstantFieldrefInfo;
import com.feinno.runtime.bytecode.type.ConstantNameAndTypeInfo;
import com.feinno.runtime.bytecode.type.ConstantUTF8Info;
import com.feinno.runtime.bytecode.type.LineNumberTableAttribute;
import com.feinno.runtime.bytecode.type.LineNumberTableAttribute.LineNumberTable;
import com.feinno.runtime.bytecode.type.LocalVariableTableAttribute;
import com.feinno.runtime.bytecode.type.LocalVariableTableAttribute.LocalVariableTable;
import com.feinno.runtime.bytecode.type.LocalVariableTypeTableAttribute;
import com.feinno.runtime.bytecode.type.LocalVariableTypeTableAttribute.LocalVariableTypeTable;
import com.feinno.runtime.bytecode.type.MethodInfo;
import com.feinno.runtime.bytecode.util.InstructionUtil;

public class TestDynamicGet {

	public static int counter = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new TestDynamicGet().test();
	}

	public void test() throws Exception {

		String path = "/test/com/feinno/runtime/bytecode/DemoClass.class";
		ClassFile classFile = processClass(path);
		// 创建这个类并执行这个类的Test方法
		Class newClass = createClass(classFile);
		IDemo demo = (IDemo) newClass.newInstance();
		// 执行控制台监控和刚刚生成的class代码
		consoleMonitor();
		demo.test(100);

	}

	public ClassFile processClass(String path) throws Exception {
		ClassFile classFile = ClassFile.valueOf(toBytes(path));

		// Step 1.向常量池增加一个该类的引用
		List<CPInfo> newCpInfoList = new ArrayList<CPInfo>();
		CPInfo[] cpInfos = classFile.getConstant_pool();
		for (CPInfo cpInfo : cpInfos) {
			newCpInfoList.add(cpInfo);
		}
		int testDynamicClassDescIndex = newCpInfoList.size();
		newCpInfoList.add(new ConstantUTF8Info(classFile, "test/com/feinno/runtime/bytecode/TestDynamicGet"));
		int testDynamicClassIndex = newCpInfoList.size();
		newCpInfoList.add(new ConstantClassInfo(classFile, testDynamicClassDescIndex));
		int counterIndex = newCpInfoList.size();
		newCpInfoList.add(new ConstantUTF8Info(classFile, "counter"));
		int IIndex = newCpInfoList.size();
		newCpInfoList.add(new ConstantUTF8Info(classFile, "I"));
		int nameAndType = newCpInfoList.size();
		newCpInfoList.add(new ConstantNameAndTypeInfo(classFile, counterIndex, IIndex));
		int fieldIndex = newCpInfoList.size();
		newCpInfoList.add(new ConstantFieldrefInfo(classFile, testDynamicClassIndex, nameAndType));
		byte[] newBackupCodes = null;
		// Step2.处理方法区
		List<MethodInfo> newMethodInfoList = new ArrayList<MethodInfo>();
		for (MethodInfo method : classFile.getMethods()) {
			if (method.getName().getValue().equals("test")) {
				// 取得指令码
				CodeAttribute codeAttribute = method.getCodeAttribute();
				int oldLength = codeAttribute.getLength();
				byte[] codes = codeAttribute.getCodes();
				byte[] newCodes = new byte[codes.length + 8];
				int pos = 16;
				System.arraycopy(codes, 0, newCodes, 0, pos);
				newCodes[8] = (byte) (newCodes[8] + 8);
				// 将静态field压入栈顶
				newCodes[pos++] = OpcodeEnum.GETSTATIC.getOpcode();
				newCodes[pos++] = (byte) (fieldIndex >>> 8 & 0xFF);
				newCodes[pos++] = (byte) (fieldIndex >>> 0 & 0xFF);
				// 将1压入栈顶，并计算和值
				newCodes[pos++] = OpcodeEnum.ICONST_1.getOpcode();
				newCodes[pos++] = OpcodeEnum.IADD.getOpcode();
				newCodes[pos++] = OpcodeEnum.PUTSTATIC.getOpcode();
				newCodes[pos++] = (byte) (fieldIndex >>> 8 & 0xFF);
				newCodes[pos++] = (byte) (fieldIndex >>> 0 & 0xFF);
				System.out.println(pos);
				System.arraycopy(codes, 16, newCodes, pos++, codes.length - 16);
				newCodes[newCodes.length - 2] = (byte) (newCodes[newCodes.length - 2] - 8);
				codeAttribute.setCodes(newCodes);
				clearCodeAttribute(codeAttribute);
				int newLength = codeAttribute.getLength();
				method.setCorrectOffset(newLength - oldLength);

				newMethodInfoList.add(method);
				newBackupCodes = newCodes;
				// System.out.println(Arrays.toString(codes));
				System.out.println(Arrays.toString(newCodes));

			} else {
				newMethodInfoList.add(method);
			}

		}

		CPInfo[] newCpInfos = new CPInfo[newCpInfoList.size()];
		newCpInfoList.toArray(newCpInfos);
		classFile.writeToCPInfo(newCpInfos);

		MethodInfo[] newMethodInfos = new MethodInfo[newMethodInfoList.size()];
		newMethodInfoList.toArray(newMethodInfos);
		classFile.writeToMethodInfo(newMethodInfos);
		System.out.println(InstructionUtil.toInstructionString(classFile, newBackupCodes));

		return classFile;
	}

	public void consoleMonitor() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					System.err.println("Please input Option");
					System.err.print(">");
					Scanner scanner = new Scanner(System.in);
					String line = scanner.next();
					if (line.equals("get")) {
						System.err.println(counter - 1);
					} else if (line.equals("stop")) {
						System.exit(1);
					}
				}

			}
		}).start();
	}

	public static byte[] toBytes(String path) {
		try {
			InputStream input = TestDynamicGet.class.getResourceAsStream(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] b = new byte[1024];
			while ((len = input.read(b, 0, b.length)) != -1) {
				baos.write(b, 0, len);
			}
			byte[] buffer = baos.toByteArray();
			return buffer;
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static Class createClass(ClassFile classFile) throws Exception {
		defineClass1.setAccessible(true);
		Object[] args = new Object[] { null, classFile.getBuffer(), new Integer(0),
				new Integer(classFile.getBuffer().length) };
		Class newClass = (Class) defineClass1.invoke(Thread.currentThread().getContextClassLoader(), args);
		defineClass1.setAccessible(false);
		return newClass;
	}

	/**
	 * 清理CodeAttribute属性
	 * 
	 * @param codeAttribute
	 */
	private void clearCodeAttribute(CodeAttribute codeAttribute) {
		// 修改第一个参数this的常量池描述为当前类的描述
		LocalVariableTableAttribute localVariableTableAttribute = (LocalVariableTableAttribute) codeAttribute
				.getAttribute(AttributeType.LOCALVARIABLETABLE);
		if (localVariableTableAttribute != null && localVariableTableAttribute.getLineVariableTables() != null
				&& localVariableTableAttribute.getLineVariableTables().length > 0) {
			for (LocalVariableTable localVariableTable : localVariableTableAttribute.getLineVariableTables()) {
				localVariableTable.setStartPc(0);
				localVariableTable.setLengthPc(codeAttribute.getCodes().length);
			}
		}
		// 修改泛型变量签名
		LocalVariableTypeTableAttribute localVariableTypeTableAttribute = (LocalVariableTypeTableAttribute) codeAttribute
				.getAttribute(AttributeType.LOCALVARIABLETYPETABLE);
		if (localVariableTypeTableAttribute != null
				&& localVariableTypeTableAttribute.getLineVariableTypeTables() != null
				&& localVariableTypeTableAttribute.getLineVariableTypeTables().length > 0) {
			for (LocalVariableTypeTable localVariableTypeTable : localVariableTypeTableAttribute
					.getLineVariableTypeTables()) {
				localVariableTypeTable.setStartPc(0);
				localVariableTypeTable.setLengthPc(codeAttribute.getCodes().length);
			}
		}
		// 修改LineNumberTableAttribute属性，该属性用于调试器，但是不修改将使运行报错
		LineNumberTableAttribute lineNumberTableAttribute = (LineNumberTableAttribute) codeAttribute
				.getAttribute(AttributeType.LINENUMBERTABLE);
		if (lineNumberTableAttribute != null && lineNumberTableAttribute.getLineNumberTables() != null
				&& lineNumberTableAttribute.getLineNumberTables().length > 0) {
			for (LineNumberTable lineNumberTable : lineNumberTableAttribute.getLineNumberTables()) {
				lineNumberTable.setStartPc(0);
				lineNumberTable.setLineNumber(0);
			}
		}
	}

	private static java.lang.reflect.Method defineClass1;

	static {
		try {
			AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
				public Object run() throws Exception {
					Class<?> cl = Class.forName("java.lang.ClassLoader");
					defineClass1 = cl.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class,
							int.class, int.class });
					return null;
				}
			});
		} catch (PrivilegedActionException pae) {
			throw new RuntimeException("cannot initialize ClassLoader", pae.getException());
		}
	}
}
