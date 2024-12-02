package com.jinhook.res_project.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageBean implements Serializable {
    private Integer pageno;
    private Integer pagesize;
    private String sortby;
    private String sorttype;
    private Integer pre;
    private Integer next;
    private Integer totalpages;
    private Integer total;
    private List<Resfood> dataset;
}
