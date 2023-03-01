package com.itheima.reggie.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itheima.reggie.common.R;

/**
 * 文件上传通用controller
 * 
 * @author 24166
 *
 */
@RestController
@RequestMapping("/common")
public class CommonController {

	// 读取application.yml中的配置
	@Value("${reggie.path}")
	private String basePath;

	/**
	 * 上传文件到服务器
	 * 
	 * @param 前端传入的MultipartFile文件对象
	 * @return
	 */
	@PostMapping("/upload")
	public R<String> upload(MultipartFile file) {
		// 判断目录是否存在不存在则创建
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

		// 文件转储
		try {
			file.transferTo(new File(basePath + filename));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return R.success(filename);
	}

	/**
	 * 下载文件到浏览器缓存中
	 * 
	 * @param name
	 * @param response
	 */
	@GetMapping("/download")
	public void download(String name, HttpServletResponse response) {
		try {
			FileInputStream inputstream = new FileInputStream(new File(basePath + name));
			ServletOutputStream outputStream = response.getOutputStream();

			response.setContentType("image/jpeg");

			int len = 0;
			byte[] bytes = new byte[1024];

			while ((len = inputstream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, len);
				outputStream.flush();
			}

			outputStream.close();
			inputstream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
