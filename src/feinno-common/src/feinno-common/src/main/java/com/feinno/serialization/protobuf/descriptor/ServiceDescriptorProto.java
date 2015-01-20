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
 * ServiceDescriptorProto
 * 
 * @author yanghu
 */

public class ServiceDescriptorProto extends ProtoEntity {

    @ProtoMember(1)
    private String name;

    public String getName() {
      return name;
    }

    public ServiceDescriptorProto setName(String value) {
      name = value;
      return this;
    }

    @ProtoMember(2)
    private List<MethodDescriptorProto> method;

    public List<MethodDescriptorProto> getMethod() {
      return method;
    }

    public ServiceDescriptorProto setMethod(List<MethodDescriptorProto> value) {
      method = value;
      return this;
    }

}
