package com.itheima.reggie.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;

@RestController
@RequestMapping("/dish")
public class DishController {
	@Autowired
	private DishService dishService;
//	@Autowired
//	private DishFlavorService dishFlavorService;
	@Autowired
	private CategoryService categoryService;

	/**
	 * 分页
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/page")
	public R<Page<DishDto>> pageSearch(Integer page, Integer pageSize, String name) {
		// log.info("page={},pagesize={},name={}", page, pageSize, name);

		LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<Dish>();
		queryWrapper.like(name != null, Dish::getName, name);
		queryWrapper.orderByDesc(Dish::getUpdateTime);

		Page<Dish> pageinfo = new Page<>(page, pageSize);
		Page<DishDto> pageinfo2 = new Page<>(page, pageSize);
		dishService.page(pageinfo, queryWrapper);

		BeanUtils.copyProperties(pageinfo, pageinfo2, "records");
		List<Dish> records = pageinfo.getRecords();
		List<DishDto> list = records.stream().map((item) -> {
			DishDto dishDto = new DishDto();
			BeanUtils.copyProperties(item, dishDto);
			Long categoryId = item.getCategoryId();
			Category category = categoryService.getById(categoryId);
			if (category != null) {
				String categoryName = category.getName();
				dishDto.setCategoryName(categoryName);
			}
			return dishDto;
		}).collect(Collectors.toList());

		pageinfo2.setRecords(list);
		return R.success(pageinfo2);
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

	/**
	 * 修改菜品
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public R<DishDto> update(@PathVariable Long id) {
		DishDto dishDto = dishService.getByIdWithFlavor(id);
		return R.success(dishDto);
	}

}
