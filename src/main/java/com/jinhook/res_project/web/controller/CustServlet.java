package com.jinhook.res_project.web.controller;

import com.jinhook.res_project.bean.CartItem;
import com.jinhook.res_project.bean.Resorder;
import com.jinhook.res_project.bean.Resuser;
import com.jinhook.res_project.utils.YcConstants;
import com.jinhook.res_project.web.service.OrderBiz;
import com.jinhook.res_project.web.model.ResponseResult;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@WebServlet("/custOp.action")
public class CustServlet extends BaseServlet{
    public ResponseResult confirmOrder(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session = req.getSession();
        //取购物车信息
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if(cart == null || cart.size() <= 0){
            return ResponseResult.error("购物车为空");
        }
        //为了取到用户信息的fid
        try {
            Resuser resuser = (Resuser) session.getAttribute(YcConstants.RESUSER);
            Resorder resorder = parseRequestParamToT(req, Resorder.class);
            Set<CartItem> cartItems = new HashSet<CartItem>(  cart.values()  );
            //调用业务层
            OrderBiz orderBiz = new OrderBiz();
            int result = orderBiz.order(resorder, resuser, cartItems);

            if(result == 0){
                return ResponseResult.error("下单失败");
            }
            session.removeAttribute("cart");    //下完单后，清空购物车。
            return ResponseResult.ok().setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            session.removeAttribute("cart");
            return ResponseResult.error(e.getMessage());
        }

    }
}
