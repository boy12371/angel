/*
 * FAE, Feinno App Engine
 *  
 * Create by yanghu 2012-08-09
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */

package com.feinno.serialization.protobuf.descriptor;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * EnumValueDescriptorProto
 * 
 * @author yanghu
 */

public class EnumValueDescriptorProto extends ProtoEntity {

    @ProtoMember(1)
    private String name;

    public String getName() {
      return name;
    }

    public EnumValueDescriptorProto setName(String value) {
      name = value;
      return this;
    }

    @ProtoMember(2)
    private int number;

    public int getNumber() {
      return number;
    }

    public EnumValueDescriptorProto setNumber(int value) {
      number = value;
      return this;
    }

}
