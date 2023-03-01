package com.itheima.reggie.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

	@Autowired
	private DishService dishService;
	@Autowired
	private SetmealService setmealService;

	@Override
	public void remove(Long id) {
		LambdaQueryWrapper<Dish> dishqueryWrapper = new LambdaQueryWrapper<>();
		dishqueryWrapper.eq(Dish::getCategoryId, id);
		// 统计条数
		int count1 = dishService.count(dishqueryWrapper);

		if (count1 > 0) {
			throw new CustomException("当前分类下已关联菜品，不能删除");
		}

		LambdaQueryWrapper<Setmeal> setmealqueryWrapper = new LambdaQueryWrapper<>();
		setmealqueryWrapper.eq(Setmeal::getCategoryId, id);
		int count2 = setmealService.count(setmealqueryWrapper);
		if (count2 > 0) {
			throw new CustomException("当前分类下已关联套餐，不能删除");
		}
		super.removeById(id);
	}

}
