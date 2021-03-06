package com.feinno.runtime.bytecode.type;

/**
 * 常量池中的字段类型信息表示类
 * 
 * @author Lv.Mingwei
 * 
 */
public class ConstantInterfacerefInfo extends ConstantReference {

	public ConstantInterfacerefInfo(ClassFile classFile) {
		super(classFile, ConstantType.CONSTANT_INTERFACEMETHODREF);
	}

	public ConstantInterfacerefInfo(ClassFile classFile, int classIndex, int nameAndTypeIndex) {
		super(classFile, ConstantType.CONSTANT_INTERFACEMETHODREF, classIndex, nameAndTypeIndex);
	}

	protected ConstantType getConstantType() {
		return ConstantType.CONSTANT_INTERFACEMETHODREF;
	}
}
