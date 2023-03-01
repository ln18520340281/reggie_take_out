package com.itheima.reggie.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 员工登录
	 * 
	 * @param request
	 * @param employee
	 * @return
	 */
	@PostMapping("login")
	public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

		//包装查询条件
		LambdaQueryWrapper<Employee> warrper = new LambdaQueryWrapper<Employee>();
		warrper.eq(Employee::getUsername, employee.getUsername());

		Employee one = employeeService.getOne(warrper);

		//判断是否账号是否合法
		if (one == null)
			return R.error("登录失败");
		if (!one.getPassword().equals(md5DigestAsHex))
			return R.error("登录失败");
		if (one.getStatus() == 0)
			return R.error("账号已禁用");

		//合法就把账号ID放到session里
		request.getSession().setAttribute("employee", one.getId());
		
		//然后把合法账号信息回传给前端
		return R.success(one);
	}

	/**
	 * 员工退出
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/logout")
	public R<String> logout(HttpServletRequest request) {
		request.getSession().removeAttribute("employee");
		return R.success("退出成功");
	}

	/**
	 * 新增员工
	 * 
	 * @param employee
	 * @return
	 */
	@PostMapping
	public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
		log.info("新增员工，该员工信息{}", employee.toString());
		employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
		employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
		employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
		employeeService.save(employee);
		return R.success("新增员工成功");
	}

	/**
	 * 员工信息分页查询
	 * 
	 * @param page     查询页码
	 * @param pagesize 一页有多少条数据
	 * @param name     查询员工名字
	 * @return
	 */
	@GetMapping("/page")
	public R<Page<Employee>> pageSearch(Integer page, Integer pageSize, String name) {
//		log.info("page={},pagesize={},name={}", page, pageSize, name);

		LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
		queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
		queryWrapper.orderByDesc(Employee::getUpdateTime);

		Page<Employee> pageinfo = new Page<>(page, pageSize);
		employeeService.page(pageinfo, queryWrapper);
		return R.success(pageinfo);
	}

	/**
	 * 根据员工id修改员工信息
	 * 
	 * @param employee
	 * @return
	 */
	@PutMapping
	public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
		employeeService.updateById(employee);
		return R.success("员工修改成功");
	}

	/**
	 * 根据id查询员工信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public R<Employee> getByID(@PathVariable Long id) {
		Employee employee = employeeService.getById(id);
		if (employee != null)
			return R.success(employee);
		else
			return R.error("没有查询到员工的相关信息");
	}

}
