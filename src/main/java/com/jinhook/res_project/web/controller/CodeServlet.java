package com.jinhook.res_project.web.controller;

import javax.imageio.ImageIO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Log4j
@WebServlet("/code.action")
public class CodeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int width=100;
        int height=50;

        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);

        //2.美化图片
        //2.1填充背景色
        Graphics g=image.getGraphics();//画笔对象
        g.setColor(Color.PINK);
        g.fillRect(0, 0, width, height);

        //2.2画边框
        g.setColor(Color.red);
        g.drawRect(0, 0, width-1, height-1);

        //2.3写验证码
        String str = "abcdefghijklmnopqrstuvwxyz123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i = 1; i<=4;i++){
            int index=  random.nextInt(str.length());
            char ch = str.charAt(index);

            sb.append(ch);
            g.drawString( ch+"", width/5*i, height/2);
        }
        log.info("验证码是："+sb.toString());
        System.out.println("验证码是："+sb.toString());

        HttpSession session=req.getSession();
        session.setAttribute("code", sb.toString());

        g.setColor(Color.green);
        for (int i = 0; i < 10; i++) {
            int x1 = random.nextInt(width);
            int x2 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int y2 = random.nextInt(height);
            g.drawLine(x1, x2, y1, y2);
        }
        resp.setContentType("image/png");
        ImageIO.write(image, "jpg", resp.getOutputStream());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
