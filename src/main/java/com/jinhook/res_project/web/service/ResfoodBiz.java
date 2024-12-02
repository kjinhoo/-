package com.jinhook.res_project.web.service;

import com.jinhook.res_project.bean.PageBean;
import com.jinhook.res_project.bean.Resfood;
import com.jinhook.res_project.dao.DbHelper;

import java.util.List;

public class ResfoodBiz {

    public PageBean findByPage(PageBean pb){
        String orderby = pb.getSortby();
        String orderType = pb.getSorttype();
        int pages = pb.getPageno();
        int pagesize = pb.getPagesize();
        List<Resfood> dataset = find(orderby, orderType, pages, pagesize);
        pb.setDataset(dataset);
        long total = countAll();
        pb.setTotal((int)total);
        //上一页和后一页和总页数
        if(total%pagesize==0){
            pb.setTotalpages((int)total/pagesize);
        }
        pb.setTotalpages((int)total/pagesize + 1);
        if(pages==1){
            pb.setPre(1);
        }else{
            pb.setPre(pages-1);
        }
        if(pages==pb.getTotalpages()){
            pb.setNext(pb.getTotalpages());
        }else{
            pb.setNext(pages+1);
        }
        return pb;
    }

    private long countAll(){
        String sql = "select count(*) from resfood";
        DbHelper db = new DbHelper();
        return db.selectOne((rs, rownum)->{
            return rs.getLong(1);
        }, sql);
    }

    private List<Resfood> find(String orderby, String ordertype, int pageno, int pagesize){
        String sql = "select * from resfood order by "+orderby+" "+ordertype + " limit ?,?";
        DbHelper db = new DbHelper();
        int start = (pageno -1 )*pagesize;
        return db.select((rs, rownum)->{
            Resfood r = new Resfood();
            r.setFid(rs.getInt(1));
            r.setFname(rs.getString(2));
            r.setNormprice(rs.getDouble(3));
            r.setRealprice(rs.getDouble(4));
            r.setDetail(rs.getString(5));
            r.setFphoto(rs.getString(6));
            return r;
        },sql,  start, pagesize);
    }

}
