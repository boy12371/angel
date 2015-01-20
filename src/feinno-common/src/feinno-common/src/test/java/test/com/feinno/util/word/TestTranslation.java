package test.com.feinno.util.word;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.util.word.Translation;
import com.feinno.util.word.TranslationWordEnum;
import com.feinno.util.word.TranslationWordInfo;

public class TestTranslation {
	@Test
	public void TestTranslation() {
		TranslationWordInfo test = new TranslationWordInfo();
		Assert.assertNotNull(Translation.trans.translate(
				TranslationWordInfo.BIG5_WORD, TranslationWordEnum.GBKTOBIG5));
		Assert.assertNotNull(Translation.trans.translate(
				TranslationWordInfo.GBK_WORD, TranslationWordEnum.BIG5TOGBK));
		TranslationWordEnum testEnum = TranslationWordEnum.BIG5TOGBK;
		testEnum.setCode(1);
	}
}
