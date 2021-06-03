package my.socket.mysql;

import java.sql.*;

//import my.socket.utils.MovieTags;
import my.socket.utils.Protocol;

public class UserOpration {
	public static Connection connect = null;

	public static int usernum;
	private UserOpration() {}
	private static UserOpration userOprator = new UserOpration();
	public static UserOpration getUserOprator() {return userOprator;}
	//登录操作
	public String longin(String username, String passwd)  {
		try
		{
			DBManager db = new DBManager();
			ResultSet ret = db.select(String.format("select * from t_user where user='%s' and pwd='%s' ",username, passwd));
			if (ret.next()) {
				ret.close();
				return Protocol.LOGIN_SUCCESS;
			}
			ret.close();
			return Protocol.LOGIN_FAILED;
		}
		catch (SQLException e)
		{
			return Protocol.LOGIN_FAILED;
		}

	}
	//注册操作
	public String register(String username, String passwd) {
		try
		{
			DBManager db = new DBManager();
			ResultSet ret = db.select(String.format("select * from t_user where user='%s'",username));
			if(ret.next())
				return Protocol.REGISTER_EXIST;
			ret.close();
			int id = db.insert(String.format("insert into  t_user (user, pwd) values ('%s' , '%s')",username, passwd));
			if(id == -1)
				return Protocol.REGISTER_FAILED;
			return Protocol.REGISTER_SUCCESS;
		}
		catch (SQLException e)
		{
			return Protocol.REGISTER_FAILED;
		}
	}
	//注销操作
//	public String logout(String username) {
//		String result = Protocol.LOGOUTFAILED;
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//		}catch(ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			if(connect == null)
//				//连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码
//				connect = DriverManager.getConnection(sqlbase,user,password);
//			System.out.println("Success connect Mysql server!");
//		}catch(SQLException e) {
//			System.out.println("Failed to connect Mysql server!");
//			e.printStackTrace();
//		}
//		if(connect != null) {
//			try {
//				Statement stmt = connect.createStatement();
//				ResultSet rs = stmt.executeQuery("select * from userinfo where userName = '" + username + "'");
//				if(rs.next()) {
//					String sql = "update userinfo set isLogin = 0 where username = '" + username + "'";
//					PreparedStatement prestmt = (PreparedStatement)connect.prepareStatement(sql);
//					prestmt.executeUpdate();
//					result  = Protocol.LOGOUTSUCCESS;
//				}
//				else {
//					result = Protocol.LOGOUTFAILED;
//				}
//			}catch(SQLException e) {
//				System.out.println("Logout failed!");
//				result = Protocol.LOGOUTFAILED;
//			}
//		}
//		return result;
//	}
//	//监测用户是否评分过
//	public boolean rated(String username) {
//		boolean result = false;
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//		}catch(ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			if(connect == null)
//				//连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码
//				connect = DriverManager.getConnection(sqlbase,user,password);
//			System.out.println("Success connect Mysql server!");
//		}catch(SQLException e) {
//			System.out.println("Failed to connect Mysql server!");
//			e.printStackTrace();
//		}
//		if(connect != null) {
//			try {
//				Statement stmt = connect.createStatement();
//				ResultSet rs = stmt.executeQuery("select ratings.userId from ratings,userinfo where "
//						+ "ratings.userId = userinfo.userId and userinfo.userName = '" + username + "'");
//				if(rs.next()) {
//					result = true;
//				}
//				else {
//					result = false;
//				}
//			}catch(SQLException e) {
//				System.out.println("User have not rated!");
//				result = false;
//			}
//		}
//		return result;
//	}
//	//监测用户是否在线
//	public boolean isOnline(String username) {
//		boolean result = false;
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//		}catch(ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			if(connect == null)
//				//连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码
//				connect = DriverManager.getConnection(sqlbase,user,password);
//			System.out.println("Success connect Mysql server!");
//		}catch(SQLException e) {
//			System.out.println("Failed to connect Mysql server!");
//			e.printStackTrace();
//		}
//		if(connect != null) {
//			try {
//				Statement stmt = connect.createStatement();
//				ResultSet rs = stmt.executeQuery("select isLogin from userinfo where userinfo.username = '" + username + "'");
//				if(rs.next()) {
//					int logined = rs.getInt("isLogin");
//					if(logined == 1) {
//						result = true;
//					}
//					else {
//						result = false;
//					}
//				}
//				else {
//					result = false;
//				}
//			}catch(SQLException e) {
//				System.out.println("User have not logined!");
//				result = false;
//			}
//		}
//		return result;
//	}
//	//获取用户ID
//	public long getUserId(String username) {
//		long result = -1;
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//		}catch(ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			if(connect == null)
//				//连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码
//				connect = DriverManager.getConnection(sqlbase,user,password);
//			System.out.println("Success connect Mysql server!");
//		}catch(SQLException e) {
//			System.out.println("Failed to connect Mysql server!");
//			e.printStackTrace();
//		}
//		if(connect != null) {
//			try {
//				Statement stmt = connect.createStatement();
//				ResultSet rs = stmt.executeQuery("select userId from userinfo where username = '" + username + "'");
//				if(rs.next()) {
//					result = rs.getLong("userId");
//				}
//				else {
//					result = -1;
//				}
//			}catch(SQLException e) {
//				System.out.println("User have not logined!");
//				result = -1;
//			}
//		}
//		return result;
//	}
}
