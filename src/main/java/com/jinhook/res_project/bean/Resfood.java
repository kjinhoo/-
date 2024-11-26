package com.jinhook.res_project.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Resfood implements Serializable {
    private Integer fid;
    private String fname;
    private Double normprice;
    private Double realprice;
    private String detail;
    private String fphoto;
}
