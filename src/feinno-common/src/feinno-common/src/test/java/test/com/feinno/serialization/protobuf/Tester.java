package test.com.feinno.serialization.protobuf;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import test.com.feinno.serialization.protobuf.bean.BooleanTest;
import test.com.feinno.serialization.protobuf.bean.DefineTypeBean;

import com.feinno.serialization.Serializer;
import com.feinno.serialization.protobuf.ProtoBuilder;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoManager;
import com.feinno.serialization.protobuf.util.FileUtil;
import com.feinno.serialization.protobuf.util.ProtoTransferEntity;

/**
 * 单元测试类<br>
 * 部分测试类型需要下面三个工程的支持,请确保本工程中有下面工程的引用: <br>
 * <code>
 * 1.feinno-common
 * 2.feinno-ha
 * 3.feinno-imps
 * </code>
 * 
 * @author Lv.Mingwei
 * 
 */
public class Tester {

	public static int counter = 0;

	private static final DecimalFormat df = new DecimalFormat("0.0000");

	public byte[] buffer = null;

	@Test
	public void test() throws Exception {
		// 开启Debug模式，会生成源码到根目录下,如果在eclipse环境下找不到，且没修改过配置，那么应该在bin目录下，默认是不开启Debug模式
		// ProtoManager.setDebug(true);

		//
		// 是否启用新版本,这个我在本机环境修改了common工程中的ProtobufCodecFactory类，增加了静态的isEnableNewProtoBuf变量
		// 用于切换新老版本的序列化方式来判断序列化效率以及兼容性，因类文件位于common工程中所以无法上传，其他环境请在这里注释掉下段代码
		// com.feinno.serialization.protobuf.ProtobufCodecFactory.isEnableNewProtoBuf
		// = true;
		//
		// 性能测试

		testPerformance(100000);
		// 用于兼容性的测试
		// tester.testCompatibility();

		// 交叉序列化,验证字段完整性,序列化一个A类型,用B类型反序列化,
		// 再把反序列化的B类型序列化,再用A类型反序列化,正常情况下内容不应该丢失
		testWriteByNewFeinno(DataCreater.newSpecialFieldBean(true));
		test.com.feinno.serialization.protobuf.bean.Person person = DataCreater.newPerson(false);
		testParseByNewFeinno(person);
		testWriteByNewFeinno(person);
		testParseByNewFeinno(DataCreater.newSpecialFieldBean(false));

		// 未知字段测试
		testUnknownField();
		//
		// // MAP测试
		testWriteByNewFeinno(DataCreater.newSimpleMapBean(true));
		testParseByNewFeinno(DataCreater.newSimpleMapBean(false));
		testWriteByNewFeinno(DataCreater.newMapBean(true));
		// tester.testParseByFeinno(DataCreater.newListBean(false));
		// tester.testWriteByFeinno(DataCreater.newListBean(true));
		testParseByNewFeinno(DataCreater.newMapBean(false));

		testWriteByNewFeinno(DataCreater.newSpecialFieldBean(true));
		testParseByNewFeinno(DataCreater.newSpecialFieldBean(false));

		testWriteByNewFeinno(DataCreater.newSimpleMapBean(true));//
		// 新版本序列化一个MAP
		testWriteByNewFeinno(DataCreater.newSimpleMapBean(true));//
		// 使用新版本将MAP序列化
		testParseByNewFeinno(DataCreater.newSimpleMapBean(false));//
		// 新版本再使用MAP接受信息

		// EnumType<?>类型的测试
		testWriteByNewFeinno(DataCreater.newEnumTypeBean(true));
		testParseByNewFeinno(DataCreater.newEnumTypeBean(false));
		// 全类型测试
		testWriteByNewFeinno(DataCreater.newFullElementsBean(true));
		// tester.filePath = "D:\\serialize.out";
		testParseByNewFeinno(DataCreater.newFullElementsBean(false));

		// 自定义序列TYPE的测试
		test.com.feinno.serialization.protobuf.bean.DefineTypeBean defineTypeBean = DataCreater.newDefineTypeBean(true);
		defineTypeBean.setArray(new DefineTypeBean[] { DataCreater.newDefineTypeBean(true),
				DataCreater.newDefineTypeBean(true) });
		testWriteByNewFeinno(defineTypeBean);
		testParseByNewFeinno(DataCreater.newDefineTypeBean(false));
		testParseByNewFeinno(DataCreater.newTable(false));

		// // 调试工具的测试
		com.feinno.serialization.protobuf.ProtoDebug.testWriteAndMerage(DataCreater.newFullElementsBean(true));

		TestNative nativeTester = new TestNative();
		nativeTester.test();
		
		ProtoManager.toByteArray(new BooleanTest());
	}

	/**
	 * 用于测试未知类型的序列化与反序列化时的安全性
	 * 
	 * @throws IOException
	 */
	public void testUnknownField() throws IOException {
		// 首先序列化一个大类型
		testWriteByNewFeinno(DataCreater.newPerson(true));
		// 用小类型来反序列化大类型中的值，无法识别的值会被存储在UnknownField中
		ProtoEntity proto = DataCreater.newTable(false);
		testParseByNewFeinno(proto);
		System.out.println("--未知类型----------------------------------------------------");
		System.out.println(proto.getUnknownFields());

		// 再把小类型序列化进流中
		testWriteByNewFeinno(proto);
		// 再用大类型反序列化一下，看数据是否丢失
		proto = DataCreater.newPerson(false);
		testParseByNewFeinno(proto);
		System.out.println("--未知类型----------------------------------------------------");
		System.out.println(proto.getUnknownFields());

		// 再把反序列化出来的大类型序列化到流中
		testParseByNewFeinno(proto);
		// 再用小类型将他取出
		proto = new ProtoTransferEntity();
		testParseByNewFeinno(proto);
		System.out.println("--未知类型----------------------------------------------------");
		System.out.println(proto.getUnknownFields());

		// 再用大类型将上一步序列化的小类型取出,查看未知字段能否填充回这个大类型的相应字段中
		proto = DataCreater.newPerson(false);
		testParseByNewFeinno(proto);
		System.out.println("--未知类型----------------------------------------------------");
		System.out.println(proto.getUnknownFields());

	}

	/**
	 * 用于兼容性的测试,首先使用老版序列化一个person对象，用新版反序列化，再用新版序列化，用老版反序列化，看是否有丢失对象的情况
	 */
	public void testCompatibility() throws IOException {
		// 老版序列化一个person对象,用新版反序列化
		testWriteByFeinno(DataCreater.newPerson(true));
		testParseByNewFeinno(DataCreater.newPerson(false));
		// 再用新版序列化，用老版反序列化，看是否有丢失对象的情况
		testWriteByNewFeinno(DataCreater.newPerson(true));
		testParseByFeinno(DataCreater.newPerson(false));
		// 老版序列化一个SinBean对象,用新版反序列化
		testWriteByFeinno(DataCreater.newSinBean(true));
		testParseByNewFeinno(DataCreater.newSinBean(false));
		// 再用新版序列化，用老版反序列化，看是否有丢失对象的情况
		testWriteByNewFeinno(DataCreater.newSinBean(true));
		testParseByFeinno(DataCreater.newSinBean(false));

	}

	/**
	 * 用于性能测试
	 * 
	 * @throws IOException
	 */
	public void testPerformance(int count) throws IOException {
		System.out.println("正在进行" + count + "次的序列及反序列化性能统计,大概需要" + (count / 100 + 1000) + "毫秒,请稍后!");
		Date startDate = new Date();
		// 第一次为预加载，因此忽略时间
		// DataCreater.newEnumListToMapBean(false).newProtoBuilder();
		DataCreater.newPerson(false).toByteArray();

		long[][] time = new long[count][10];
		long writeTimeGoogle = 0L;
		long parseTimeGoogle = 0L;
		long writeTimeByFeinno = 0L;
		long parseTimeByFeinno = 0L;
		long writeTimeByNewFeinno = 0L;
		long parseTimeByNewFeinno = 0L;
		long fullWriteTimeByFeinno = 0L;
		long fullParseTimeByFeinno = 0L;
		long fullWriteTimeByNewFeinno = 0L;
		long fullParseTimeByNewFeinno = 0L;
		for (int i = 0; i < time.length; i++) {
			// time[i][0] = testWriteByGoogle();
			// time[i][1] = testParseByGoogle();
			// time[i][2] = testWriteByFeinno(DataCreater.newTable(true));
			// time[i][3] = testParseByFeinno(DataCreater.newTable(false));
			time[i][4] = testWriteByNewFeinno(DataCreater.newTable(true));
			time[i][5] = testParseByNewFeinno(DataCreater.newTable(false));
			// time[i][6] = testWriteByFeinno(DataCreater.newPerson(true));
			// time[i][7] = testParseByFeinno(DataCreater.newPerson(false));
			// time[i][8] = testWriteByNewFeinno(DataCreater.newPerson(true));
			// time[i][9] = testParseByNewFeinno(DataCreater.newPerson(false));

			writeTimeGoogle += time[i][0];
			parseTimeGoogle += time[i][1];
			writeTimeByFeinno += time[i][2];
			parseTimeByFeinno += time[i][3];
			writeTimeByNewFeinno += time[i][4];
			parseTimeByNewFeinno += time[i][5];
			fullWriteTimeByFeinno += time[i][6];
			fullParseTimeByFeinno += time[i][7];
			fullWriteTimeByNewFeinno += time[i][8];
			fullParseTimeByNewFeinno += time[i][9];
			// 多线程测试时请将此输出注释掉
			// System.out.println("-------------------------------");
			// System.out.println("Google:");
			// System.out.println(i + "   序列化" + time[i][0]);
			// System.out.println(i + " 反序列化" + time[i][1]);
			// System.out.println("Feinno:");
			// System.out.println(i + "   序列化" + time[i][2]);
			// System.out.println(i + " 反序列化" + time[i][3]);
			// System.out.println("New Feinno:");
			// System.out.println(i + "   序列化" + time[i][4]);
			// System.out.println(i + " 反序列化" + time[i][5]);
			// System.out.println("全面覆盖类型的测试 Feinno:");
			// System.out.println(i + "   序列化" + time[i][6]);
			// System.out.println(i + " 反序列化" + time[i][7]);
			// System.out.println("全面覆盖类型的测试 New Feinno:");
			// System.out.println(i + "   序列化" + time[i][8]);
			// System.out.println(i + " 反序列化" + time[i][9]);
		}

		System.out.println("========" + time.length + "次耗时汇总==========");
		System.out.println("Google:");
		System.out.println(time.length + "次序列化总耗时     :"
				+ TimeUnit.MILLISECONDS.convert(writeTimeGoogle, TimeUnit.NANOSECONDS) + "毫秒，比例:"
				+ df.format(Double.valueOf(writeTimeGoogle) / Double.valueOf(writeTimeGoogle)));

		System.out.println(time.length + "次反序列化总耗时:"
				+ TimeUnit.MILLISECONDS.convert(parseTimeGoogle, TimeUnit.NANOSECONDS) + "毫秒，比例:"
				+ df.format(Double.valueOf(parseTimeGoogle) / Double.valueOf(parseTimeGoogle)));
		System.out.println();
		System.out.println("Feinno:");
		System.out.println(time.length + "次序列化总耗时     :"
				+ TimeUnit.MILLISECONDS.convert(writeTimeByFeinno, TimeUnit.NANOSECONDS) + "毫秒，比例:"
				+ df.format(Double.valueOf(writeTimeByFeinno) / Double.valueOf(writeTimeGoogle)));

		System.out.println(time.length + "次反序列化总耗时:"
				+ TimeUnit.MILLISECONDS.convert(parseTimeByFeinno, TimeUnit.NANOSECONDS) + "毫秒，比例:"
				+ df.format(Double.valueOf(parseTimeByFeinno) / Double.valueOf(parseTimeGoogle)));
		System.out.println();
		System.out.println("New Feinno:");
		System.out.println(time.length + "次序列化总耗时     :"
				+ TimeUnit.MILLISECONDS.convert(writeTimeByNewFeinno, TimeUnit.NANOSECONDS) + "毫秒，比例:"
				+ df.format(Double.valueOf(writeTimeByNewFeinno) / Double.valueOf(writeTimeGoogle)));

		System.out.println(time.length + "次反序列化总耗时:"
				+ TimeUnit.MILLISECONDS.convert(parseTimeByNewFeinno, TimeUnit.NANOSECONDS) + "毫秒，比例:"
				+ df.format(Double.valueOf(parseTimeByNewFeinno) / Double.valueOf(parseTimeGoogle)));

		System.out.println();
		System.out.println("全面覆盖类型的测试  Feinno:");
		System.out.println(time.length + "次序列化总耗时     :"
				+ TimeUnit.MILLISECONDS.convert(fullWriteTimeByFeinno, TimeUnit.NANOSECONDS) + "毫秒，比例:"
				+ df.format(Double.valueOf(fullWriteTimeByFeinno) / Double.valueOf(writeTimeGoogle)));

		System.out.println(time.length + "次反序列化总耗时:"
				+ TimeUnit.MILLISECONDS.convert(fullParseTimeByFeinno, TimeUnit.NANOSECONDS) + "毫秒，比例:"
				+ df.format(Double.valueOf(fullParseTimeByFeinno) / Double.valueOf(parseTimeGoogle)));

		System.out.println();
		System.out.println("全面覆盖类型的测试  New Feinno:");
		System.out.println(time.length + "次序列化总耗时     :"
				+ TimeUnit.MILLISECONDS.convert(fullWriteTimeByNewFeinno, TimeUnit.NANOSECONDS) + "毫秒，比例:"
				+ df.format(Double.valueOf(fullWriteTimeByNewFeinno) / Double.valueOf(writeTimeGoogle)));

		System.out.println(time.length + "次反序列化总耗时:"
				+ TimeUnit.MILLISECONDS.convert(fullParseTimeByNewFeinno, TimeUnit.NANOSECONDS) + "毫秒，比例:"
				+ df.format(Double.valueOf(fullParseTimeByNewFeinno) / Double.valueOf(parseTimeGoogle)));

		System.out.println("Start:" + startDate + "------The End:" + new Date());
		System.out.println("共耗时:" + (System.currentTimeMillis() - startDate.getTime())
				+ "毫秒,此时间包括测试数据的生成以及其他准备工作所消耗的时间.");
	}

	// public long testWriteByGoogle() throws IOException {
	// // FileOutputStream output = new FileOutputStream(filePath);
	// TableProtos.Table.Builder builder = TableProtos.Table.newBuilder();
	// builder.setId(1);
	// builder.setName("我是Table");
	// builder.setEmail("Table@163.com");
	// TableProtos.Table.User.Builder builder2 =
	// TableProtos.Table.User.newBuilder();
	// builder2.setId(2);
	// builder2.setName("我是User");
	// builder2.setEmail("User@163.com");
	// builder.setUser(builder2);
	// TableProtos.Table table = builder.build();
	//
	// long startTime = System.nanoTime();
	// buffer = table.toByteArray();
	// long resultTime = System.nanoTime() - startTime;
	//
	// // output.close();
	// return resultTime;
	// }
	//
	// public long testParseByGoogle() throws IOException {
	// // FileInputStream input = new FileInputStream(filePath);
	// // byte[] array = getBytesFromIS(input);
	// TableProtos.Table.Builder builder = TableProtos.Table.newBuilder();
	//
	// long startTime = System.nanoTime();
	// builder.mergeFrom(buffer);
	// builder.build();
	// long resultTime = System.nanoTime() - startTime;
	//
	// // input.close();
	// return resultTime;
	// }

	public long testWriteByFeinno(ProtoEntity proto) throws IOException {
		String filePath = null;
		if (Tester.class.getResource("/") != null) {
			filePath = Tester.class.getResource("/").getPath() + System.nanoTime() + "Test.data";
		} else {
			filePath = Tester.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		}

		FileOutputStream output = new FileOutputStream(filePath);

		long startTime = System.nanoTime();
		Serializer.encode(proto, output);
		long resultTime = System.nanoTime() - startTime;

		output.close();
		return resultTime;
	}

	public long testParseByFeinno(ProtoEntity proto) throws IOException {
		// FileInputStream input = new FileInputStream(filePath);
		// byte[] array = getBytesFromIS(input);

		long startTime = System.nanoTime();
		proto = Serializer.decode(proto.getClass(), buffer);
		long resultTime = System.nanoTime() - startTime;

		// input.close();
		return resultTime;
	}

	public long testWriteByNewFeinno(ProtoEntity proto) throws IOException {
		// FileOutputStream output = new FileOutputStream(filePath);
		ProtoBuilder<ProtoEntity> builder = ProtoManager.getProtoBuilder(proto);

		long startTime = System.nanoTime();
		buffer = builder.toByteArray();
		long resultTime = System.nanoTime() - startTime;

		// output.close();
		return resultTime;
	}

	public long testParseByNewFeinno(ProtoEntity proto) throws IOException {
		// FileInputStream input = new FileInputStream(filePath);
		// byte[] array = getBytesFromIS(input);
		ProtoBuilder<ProtoEntity> builder = ProtoManager.getProtoBuilder(proto);

		long startTime = System.nanoTime();
		builder.parseFrom(buffer);
		builder.getData();
		long resultTime = System.nanoTime() - startTime;

		// input.close();
		return resultTime;
	}

	public static byte[] getBytesFromIS(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b = 0;
		while ((b = is.read()) != -1)
			baos.write(b);
		return baos.toByteArray();
	}

	public static byte[] getBytesFromIS(String path) throws IOException {
		String string = FileUtil.read(path).trim();
		if (string.startsWith("[")) {
			string = string.substring(1, string.length());
		}
		if (string.endsWith("]")) {
			string = string.substring(0, string.length() - 1);
		}
		String[] strArray = string.split(",");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (String str : strArray) {
			baos.write(Byte.valueOf(str.trim()));
		}
		return baos.toByteArray();
	}

	public static void coverFileByteToFileString(String sourcePath, String destPath) throws IOException {
		String body = Arrays.toString(getBytesFromIS(new FileInputStream(sourcePath)));
		FileUtil.write(body, destPath);
	}
}
