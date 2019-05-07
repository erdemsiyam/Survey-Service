package com.tpaWorkers.SurveyService.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(UserInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("Request 'Client IP : "+request.getRemoteAddr()+"','URI : "+""+request.getRequestURI()+"'");
        return super.preHandle(request, response, handler);
    }
}
