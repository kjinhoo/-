package com.jinhook.res_project.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Resorder implements Serializable {
    private Integer roid;
    private Integer userid;
    private String address;
    private String tel;
    private String ordertime;
    private String deliverytime;
    private String ps;
    private Integer status;
}
