package com.jinhook.res_project.web.controller;

import com.jinhook.res_project.bean.Resfood;
import com.jinhook.res_project.dao.DbHelper;
import com.jinhook.res_project.web.model.ResponseResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/resfood.action")
public class ResfoodServlet extends BaseServlet{

    public ResponseResult findAllFoods(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sql = "select * from resfood";
        DbHelper db = new DbHelper();
        List<Resfood> result = db.select((rs, rownum) -> {
            Resfood r = new Resfood();
            r.setFid(rs.getInt("fid"));
            r.setFname(rs.getString("fname"));
            r.setNormprice(rs.getDouble("normprice"));
            r.setRealprice(rs.getDouble("realprice"));
            r.setDetail(rs.getString("detail"));
            r.setFphoto(rs.getString("fphoto"));
            return r;
        }, sql);
        if(result != null && result.size()>0){
            return ResponseResult.ok().setData(result);
        }
        return ResponseResult.error("无菜品信息");
    }
}
