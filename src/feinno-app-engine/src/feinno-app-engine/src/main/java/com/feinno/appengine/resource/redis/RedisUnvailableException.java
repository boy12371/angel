/**
 * 
 */
package com.feinno.appengine.resource.redis;


/**
 * @author chenchunsong
 *
 */
public class RedisUnvailableException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 1603800116125176208L;

    /**
     * @param message
     * @param cause
     */
    public RedisUnvailableException(String message , Throwable cause) {
        super(message , cause);
        // TODO Auto-generated constructor stub
    }

}
