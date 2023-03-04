package com.itheima.reggie.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
	@Autowired
	private SetmealService setmealService;
	@Autowired
	private SetmealDishService setmealDishService;
	@Autowired
	private CategoryService categoryService;

	/**
	 * 新增套餐
	 * 
	 * @param setmealDto
	 * @return
	 */
	@PostMapping
	public R<String> save(@RequestBody SetmealDto setmealDto) {
		setmealService.saveWithDish(setmealDto);
		return R.success("新增成功");
	}

	/**
	 * 套餐信息分页查询
	 * 
	 * @param page
	 * @param pageSize
	 * @param name
	 * @return
	 */
	@GetMapping("/page")
	public R<Page> page(Integer page, Integer pageSize, String name) {
		LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.like(name != null, Setmeal::getName, name);
		lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

		Page<Setmeal> setmealPage = new Page<>(page, pageSize);
		Page<SetmealDto> setmealdtoPage = new Page<>();
		setmealService.page(setmealPage, lambdaQueryWrapper);
		BeanUtils.copyProperties(setmealPage, setmealdtoPage, "records");

		List<Setmeal> records = setmealPage.getRecords();
		List<SetmealDto> list = records.stream().map(item -> {
			SetmealDto setmealDto = new SetmealDto();
			BeanUtils.copyProperties(item, setmealDto);

			Long categoryId = item.getCategoryId();
			Category category = categoryService.getById(categoryId);
			if (category != null) {
				String categoryName = category.getName();
				setmealDto.setCategoryName(categoryName);
			}
			return setmealDto;
		}).collect(Collectors.toList());
		setmealdtoPage.setRecords(list);
		return R.success(setmealdtoPage);
	}

	/**
	 * 删除套餐
	 * @param ids
	 * @return
	 */
	@DeleteMapping
	public R<String> delete(@RequestParam List<Long> ids) {
		setmealService.removeDish(ids);
		return R.success("删除成功");
	}
}
