package test.com.feinno.util.word;

import org.junit.Test;

import test.com.feinno.serialization.protobuf.DataCreater;

import com.feinno.diagnostic.dumper.Dumpable;
import com.feinno.diagnostic.dumper.ObjectDumper;
import com.feinno.initialization.InitialLock;
import com.feinno.util.word.TranslationWordEnum;
import com.feinno.util.word.TranslationWordInfo;

public class TestWord {

	@Test
	public void test() {
		new TranslationWordInfo();
		System.out.println(TranslationWordInfo.BIG5_WORD);
		System.out.println(TranslationWordInfo.GBK_WORD);
		System.out.println(TranslationWordEnum.BIG5TOGBK);
		System.out.println(TranslationWordEnum.GBKTOBIG5);
		TranslationWordEnum.BIG5TOGBK.setCode(TranslationWordEnum.BIG5TOGBK.getCode());
		new ObjectDumper();
		ObjectDumper.dumpString(null);
		ObjectDumper.dumpString(new Dumpable() {
			@Override
			public String dumpContent() {
				return null;
			}
		});
		ObjectDumper.dumpString((DataCreater.newFullElementsBean(true)));
		ObjectDumper.dumpString((DataCreater.newFullElementsBean(true)), "Test");

		try {

			ObjectDumper.dumpString(new Dumpable() {
				@Override
				public String dumpContent() {
					throw new RuntimeException();
				}
			});
		} catch (RuntimeException e) {
		}
		
		InitialLock initialLock = new InitialLock();
		initialLock.doInit(new Runnable(){
			@Override
			public void run() {
				
			}
		});
	}
}
