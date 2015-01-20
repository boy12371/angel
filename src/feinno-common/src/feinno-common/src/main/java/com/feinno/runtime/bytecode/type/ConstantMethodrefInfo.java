package com.feinno.runtime.bytecode.type;

/**
 * 常量池中的字段类型信息表示类
 * 
 * @author Lv.Mingwei
 * 
 */
public class ConstantMethodrefInfo extends ConstantReference {

	public ConstantMethodrefInfo(ClassFile classFile) {
		super(classFile, ConstantType.CONSTANT_METHODREF);
	}

	public ConstantMethodrefInfo(ClassFile classFile, int classIndex, int nameAndTypeIndex) {
		super(classFile, ConstantType.CONSTANT_METHODREF, classIndex, nameAndTypeIndex);
	}

	protected ConstantType getConstantType() {
		return ConstantType.CONSTANT_METHODREF;
	}
}
