package com.jinhook.res_project.web.filter;

import com.jinhook.res_project.bean.Resuser;
import com.jinhook.res_project.dao.RedisHelper;
import com.jinhook.res_project.utils.YcConstants;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import redis.clients.jedis.Jedis;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/resuser.action")
public class DeviceFilter extends BaseFilter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        //看下用户是否为登陆操作，且登录成功的话就记录用户的浏览器喜好
        String op = servletRequest.getParameter("op");
        System.out.println("到这没");
        if("login".equals(op)){
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpSession session = req.getSession();
            Resuser resuser = (Resuser) session.getAttribute(YcConstants.RESUSER);
            if(resuser!= null){
                System.out.println("到这没");
                String uid = resuser.getUserid() + "";
                //取出浏览器设备信息
                Browser browser = UserAgent.parseUserAgentString(req.getHeader("User-Agent")).getBrowser();
                String browserName = browser.getName();

                //将用户的喜好信息存入redis中
                Jedis jedis = RedisHelper.getRedisInstance();
                //浏览器被哪个用户使用了
                jedis.sadd(browserName + YcConstants.REDIS_BROWSER_USER, uid);
                //用户使用了哪些浏览器
                jedis.lpush(uid + YcConstants.REDIS_USER_BROWSER, browserName);
            }
        }
    }
}
