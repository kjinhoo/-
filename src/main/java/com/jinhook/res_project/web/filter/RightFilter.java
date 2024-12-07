package com.jinhook.res_project.web.filter;

import com.jinhook.res_project.utils.YcConstants;
import com.jinhook.res_project.web.model.ResponseResult;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(value = {"/resorder.action","/custOp.action","/resfood.action"})
public class RightFilter extends BaseFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        String op = request.getParameter("op");
        if(session.getAttribute(YcConstants.RESUSER)!=null){
            filterChain.doFilter(servletRequest, servletResponse);
        }else if("findFoodsByPages".equals(op) || "findAllFoods".equals(op)){
            //ResfoodServlet中有两个方法不需要登录
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            ResponseResult result = ResponseResult.noLogin();
            writeJson(result, (HttpServletResponse) servletResponse);
        }
    }
}
