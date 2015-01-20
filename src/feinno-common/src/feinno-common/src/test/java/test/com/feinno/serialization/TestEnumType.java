/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-11-22
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization;

import java.io.IOException;

import org.junit.Test;

import test.com.feinno.serialization.entity.EnumTypeByte;
import test.com.feinno.serialization.entity.EnumTypeChar;
import test.com.feinno.serialization.entity.EnumTypeClass;
import test.com.feinno.serialization.entity.EnumTypeInt;
import test.com.feinno.serialization.entity.EnumTypeLong;
import test.com.feinno.serialization.entity.EnumTypeShort;
import test.com.feinno.serialization.entity.EnumTypeString;

import com.feinno.serialization.Serializer;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
public class TestEnumType {

	@Test 
	public void test() {
		EnumTypeInt i = EnumTypeInt.valueOf(1);
		EnumTypeLong l = EnumTypeLong.valueOf(2l);
		EnumTypeShort s = EnumTypeShort.valueOf((short)3);
		
		EnumTypeChar c = EnumTypeChar.valueOf((char)1);
		EnumTypeString str = EnumTypeString.valueOf("2");
		EnumTypeByte b = EnumTypeByte.valueOf((byte)3);
		EnumTypeClass a = new EnumTypeClass();
		a.setEnumInt(i);
		a.setEnumByte(b);
		a.setEnumChar(c);
		a.setEnumLong(l);
		a.setEnumShort(s);
		a.setEnumString(str);
		
		try {
			byte[] by = Serializer.encode(a);
			System.out.println("Entity = " + by);

			EnumTypeClass ee = Serializer.decode(EnumTypeClass.class, by);
			System.out.println(ee.getEnumChar().key());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
