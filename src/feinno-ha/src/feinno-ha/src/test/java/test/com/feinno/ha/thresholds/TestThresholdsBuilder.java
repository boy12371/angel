package test.com.feinno.ha.thresholds;

import java.util.HashMap;
import java.util.Iterator;

import org.antlr.runtime.RecognitionException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.feinno.configuration.TestConfigurationManager;

import com.feinno.ha.util.thresholds.ParserThresholdsException;
import com.feinno.ha.util.thresholds.ThresholdExpression;
import com.feinno.ha.util.thresholds.ThresholdsBuilder;

public class TestThresholdsBuilder {

	@Test
	public void testThreshold() {
		try {
			HashMap<String, ThresholdExpression> map = ThresholdsBuilder
					.parse("( \"tx(/asdfas3434dfassec.)\" > 11.23223);( \"tx(/asdfasdfassec.)\" > 22222);");
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				ThresholdExpression te = map.get(key);
				LOGGER.info(te.toString());
			}
		} catch (RecognitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserThresholdsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Logger LOGGER = LoggerFactory
			.getLogger(TestThresholdsBuilder.class);

}
