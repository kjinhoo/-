package com.jinhook.res_project.dao;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.*;



public class DbHelper {

	private static Logger log = Logger.getLogger(DbHelper.class.getName());

	static {
		try {
			Class.forName(DbProperties.getInstance().getProperty("driverClassName"));
		} catch (ClassNotFoundException e) {
			log.error("驱动加载失败，程序结束.");
			e.printStackTrace();
			System.exit(0);// 驱动加载不了，系统退出
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		log.info("驱动加载成功");
	}

	/*
	 *获取与数据库的联接
	 *获取联接对象
	 * */
	public static Connection getConnection() throws SQLException, IOException {
		String url = DbProperties.getInstance().getProperty("url");
		String username = DbProperties.getInstance().getProperty("username");
		String password = DbProperties.getInstance().getProperty("password");
		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}

	/*
	 * 待执行的更新语句 更新语句 占位符对应的值 可以有0-N个，按顺序 受影响的行数
	 */
	public int doUpdate(String sql, Object... params) {
		int r = -1;
		try (Connection con = getConnection();
			 PreparedStatement stmt = con.prepareStatement(sql);) {

			//TODO:如何处理？
			doParams(stmt, params);
			r = stmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
		return r;
	}


	/*
	 * 处理sql语句中的参数值
	 * stmt
	 * params
	 * SQLException
	 * */
	public void doParams(PreparedStatement stmt, Object... params) throws SQLException {
		//循环
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
			}
		}
	}

	public <T> List<T> select(RowMapper<T> rowMapper, String sql, Object... params) {
		List<T> list = new ArrayList<>();
		try (
				Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql);
		) {

			doParams(stmt, params);
			ResultSet rs = stmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				T t = rowMapper.mapRow(rs, i);
				i++;
				list.add(t);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
		return list;
	}

	public <T> T selectOne(RowMapper<T> rowMapper, String sql, Object... params) {
		T t = null;
		try (Connection con = getConnection();
			 PreparedStatement stmt = con.prepareStatement(sql);) {

			//TODO:如何处理？
			doParams(stmt, params);
			ResultSet rs = stmt.executeQuery();
			int i = 0;
			while (rs.next()) {
				//对于rs这一行有几列 ——>交给用户处理
				t = rowMapper.mapRow(rs, i);
				i++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
		return t;
	}

	public List<Map<String, Object>> select(String sql, Object... params) {
		List<Map<String, Object>> list = new ArrayList<>();
		try (
				Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql);
		) {
			doParams(stmt, params);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			List<String> listColumnName = new ArrayList<String>();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = rsmd.getColumnName(i).toLowerCase();
				listColumnName.add(columnName);
			}
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columnCount; i++) {
					Object value = rs.getObject(i);
					map.put(listColumnName.get(i - 1), value);
				}
				list.add(map);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
		return list;

	}

	public Map<String, Object> selectOneMap(String sql, Object... params) {
		Map<String, Object> map = new LinkedHashMap<>();
		try (
				Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql);
		) {
			doParams(stmt, params);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			List<String> listColumnName = new ArrayList<String>();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = rsmd.getColumnName(i).toLowerCase();
				listColumnName.add(columnName);
			}
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					Object value = rs.getObject(i);
					map.put(listColumnName.get(i - 1), value);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex.getMessage());
		}
		return map;

	}

	public int queryForInt(String sql, Object[] params) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;

		try {
			conn = getConnection();  // 假设 getConnection() 方法会返回一个有效连接
			stmt = conn.prepareStatement(sql);

			//在准备好的语句中设置参数
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					stmt.setObject(i + 1, params[i]);
				}
			}

			rs = stmt.executeQuery();

			// 提取整数结果
			if (rs.next()) {
				result = rs.getInt(1);  // 假设结果位于第一列
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(conn, stmt, rs);  // 关闭最后区块中的资源
		}

		return result;
	}

	private void close(Connection conn, PreparedStatement stmt, ResultSet rs) {
		// TODO Auto-generated method stub

	}
}










