package com.itheima.reggie.common;

/**
 * 自定义业务异常
 * 
 * @author 24166
 *
 */
public class CustomException extends RuntimeException {
	/**
	 * 生成序列化ID
	 */
	private static final long serialVersionUID = 1692201139310590555L;

	public CustomException(String message) {
		super(message);
	}
}
