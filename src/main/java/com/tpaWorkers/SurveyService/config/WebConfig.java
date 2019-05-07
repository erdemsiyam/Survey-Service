package com.tpaWorkers.SurveyService.config;

import com.tpaWorkers.SurveyService.interceptor.AdminInterceptor;
import com.tpaWorkers.SurveyService.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.tpaWorkers.SurveyService"})
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setContentType("text/html;charset=UTF-8");
        return viewResolver;
    }
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        List<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(new MediaType("text", "plain", Charset.forName("UTF-8")));
        mediaTypeList.add(new MediaType("text", "html", Charset.forName("UTF-8")));
        mediaTypeList.add(new MediaType("application", "json", Charset.forName("UTF-8")));
        stringConverter.setSupportedMediaTypes(mediaTypeList);
        converters.add(stringConverter);// varsa diger converter tanimlari ...}
    }
    /////
    @Bean
    public UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }
    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new RequestLoggingInterceptor()).addPathPatterns("/*");
        registry.addInterceptor(userInterceptor()).addPathPatterns(new String[] { "/api/user/*"});
        registry.addInterceptor(adminInterceptor()).addPathPatterns(new String[] { "/api/admin/*"});
    }
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) { // sunum 6 : Özel Olarak Karşılama Yapılmayan İsteklerin Servlet Taşıyıcısına Devri
        configurer.enable();
    }
}

