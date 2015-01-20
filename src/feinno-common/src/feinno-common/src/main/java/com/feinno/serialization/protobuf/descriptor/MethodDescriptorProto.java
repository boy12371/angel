/*
 * FAE, Feinno App Engine
 *  
 * Create by yanghu 2012-08-10
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */

package com.feinno.serialization.protobuf.descriptor;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * MethodDescriptorProto
 * 
 * @author yanghu
 */

public class MethodDescriptorProto extends ProtoEntity {

    @ProtoMember(1)
    private String name;

    public String getName() {
      return name;
    }

    public MethodDescriptorProto setName(String value) {
      this.name = value;
      return this;
    }

    @ProtoMember(2)
    private String input_type;

    public String getInput_type() {
      return input_type;
    }

    public MethodDescriptorProto setInput_type(String value) {
      this.input_type = value;
      return this;
    }

    @ProtoMember(3)
    private String output_type;

    public String getOutput_type() {
      return output_type;
    }

    public MethodDescriptorProto setOutput_type(String value) {
      this.output_type = value;
      return this;
    }

}
