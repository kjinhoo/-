package com.jinhook.res_project.web.controller;

import com.jinhook.res_project.bean.Resuser;
import com.jinhook.res_project.dao.DbHelper;
import com.jinhook.res_project.utils.EncryptUtils;
import com.jinhook.res_project.web.model.ResponseResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ResuserServlet", value ="/resuser.action")
public class ResuserServlet extends BaseServlet{

        public ResponseResult checkLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            HttpSession session = req.getSession();
            Object resuser = session.getAttribute("resuser");
            if(resuser != null){
                return ResponseResult.ok().setData(resuser);
            }
            return ResponseResult.error();

        }

        public ResponseResult logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            HttpSession session = req.getSession();
            session.removeAttribute("resuser");
            return ResponseResult.ok();
        }

        public ResponseResult login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Resuser resuser = parseRequestParamToT(req, Resuser.class);
            //用户输入的验证码
            String valcode = req.getParameter("valcode");
            //标准验证码，对比输入的是否正确
            HttpSession session = req.getSession();
            //做个开关，未上线前的联调时，postman发请求时，不经过界面发请求，所以没有验证码，要把这一段逻辑给跳过。
//            boolean validate = false;
//            if(validate){
                String code = session.getAttribute("code").toString();
                if( !code.equals(valcode)){
                    return ResponseResult.error("验证码错误");
                }
//            }
            //验证码过了，去数据库验证用户名和密码是否正确
            DbHelper db = new DbHelper();
            String sql = "select * from resuser where username=? and pwd=?";
            Resuser result = db.selectOne((rs, rownum) -> {
                Resuser r = new Resuser();
                r.setUserid(rs.getInt("userid"));
                r.setUsername(rs.getString("username"));
                r.setEmail(rs.getString("email"));
                return r;
            }, sql, resuser.getUsername(), EncryptUtils.encryptToMD5(resuser.getPwd()));

            if(result != null){
                session.setAttribute("resuser", result);
                return ResponseResult.ok().setData(result);
            }else{
                return ResponseResult.error("用户名或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
    }

}
