package com.feinno.maven.code.json;

import java.io.StringReader;
import java.io.StringWriter;

/**
 * <b>描述: 格式化json代码</b>
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Zhou.yan
 * 
 */
public class SimpleFormatter {
	/**
	 * 格式化json代码，其中包括换行和制表符
	 * 
	 * @param code 需要转换的json字符串
	 * @return 返回格式化后的字符串
	 * @throws Exception
	 */
	public String format(String code) throws Exception {
		StringWriter writer = new StringWriter();
		StringReader reader = new StringReader(code);
		int c;
		int tabsize = 0;
		while ((c = reader.read()) != -1) {
			if (c == ',' || c == ';') {
				// 换行处理，并且打印制表符
				writer.write(c);
				writer.write("\n");
				for (int i = 0; i < tabsize; i++) {
					writer.write("\t");
				}
			} else if (c == '{' || c == '[') {
				// 换行，并且增加缩进，使制表符计数加1
				writer.write(c);
				writer.write("\n");
				tabsize++;
				for (int i = 0; i < tabsize; i++) {
					writer.write("\t");
				}
			} else if (c == '}' || c == ']') {
				// 换行，减少缩进，制表符计数减1
				writer.write("\n");
				tabsize--;
				for (int i = 0; i < tabsize; i++) {
					writer.write("\t");
				}
				writer.write(c);
			} else {
				writer.write(c);
			}
		}
		return writer.toString();
	}
}
