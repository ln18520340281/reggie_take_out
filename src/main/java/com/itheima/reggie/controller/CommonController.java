package com.itheima.reggie.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itheima.reggie.common.R;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件上传通用controller
 * 
 * @author 24166
 *
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

	// 读取application.yml中的配置
	@Value("${reggie.path}")
	private String basePath;

	/**
	 * 上传文件到服务器
	 * @param 前端传入的MultipartFile文件对象
	 * @return
	 */
	@PostMapping("/upload")
	public R<String> upload(MultipartFile file) {
		//判断目录是否存在不存在则创建
		File dir = new File(basePath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		
		
		// 原始文件名
		String originalFilename = file.getOriginalFilename();
		// 取文件后缀
		String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
		// 生成uuid再拼接文件后缀
		String filename = UUID.randomUUID().toString() + substring;
		
		//文件转储
		try {
			file.transferTo(new File(basePath + filename));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return R.success(filename);
	}
}
