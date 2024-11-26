package com.jinhook.res_project.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class DbProperties extends Properties{
	
	private static DbProperties dbProperties;
	
	private DbProperties(){
		//以反射方式来获取文件流
		//					  getClassLoader获得类加载器 到bin目录下加载 db.properties 文件，以文件流的形式加载
		InputStream iis = DbProperties.class.getClassLoader().getResourceAsStream("db.properties");
	
		try {
			super.load(iis);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//单例确保该类只实例化一次（即构造方法只执行一次），所以配置文件只读取一次，符合配置文件的特性
	public static DbProperties getInstance() {
		if(dbProperties == null) {
			dbProperties = new DbProperties();
		}
		return dbProperties;
	}

}
