package com.jinhook.res_project.web.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
public class SessionChangeListener implements HttpSessionAttributeListener {
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        System.out.println("session添加属性");
        String name = event.getName();
        Object value = event.getValue();
        System.out.println(name + " : " + value);
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        System.out.println("session移除属性");
        String name = event.getName();
        Object value = event.getValue();
        System.out.println(name + " : " + value);
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        System.out.println("session替换属性");
        String name = event.getName();
        Object value = event.getValue();
        System.out.println(name + " : " + value);
    }
}
