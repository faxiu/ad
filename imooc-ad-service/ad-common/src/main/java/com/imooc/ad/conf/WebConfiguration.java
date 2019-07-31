package com.imooc.ad.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Author: hekai
 * @Date: 2019-07-03 17:42
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * 配置消息转换器
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.clear();
        //实现将java对象转化成json对象
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    /**
     * 匹配路由请求规则
     */
//    default void configurePathMatch(PathMatchConfigurer configurer) {
//    }

    /**
     * 注册自定义的 Formatter 和 Convert
     */
//    default void addFormatters(FormatterRegistry registry) {
//    }

    /**
     * 添加静态资源处理器
     */
//    default void addResourceHandlers(ResourceHandlerRegistry registry) {
//    }

    /**
     * 添加自定义视图控制器
     */
//    default void addViewControllers(ViewControllerRegistry registry) {
//    }

    /**
     * 添加自定义方法参数处理器
     */
//    default void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//    }
}
