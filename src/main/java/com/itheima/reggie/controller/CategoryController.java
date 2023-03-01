package com.itheima.reggie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	/**
	 * 分页查询菜品类型
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@GetMapping("/page")
	public R<Page<Category>> pageSearch(Integer page, Integer pageSize) {
		// log.info("page={},pagesize={},name={}", page, pageSize, name);

		LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>();
		queryWrapper.orderByDesc(Category::getSort);

		Page<Category> pageinfo = new Page<>(page, pageSize);
		categoryService.page(pageinfo, queryWrapper);
		return R.success(pageinfo);
	}

	/**
	 * 添加菜品
	 * 
	 * @param category
	 * @return
	 */
	@PostMapping
	public R<String> save(@RequestBody Category category) {
//		log.info(category.toString());
		categoryService.save(category);
		return R.success("新增分类成功");
	}

	/**
	 * 删除菜品
	 * 
	 * @param ids
	 * @return
	 */
	@DeleteMapping
	public R<String> delete(Long ids) {
		categoryService.remove(ids);
		return R.success("删除成功");
	}

	/**
	 * 修改菜品
	 * 
	 * @param category
	 * @return
	 */
	@PutMapping
	public R<String> update(@RequestBody Category category) {
		categoryService.updateById(category);
		return R.success("修改成功");
	}

	/**
	 * 根据条件查询分类数据
	 * 
	 * @param category
	 * @return
	 */
	@GetMapping("/list")
	public R<List<Category>> list(Category category) {
		LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(category.getType() != null, Category::getType, category.getType());

		queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);

		List<Category> list = categoryService.list(queryWrapper);
		return R.success(list);
	}
}
