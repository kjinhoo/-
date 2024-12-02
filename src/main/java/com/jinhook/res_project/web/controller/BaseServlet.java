package com.jinhook.res_project.web.controller;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

public abstract class BaseServlet extends HttpServlet {

    /**
     * 将请求参数封装到实体类中,简化各Servlet中的代码
     * @param req 请求对象，用于获取请求参数
     * @param clazz 要封装的实体类的Class对象
     * @return
     */
    protected <T> T parseRequestParamToT(HttpServletRequest req, Class<T> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        //用反射 实例化对象
        T t = clazz.newInstance();

        //获取所有的请求参数名
        Enumeration<String> names = req.getParameterNames();

        while (names.hasMoreElements()) {
            //利用请求参数名获得请求参数值
            String name = names.nextElement();
            String value = req.getParameter(name);

            String setMethodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);

            //这里只能通过拿到全部方法，然后循环，
            Method[] methods = clazz.getDeclaredMethods();
            for(Method m : methods){
                //存在该方法
                if(m.getName().equals(setMethodName)){
                    String pType = m.getParameterTypes()[0].getSimpleName();
                    if("int".equals(pType) || "Integer".equals(pType)){
                        int i = Integer.parseInt(value);
                        m.invoke(t, i);
                    }else if("double".equals(pType) || "Double".equals(pType)){
                        double i = Double.parseDouble(value);
                        m.invoke(t, i);
                    }else if("float".equals(pType) || "Float".equals(pType)){
                        float i = Float.parseFloat(value);
                        m.invoke(t, i);
                    }else if("short".equals(pType) || "Short".equals(pType)){
                        short i = Short.parseShort(value);
                        m.invoke(t, i);
                    }else if("long".equals(pType) || "Long".equals(pType)){
                        long i = Long.parseLong(value);
                        m.invoke(t, i);
                    }else if("boolean".equals(pType) || "Boolean".equals(pType)){
                        boolean i = Boolean.parseBoolean(value);
                        m.invoke(t, i);
                    }else if("byte".equals(pType) || "Byte".equals(pType)){
                        byte i = Byte.parseByte(value);
                        m.invoke(t, i);
                    }else{
                        //这是String的
                        m.invoke(t, value);
                    }
                    break;
                }
            }
        }
        return t;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String op = req.getParameter("op");

        Method method = null;
        try {
            method = this.getClass().getDeclaredMethod(op, HttpServletRequest.class, HttpServletResponse.class);
            if(method == null){
                resp.sendError(404);
            }else {
                //要求所有的方法返回值都是ResponseResult对象
                Object responseResult = method.invoke(this, req, resp);
                //将servlet第三步：“转”给封装起来了，各servlet类中的任意方法都无需再关心将数据转为json数据返回给前端的问题，
                // 只需要返回一个ResponseResult对象即可。
                writeJson(responseResult, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(500);
        }
    }

    private void writeJson(Object responseResult, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(responseResult);
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println(jsonString);
        out.flush();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.setCharacterEncoding("utf-8");  // 解决post乱码问题
        super.service(req, resp);   //判断请求方式，最终调用相应方法
    }
}
