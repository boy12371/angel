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
 * .proto file self descriptor
 * 
 * use protoc to generate FileDescriptorSet PB Data
 * 
 * * @author yanghu
 */

public class FileDescriptorSet extends ProtoEntity {

    @ProtoMember(1)
    private List<FileDescriptorProto> file;

    public List<FileDescriptorProto> getFile() {
      return file;
    }

    public FileDescriptorSet setFile(List<FileDescriptorProto> value) {
      file = value;
      return this;
    }

}
