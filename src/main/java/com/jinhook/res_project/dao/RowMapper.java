package com.jinhook.res_project.dao;

import java.sql.ResultSet;

@FunctionalInterface	//此注解表示支持lambda表达式写法
public interface RowMapper<T> {
	
	//此接口用于对ResultSet中每一行数据的处理
	/**
	 * 作用：将ResultSet中的第rowIndex行的一条数据  处理成一个T对象，这个对象我不知道是什么，所以用泛型T表示，但是用户知道，由用户决定T是什么
	 * @param rs：用户实际传入的结果集
	 * @param rowIndex：结果集中的第n行（感觉这行没什么用）
	 * @return：返回这一行对应的结果T
	 */
	T mapRow(ResultSet rs, int rowIndex) throws Exception;
	
	
	//设计模式：模板模式
	//流程是操作：其中有n个操作不确定，由用户决定
}
