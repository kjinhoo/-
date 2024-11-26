package com.jinhook.res_project.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class Resuser implements Serializable {
    private Integer userid;
    private String username;
    private String pwd;
    private String email;
}
