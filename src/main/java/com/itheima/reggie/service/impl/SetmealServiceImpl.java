package com.itheima.reggie.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;

@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
	@Autowired
	private SetmealDishService setmealDishService;

	@Override
	public void saveWithDish(SetmealDto setmealDto) {
		this.save(setmealDto);
		List<SetmealDish> list = setmealDto.getSetmealDishes();
		list.stream().map((item) -> {
			item.setSetmealId(setmealDto.getId());
			return item;
		}).collect(Collectors.toList());
		setmealDishService.saveBatch(list);
	}

	@Override
	public void removeDish(List<Long> ids) {
		LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
		wrapper.in(Setmeal::getId, ids);
		wrapper.eq(Setmeal::getStatus,1);
		int count = this.count(wrapper);
		if(count>0) {
			throw new CustomException("套餐正在售卖中，不能删除");
		}
		this.removeByIds(ids);
		
		LambdaQueryWrapper<SetmealDish> wrapper2 = new LambdaQueryWrapper<>();
		wrapper2.in(SetmealDish::getSetmealId,ids);
		setmealDishService.remove(wrapper2);
	}

}
