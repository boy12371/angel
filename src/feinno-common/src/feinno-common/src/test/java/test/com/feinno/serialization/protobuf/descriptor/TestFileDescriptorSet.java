package test.com.feinno.serialization.protobuf.descriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.feinno.diagnostic.dumper.ObjectDumper;
import com.feinno.serialization.Serializer;
import com.feinno.serialization.protobuf.descriptor.FileDescriptorSet;

public class TestFileDescriptorSet {

	public static byte[] readFile(File file) throws IOException {

		FileInputStream fs = new FileInputStream(file);
		int size = fs.available();
		byte[] buf = new byte[size];
		fs.read(buf);
		fs.close();
		return buf;
	}

	public static void writeFile(File file, byte[] buf) throws IOException {
		FileOutputStream fs = new FileOutputStream(file);
		fs.write(buf);
		fs.flush();
		fs.close();
	}

	public static FileDescriptorSet decode(byte[] buf) throws IOException {

		FileDescriptorSet desc = Serializer
				.decode(FileDescriptorSet.class, buf);
		System.out.println(ObjectDumper.dumpString(desc));
		return desc;
	}

	public static byte[] encode(FileDescriptorSet desc) throws IOException {
		return Serializer.encode(desc);
	}

	public static void main(String[] argv) throws IOException {

		String path = TestFileDescriptorSet.class.getResource("").getPath();
		
		/*
		File file1 = new File(path, "entity.ftl");
		byte[] buf3 = readFile(file1);
		*/
		
		File file = new File(path, "test.pb");
		
		byte[] buf = readFile(file);

		FileDescriptorSet desc = decode(buf);

		byte[] buf2 = encode(desc);

		/*
		File fo = new File("/home/yanghu/test_out.pb");

		writeFile(fo, buf2);
		*/
		
		if (buf.length == buf2.length) {
			for (int i = 0; i < buf.length; i++) {
				if (buf[i] != buf2[i]) {
					throw new RuntimeException("desc error");
				}
			}
		} else {
			throw new RuntimeException("length not equal,desc error");
		}

		//System.out.println(ProtoConfig.PROTO_NATIVE_ENTITY_CODE_TEMPLATE);
	}
}
