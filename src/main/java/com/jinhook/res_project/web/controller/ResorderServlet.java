package com.jinhook.res_project.web.controller;

import com.jinhook.res_project.bean.CartItem;
import com.jinhook.res_project.bean.Resfood;
import com.jinhook.res_project.dao.DbHelper;
import com.jinhook.res_project.web.model.ResponseResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/resorder.action")
public class ResorderServlet extends BaseServlet{

    public ResponseResult getCartInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if(cart != null){
            return ResponseResult.ok().setData(cart.values());
        }
        return ResponseResult.error("购物车为空");
    }

    public ResponseResult clearAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("cart");
        return ResponseResult.ok();
    }

        public ResponseResult order(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
//        if(session.getAttribute(YcConstants.RESUSER) == null){          //转移到rightfilter了
//            return ResponseResult.noLogin();
//        }
        int fid = Integer.parseInt(req.getParameter("fid"));
        int num = Integer.parseInt(req.getParameter("num"));

        DbHelper db = new DbHelper();
        Resfood food = db.selectOne((rs, rownum) -> {
            Resfood r = new Resfood();
            r.setFid(rs.getInt("fid"));
            r.setFname(rs.getString("fname"));
            r.setNormprice(rs.getDouble("normprice"));
            r.setRealprice(rs.getDouble("realprice"));
            r.setDetail(rs.getString("detail"));
            r.setFphoto(rs.getString("fphoto"));
            return r;
        }, "select * from resfood where fid=?", fid);

        if(food == null){
            return ResponseResult.error("菜品不存在");
        }
        //接下来是菜品存在的逻辑
        Map<Integer, CartItem> cart = new HashMap<>();
        if(session.getAttribute("cart") != null){
            cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        }else{
            session.setAttribute("cart", cart);
        }
        CartItem ci = null;
        if(cart.containsKey(fid)){
            ci = cart.get(fid);
            ci.setNum(ci.getNum() + num);
        }else{
            ci = new CartItem();
            ci.setFood(food);
            ci.setNum(num);
        }
        //可能传入的num是负数，即少买了，此时需要判断是否需要移除
        if(ci.getNum() <= 0){
            cart.remove(fid);
        }else{
            ci.getSmallCount();
            //ci改过了，没放回去
            cart.put(fid, ci);
        }
        //session里的cart需要重新放回去，更新
        session.setAttribute("cart", cart);
        //最后需要返回给前端做渲染
        return ResponseResult.ok().setData(cart.values());
    }
}
