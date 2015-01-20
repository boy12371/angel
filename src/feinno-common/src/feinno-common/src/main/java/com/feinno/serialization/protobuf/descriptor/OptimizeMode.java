/*
 * FAE, Feinno App Engine
 *  
 * Create by  
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.serialization.protobuf.descriptor;

import com.feinno.util.EnumInteger;

/**
 * OptimizeMode
 * 
 * @author  
 */
public enum OptimizeMode implements EnumInteger
{
  SPEED(1),
  CODE_SIZE(2),
  LITE_RUNTIME(3),
  ;
  
  private int value;
  
  OptimizeMode(int value)
  {
    this.value = value;
  }
  
  @Override
  public int intValue()
  {
    return value;
  }
}
