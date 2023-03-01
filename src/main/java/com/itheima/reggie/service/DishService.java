package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

//泛型中的是实体类
public interface DishService extends IService<Dish>{
	public void saveWithFlavor(DishDto dishDto);
}
