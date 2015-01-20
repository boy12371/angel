/*
 * FAE, Feinno App Engine
 *  
 * Create by yanghu 2012-08-09
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */

package com.feinno.serialization.protobuf.descriptor;

import java.util.List;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * EnumDescriptorProto
 * 
 * @author yanghu
 */

public class EnumDescriptorProto extends ProtoEntity {

    @ProtoMember(1)
    private String name;

    public String getName() {
      return name;
    }

    public EnumDescriptorProto setName(String value) {
      name = value;
      return this;
    }

    @ProtoMember(2)
    private List<EnumValueDescriptorProto> value;

    public List<EnumValueDescriptorProto> getValue() {
      return value;
    }

    public EnumDescriptorProto setValue(List<EnumValueDescriptorProto> value) {
      this.value = value;
      return this;
    }

}
