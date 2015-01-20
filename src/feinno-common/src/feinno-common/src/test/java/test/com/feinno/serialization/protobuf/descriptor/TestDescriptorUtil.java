package test.com.feinno.serialization.protobuf.descriptor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Map;

import com.feinno.diagnostic.dumper.ObjectDumper;
import com.feinno.serialization.protobuf.descriptor.DescriptorUtil;
import com.feinno.serialization.protobuf.descriptor.FileDescriptorSet;

import freemarker.template.TemplateException;

public class TestDescriptorUtil {

	private static void writeFile(String source, File path) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(path);
			fw.write(source);
		} finally {
			fw.flush();
			fw.close();
		}
	}

	/**
	 * 
	 * @param args
	 * @throws TemplateException
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException,
			TemplateException {
		FileDescriptorSet fileDescriptorSet = DescriptorUtil
				.getDescriptor(OuterClass.class);
		System.out.println(ObjectDumper.dumpString(fileDescriptorSet));

		Map<String, String> map = DescriptorUtil
				.generateEntityCode(fileDescriptorSet);

		for (Map.Entry<String, String> en : map.entrySet()) {
			String key = en.getKey();
			String value = en.getValue();

			writeFile(value, new File("/home/yanghu", key+".java"));
		}

		// System.out.println(DescriptorUtil.generateProtoFile(fileDescriptorSet));

		// System.out.println(DescriptorUtil.generateEntityCode(fileDescriptorSet));
	}

}
