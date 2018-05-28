package com.gibraltar.filter;


import com.gibraltar.service.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*",initParams={@WebInitParam(name ="EXCLUDED_PAGES" , value = "/login;/register")})
public class SSOFilter implements Filter {

    private static final Logger logger= LoggerFactory.getLogger(SSOFilter.class);


    @Resource
    AuthService authService;

    private String excludedPages;
    private String[] excludedPageArray;


    public void init(FilterConfig filterConfig) throws ServletException {
        excludedPages = filterConfig.getInitParameter("EXCLUDED_PAGES");
        if (null!=excludedPages && excludedPages.length()!=0) { // 例外页面不为空
            excludedPageArray = excludedPages.split(String.valueOf(';'));
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("now is in the filter");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp=(HttpServletResponse) servletResponse;

        boolean isExcludedPage = false;

        for (String page : excludedPageArray) {// 遍历例外url数组
            // 判断当前URL是否与例外页面相同
            if(req.getServletPath().equals(page)){ // 从第2个字符开始取（把前面的/去掉）
                isExcludedPage = true;
                break;
            }
        }

        if (isExcludedPage) {//login reg
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else {
            //执行验证

            if(authService.auth(req)){
                filterChain.doFilter(servletRequest, servletResponse);
            }else {
                logger.info("not authed");
                resp.sendRedirect(req.getContextPath() + "/ui/login");
            }
        }
    }

    public void destroy() {
        this.excludedPages = null;
        this.excludedPageArray = null;
    }
}
