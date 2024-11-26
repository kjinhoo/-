package com.jinhook.res_project.web.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据类
 */
@Data
public class PageBean<T> implements Serializable {
    private List<T> list;       //查出来的结果
    private int pages;      //当前页数
    private int pagesize;      //每页多少数据
    private int totalPages;     //总页数
    private int pre;
    private int next;
    private int total;      //总条数
    private String orderby;     //按什么排列
    private String orderType;   //升序还是降序

    public int getTotalPages(){
        if(total%pagesize==0){
            totalPages=total/pagesize;
        }else{
            totalPages=total/pagesize+1;
        }
        return totalPages;
    }

    public int getNext(){
        if(totalPages==pages){
            next=totalPages;
        }else{
            next=pages+1;
        }
        return next;
    }

    public int getPre(){
        if(1==pages){
           pre=1;
        }else{
            pre=pages-1;
        }
        return pre;
    }




}
