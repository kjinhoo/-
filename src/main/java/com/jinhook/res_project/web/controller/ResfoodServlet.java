package com.jinhook.res_project.web.controller;

import com.jinhook.res_project.bean.PageBean;
import com.jinhook.res_project.bean.Resfood;
import com.jinhook.res_project.bean.Resuser;
import com.jinhook.res_project.dao.DbHelper;
import com.jinhook.res_project.dao.RedisHelper;
import com.jinhook.res_project.utils.YcConstants;
import com.jinhook.res_project.web.model.ResponseResult;
import com.jinhook.res_project.web.service.ResfoodBiz;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@WebServlet("/resfood.action")
public class ResfoodServlet extends BaseServlet{

    public ResponseResult getHistory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Resuser resuser = (Resuser) session.getAttribute( YcConstants.RESUSER );
        if(resuser==null){
            return ResponseResult.noLogin();
        }
        int userid = resuser.getUserid();
        //连接redis
        Jedis jedis = RedisHelper.getRedisInstance();
        //从redis中获取用户足迹
        Set<String> visitedFoodsIds = jedis.zrevrange(userid + YcConstants.REDIS_VISITED, 0, 4);
        if(visitedFoodsIds==null || visitedFoodsIds.size()==0){
            return ResponseResult.error("无足迹");
        }

        StringBuffer sql = new StringBuffer("select * from resfood where fid in ( ");
        for (String fid : visitedFoodsIds) {
            sql.append(" ?,");
        }
        String resultSql = sql.toString();
        resultSql = resultSql.substring(0, resultSql.length() - 1);
        resultSql += ")";
        System.out.println(resultSql);
        DbHelper db = new DbHelper();
        List<Resfood> list = db.select((rs, rownum)->{
            Resfood r = new Resfood();
            r.setFid(rs.getInt("fid"));
            r.setFname(rs.getString("fname"));
            return r;
        },resultSql, visitedFoodsIds.toArray());
        return ResponseResult.ok().setData(list);
    }

    public ResponseResult traceBrowserFoods(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fid = req.getParameter("fid");
        HttpSession session = req.getSession();
        if(session.getAttribute(YcConstants.RESUSER)==null){
            return ResponseResult.error("未登录不记录足迹");
        }
        //登录了的用户，记录足迹
        int userid = ((Resuser)session.getAttribute(YcConstants.RESUSER)).getUserid();
        //连接redis
        Jedis jedis = RedisHelper.getRedisInstance();
        Date date = new Date();
        //只记录最近五条足迹，多的删除掉，模拟淘宝最近三十天记录。
        Long zcard = jedis.zcard(userid + YcConstants.REDIS_VISITED);
        if(zcard > 4){
            //删除第一条
            jedis.zremrangeByRank(userid + YcConstants.REDIS_VISITED, 0, 0);  //删除
        }
        jedis.zadd(userid + YcConstants.REDIS_VISITED, date.getTime(), fid);
        return ResponseResult.ok().setData("追踪足迹成功");


    }

    public ResponseResult findFoodsByPages(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            PageBean pb = parseRequestParamToT(req, PageBean.class);
            ResfoodBiz resfoodBiz = new ResfoodBiz();
            pb = resfoodBiz.findByPage(pb);
            return ResponseResult.ok().setData(pb);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("查询失败,原因:" + e.getMessage() );
        }
    }

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
