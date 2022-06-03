package com.boyalong.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.boyalong.reggie.common.BaseContext;
import com.boyalong.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登陆
 * filterName过滤器名字
 * urlPatterns拦截的请求，这里是拦截所有的请求
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器吗，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 路径匹配，检测是否放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        1、获取本次请求的URI
        String requestURI = request.getRequestURI();

        log.info("拦截请求：{}",requestURI);

        //不需要拦截的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

//        2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

//        3、如果不需要处理，则直接放行
        if(check){
//            log.info("本次请求不需要处理：{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

//        4.1、判断employee登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){
//            log.info("员工已登录：用户ID为：{}",request.getSession().getAttribute("employee"));

            Long empId =(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        //        4.2、判断user登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
//            log.info("用户已登录：用户ID为：{}",request.getSession().getAttribute("user"));

            Long userId =(Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }


//        log.info("用户未登录");
//        5、如果未登录则返回未登录结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
}
