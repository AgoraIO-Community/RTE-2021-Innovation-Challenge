//package my.socket.mysql;
//
//
//import my.socket.filesystem.DataUpdate;
//import my.socket.utils.Protocol;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.sql.*;
//import java.util.Calendar;
//
//public class CommentManager {
//	public static Connection connect = null;
//	public static String sqlbase = "jdbc:mysql://127.0.0.1:3306/movie?characterEncoding=utf8";
//	public static String user = "root";
//	public static String password = "mysql";
//	//这里是存储用户评论的函数
//	public static String setComment(String message) {
//		String result = null;
//		try {
//			JSONObject js = new JSONObject(message);
//			//连接数据库，查询电影代码，并把message转化为JSONObject，将评论存储在数据库
//			try {
//				Class.forName("com.mysql.cj.jdbc.Driver");
//			}catch(ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//			try {
//				if(connect == null)
//					connect = DriverManager.getConnection(sqlbase,user,password);
//				//连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码
//				System.out.println("Success connect Mysql server!");
//			}catch(SQLException e) {
//				System.out.println("Failed to connect Mysql server!");
//			}
//			if(connect != null) {
//				try {
//					Statement stmt = connect.createStatement();
//					String username = js.getString("username");
//					String moviename = js.getString("moviename");
//					int userId = 0;
//					int movieId = 0;
//
//					//通过用户名和电影名获取id
//					String sql = "select * from userinfo where userName = '"
//							+ username + "'";
//					ResultSet rs = stmt.executeQuery(sql);
//					if(rs.next()) userId = rs.getInt("userId");
//					sql = "select * from movies where title = '"
//							+ moviename + "'";
//					rs = stmt.executeQuery(sql);
//					if(rs.next()) movieId = rs.getInt("movieId");
//
//					//插入评论数据(comments表)
//					String comment = js.getString("comment");
//					float rating = (float)js.getDouble("rating");
//					Calendar calendars = Calendar.getInstance();
//			        String year = String.valueOf(calendars.get(Calendar.YEAR));
//			        String month = String.valueOf(calendars.get(Calendar.MONTH) + 1);
//			        String day = String.valueOf(calendars.get(Calendar.DATE));
//			        String hour = String.valueOf(calendars.get(Calendar.HOUR_OF_DAY));
//			        String min = String.valueOf(calendars.get(Calendar.MINUTE));
//			        String second = String.valueOf(calendars.get(Calendar.SECOND));
//			        String datetime = year + "-"+ month+ "-"+day+" "+hour+":"+min+":"+second;
//			        long timestamp = System.currentTimeMillis();
//			        sql = "insert into comments(userId,movieId,comment,rating,timestamp,datetime) values(?,?,?,?,?,?)";
//					PreparedStatement prestmt = (PreparedStatement)connect.prepareStatement(sql);
//					prestmt.setInt(1, userId);
//					prestmt.setInt(2, movieId);
//					prestmt.setString(3, comment);
//					prestmt.setFloat(4, rating);
//					prestmt.setLong(5, timestamp);
//					prestmt.setString(6, datetime);
//					prestmt.executeUpdate();
//
//					//插入评分数据(ratings表)
//					sql = "insert into ratings(userId,movieId,rating,timestamp) values(?,?,?,?)";
//					prestmt = (PreparedStatement)connect.prepareStatement(sql);
//					prestmt.setInt(1, userId);
//					prestmt.setInt(2, movieId);
//					prestmt.setFloat(3, rating);
//					prestmt.setLong(4, timestamp);
//					prestmt.executeUpdate();
//					//更新推荐计算的文件
//					DataUpdate.updateFile(userId, movieId, rating, timestamp);
//
//					sql = "select * from avgratings where movieId = " + movieId;
//					int ratingtimes = 0;
//					float avgrating = 0.0f;
//					rs = stmt.executeQuery(sql);
//					if(rs.next()) {
//						ratingtimes = rs.getInt("ratingtimes");
//						avgrating = rs.getFloat("avgrating");
//					}
//
//					float finalavgrating = (avgrating*ratingtimes + rating)/(ratingtimes + 1);
//					sql = "update avgratings set avgrating = " + finalavgrating + " where movieId = " + movieId;
//					prestmt = (PreparedStatement)connect.prepareStatement(sql);
//					prestmt.executeUpdate();
//
//					result = Protocol.COMMENTSUCCESS;     //返回值是评论结果（成功）
//				}catch(SQLException e) {
//					e.printStackTrace();
//					result = Protocol.COMMENTFAILD;		//返回值是评论结果（失败）
//				}
//			}
//		}
//		catch(JSONException e) {
//			e.printStackTrace();
//		}
//
//		return result;
//
//	}
//	//得到当前电影评论及评分的函数(传入电影名字)
//	public static String getComment(String moviename) {
//		String finalstr = "{}";			//存储最后得到的评论JSON字符串
//			//连接数据库，查询电影代码，并把message转化为JSONObject，将评论存储在数据库
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//		}catch(ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			if(connect == null)
//				connect = DriverManager.getConnection(sqlbase,user,password);
//			//连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码
//			System.out.println("Success connect Mysql server!");
//		}catch(SQLException e) {
//			System.out.println("Failed to connect Mysql server!");
//		}
//		if(connect != null) {
//			try {
//				Statement stmt = connect.createStatement();
//				int movieId = 0;
//				//通过电影名字获取电影Id
//				String sql = "select * from movies where title = '"
//						+ moviename + "'";
//				ResultSet rs = stmt.executeQuery(sql);
//				if(rs.next()) movieId = rs.getInt("movieId");
//
//
//				//通过电影Id查询相关评论
//				//先获取该电影评论数量comments_num
//				int comments_num = 0;
//				sql = "select count(*) as num from comments where movieId = '"
//						+ movieId + "'";
//				rs = stmt.executeQuery(sql);
//				if(rs.next()) comments_num = rs.getInt("num");
//				finalstr = "{\"length\":" + comments_num;
//
//				//评论数量不为0，按时间戳排序获取评论内容转成JSON字符串
//				if(comments_num != 0) {
//					finalstr += ",\"data\":[";
//					sql = "select * from userinfo,comments where userinfo.userId = comments.userId and movieId = '"
//							+ movieId + "' order by timestamp desc";
//					rs = stmt.executeQuery(sql);
//
//					//评论用户名，评论内容，评论等级，评论时间初始化
//					String username = "";
//					String comment = "";
//					float rating = 0;
//					String time = "";
//
//					//依次读取评论内容
//					for(int i=0;i<comments_num;i++) {
//						rs.next();
//
//						//依次获取评论用户名，评论内容，评论等级，评论时间
//			            username = rs.getString("userName");
//						comment = rs.getString("comment");
//						rating = rs.getFloat("rating");
//						time = rs.getString("datetime");
//						//加入JSON字符串
//						finalstr +="{\"username\":\"" + username + "\",\"comment\":\"" + comment
//				        		+ "\",\"rating\":" + rating + ",\"time\":\"" + time + "\"}";
//						if(i<(comments_num-1)) {
//							finalstr +=",";
//						}
//					}
//					finalstr += "]";
//				}
//				finalstr += "}";
//			}catch(SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return finalstr;
//	}
//}
