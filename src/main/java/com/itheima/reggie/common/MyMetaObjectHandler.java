package com.itheima.reggie.common;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

/**
 * mybatis-plus公共字段自动填充
 * 
 * @author 24166
 *
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		metaObject.setValue("createTime", LocalDateTime.now());
		metaObject.setValue("updateTime", LocalDateTime.now());
		metaObject.setValue("createUser", BaseContext.getCurrentId());
		metaObject.setValue("updateUser", BaseContext.getCurrentId());
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		metaObject.setValue("createTime", LocalDateTime.now());
		metaObject.setValue("updateTime", LocalDateTime.now());
		metaObject.setValue("createUser", BaseContext.getCurrentId());
		metaObject.setValue("updateUser", BaseContext.getCurrentId());
	}

}
