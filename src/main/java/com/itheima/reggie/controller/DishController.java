package com.itheima.reggie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itheima.reggie.common.R;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;

@RestController
@RequestMapping("/dish")
public class DishController {
	@Autowired
	private DishService dishService;
	@Autowired
	private DishFlavorService dishFlavorService;
	
	public R<String> hh(){
		return null;
	}
	
}
