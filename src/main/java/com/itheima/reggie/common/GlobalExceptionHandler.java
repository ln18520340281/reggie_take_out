package com.itheima.reggie.common;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理
 * 
 * @author 24166
 *
 */
//AOP
@ControllerAdvice(annotations = { RestController.class, Controller.class })
@Slf4j
//用于返回json数据
@ResponseBody
public class GlobalExceptionHandler {

	/**
	 * 异常处理方法
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public R<String> Exception_handler(SQLIntegrityConstraintViolationException exception) {
		log.error(exception.getMessage());

		if (exception.getMessage().contains("Duplicate entry")) {
			String[] s = exception.getMessage().split(" ");
			String msg = s[2] + "已存在";
			return R.error(msg);
		}

		return R.error("未知错误");
	}

	@ExceptionHandler(CustomException.class)
	public R<String> CustomException_handler(CustomException exception) {
		log.error(exception.getMessage());
		return R.error(exception.getMessage());
	}
}
