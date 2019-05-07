package com.tpaWorkers.SurveyService.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AdminInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(AdminInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("Request 'Admin IP : "+request.getRemoteAddr()+"','URI : "+""+request.getRequestURI()+"'");
        return super.preHandle(request, response, handler);
    }
}
//geri dönüş verilerini log