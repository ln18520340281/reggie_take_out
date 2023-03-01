package com.itheima.reggie.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;

import lombok.extern.slf4j.Slf4j;
/**
 * 瑞吉外卖
 * spring boot和过滤器结合
 * @author 24166
 *
 */
@WebFilter(filterName = "logincheckfilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

	public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		String requestURL = httpServletRequest.getRequestURI();

		log.info("拦截到请求：{}", requestURL);
		String[] urls = new String[] { "/employee/login", "/employee/logout", "/backend/**", "/front/**","common/**" };

		boolean check = check(urls, requestURL);

		if (check) {
			//放行
			chain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}

		if (httpServletRequest.getSession().getAttribute("employee") != null) {
			Long employeeId = (Long) httpServletRequest.getSession().getAttribute("employee");
			BaseContext.setCurrentId(employeeId);
			
			chain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}
		
		log.info("用户未登录");
		httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
		return;
	}

	/**
	 * 检查是否在不处理清单内
	 * @param urls	不处理的请求路径集合
	 * @param requestURI	本次请求路径的集合
	 * @return	判断本次请求是否需要处理，需要返回true不需要返回false
	 */
	public boolean check(String[] urls, String requestURI) {
		for (String url : urls) {
			boolean match = PATH_MATCHER.match(url, requestURI);
			if (match)
				return true;
		}
		return false;
	}

}
