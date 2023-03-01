package com.itheima.reggie.common;
/**
 * 基于ThreadLocal封装类，用于获取同一线程内的其他变量的数据
 * @author 24166
 *
 */
public class BaseContext {
	private static ThreadLocal<Long> threadLocal= new ThreadLocal<>();
	
	/**
	 * 设置值
	 * @param id
	 */
	public static void setCurrentId(Long id) {
		threadLocal.set(id);
	}
	
	/**
	 * 获取值
	 * @return
	 */
	public static Long getCurrentId(){
		return  threadLocal.get();
	}
}
