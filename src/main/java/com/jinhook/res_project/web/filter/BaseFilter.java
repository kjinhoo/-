package com.jinhook.res_project.web.filter;

import com.google.gson.Gson;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseFilter implements Filter {    //适配器类，空实现

    public void writeJson(Object responseResult, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(responseResult);
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println(jsonString);
        out.flush();
    }

}
