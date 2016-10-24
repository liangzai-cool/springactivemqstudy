package org.xueliang.springactivemqstudy.commons;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * 基于注解的/WEB-INF/web.xml
 * 依赖 servlet 3.0
 * @author XueLiang
 * @date 2016年10月24日 下午5:58:45
 * @version 1.0
 */
public class CommonInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		// 基于注解配置的Web容器上下文
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(WebAppConfig.class);
		
		// 添加一个监听器，并初始化上下文
		ContextLoaderListener contextLoaderListener = new ContextLoaderListener(context);
		servletContext.addListener(contextLoaderListener);
		
		// 添加一个filter并进行映射
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setForceEncoding(true);
		characterEncodingFilter.setEncoding("UTF-8");
		FilterRegistration.Dynamic dynamicFilter = servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
		dynamicFilter.addMappingForUrlPatterns(null, false, "/");
	}

}
