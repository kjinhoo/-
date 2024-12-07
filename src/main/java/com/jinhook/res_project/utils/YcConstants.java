package com.jinhook.res_project.utils;

public class YcConstants {
    //redis中保存用户历史记录的键名后缀
    public static final String REDIS_VISITED = "_visited";

    //session中用于保存用户是否登录
    public static final String RESUSER = "resuser";
    //redis中保存用户点赞记录的键名后缀
    public static final String REDIS_PRAISE = "_praise";
    //redis中保存菜品被点赞记录的键名后缀
    public static final String REDIS_FOOD_PRAISE = "_food_praise";

    public static final String REDIS_USER_BROWSER= "_user_browser";

    public static final String REDIS_BROWSER_USER = "_browser_user";


}
