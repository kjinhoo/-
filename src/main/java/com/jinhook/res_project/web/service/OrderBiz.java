package com.jinhook.res_project.web.service;

import com.jinhook.res_project.bean.CartItem;
import com.jinhook.res_project.bean.Resorder;
import com.jinhook.res_project.bean.Resuser;
import com.jinhook.res_project.dao.DbHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class OrderBiz {

    public int order(Resorder resorder, Resuser resuser, Set<CartItem> cartItems) throws SQLException, IOException {
        int result = 1;
        int roid=0;
        DbHelper db = new DbHelper();
        String sql = "insert into resorder(userid, address, tel, ordertime, deliverytime, ps, status) values(?,?,?,?,now(),?,0)";
        Connection con=null;
        try {
            con = DbHelper.getConnection();
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, resuser.getUserid());
            pstmt.setString(2, resorder.getAddress());
            pstmt.setString(3, resorder.getTel());
            pstmt.setString(4, resorder.getDeliverytime());
            pstmt.setString(5, resorder.getPs());
            pstmt.executeUpdate();

            //插完订单表，再插订单详情表
            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                roid = rs.getInt(1);
            }
            String sql2 = "insert into resorderitem(roid, fid, dealprice, num) values(?,?,?,?)";
            for (CartItem cartItem : cartItems) {
                pstmt = con.prepareStatement(sql2);
                pstmt.setString(1, roid+"");
                pstmt.setString(2, cartItem.getFood().getFid()+"");
                pstmt.setString(3, cartItem.getFood().getRealprice()+"");
                pstmt.setString(4, cartItem.getNum()+"");
                pstmt.executeUpdate();
            }
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if(con!=null){
                con.rollback();
            }
            throw e;
        }finally {
            if(con!=null){
                con.close();
            }
        }

        return result;
    }
}
