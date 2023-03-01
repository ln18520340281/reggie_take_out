package com.itheima.reggie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;

@RestController
@RequestMapping("/dish")
public class DishController {
	@Autowired
	private DishService dishService;
//	@Autowired
//	private DishFlavorService dishFlavorService;

	@GetMapping("/page")
	public R<Page<Dish>> pageSearch(Integer page, Integer pageSize) {
		// log.info("page={},pagesize={},name={}", page, pageSize, name);

		LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>();
		queryWrapper.orderByDesc(Dish::getSort);

		Page<Dish> pageinfo = new Page<>(page, pageSize);
		dishService.page(pageinfo, queryWrapper);
		return R.success(pageinfo);
	}
	
	/**
	 * 新增菜品
	 * 
	 * @param dishDto
	 * @return
	 */
	@PostMapping
	public R<String> save(@RequestBody DishDto dishDto) {
		dishService.saveWithFlavor(dishDto);
		return R.success("新增菜品成功");
	}

}
