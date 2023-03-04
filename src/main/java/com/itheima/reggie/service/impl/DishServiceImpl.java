package com.itheima.reggie.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
	@Autowired
	private DishFlavorService dishFlavorService;

	/**
	 * 新增菜品
	 * 
	 * @param dishDto
	 */
	@Transactional
	@Override
	public void saveWithFlavor(DishDto dishDto) {
		this.save(dishDto);
		Long id = dishDto.getId();
		List<DishFlavor> flavors = dishDto.getFlavors();
		flavors = flavors.stream().map((item) -> {
			item.setDishId(id);
			return item;
		}).collect(Collectors.toList());
		dishFlavorService.saveBatch(flavors);
	}

	/**
	 * 根据ID查询菜品信息
	 */
	@Override
	public DishDto getByIdWithFlavor(Long id) {
		Dish dish = this.getById(id);
		DishDto dishDto = new DishDto();
		BeanUtils.copyProperties(dish, dishDto);

		LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(DishFlavor::getDishId, dish.getId());

		List<DishFlavor> list = dishFlavorService.list(queryWrapper);
		dishDto.setFlavors(list);
		return dishDto;
	}

	/**
	 * 修改菜品
	 */
	@Override
	@Transactional
	public void updateWithFlavor(DishDto dishDto) {
		/*
		 * 虽然说这里是更新了dishDto，但是这个this是dishservice的对象，所以底层进行了向上转型，
		 * dishdto拓展了dish，所以向上转型成dish，然后再保存到数据库里面。
		 * 
		 * 虽然修改了菜品的dish部分，但是dish的口味表还没有修改，所以要加上口味表的操作部分
		 */
		this.updateById(dishDto);

		LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<DishFlavor>();
		queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
		// 移除原来的口味
		dishFlavorService.remove(queryWrapper);
		// 新增新的口味
		List<DishFlavor> flavors = dishDto.getFlavors();

//		flavors = flavors.stream().map((item) -> {
//			item.setDishId(dishDto.getId());
//			return item;
//		}).collect(Collectors.toList());

		dishFlavorService.saveBatch(flavors);
	}

}
