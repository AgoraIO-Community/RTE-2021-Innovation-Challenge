//package my.socket.mysql;
//
//import java.sql.*;
//import java.util.List;
//import java.util.Map.Entry;
//
//import my.socket.mahout.RecommendFactory;
//import my.socket.utils.MovieTags;
//import my.socket.utils.Protocol;
//
//public class Movie {
//	public static Connection connect = null;
//	public static String sqlbase = "jdbc:mysql://localhost:3306/movie?characterEncoding=utf8";
//	public static String user = "root";
//	public static String password = "mysql";
//	public static int perpagenum = 20;
//	//返回电影标签数组
//	public static String[] getMovieTags(long id) {
//		String[] tags = null;
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
//				ResultSet rs = stmt.executeQuery("select * from movies where movieId =" + id);
//				rs.next();
//				tags = rs.getString("genres").split("\\|");
//
//			}catch(SQLException e) {
//				System.out.println("Can not find movie!");
//				tags = null;
//			}
//		}
//		return tags;
//	}
//	public static String[] getMovieTags(long[] id) {
//		String[] tags = new String[id.length];
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
//				ResultSet rs = null;
//				for(int i = 0;i < id.length;i++) {
//					rs = stmt.executeQuery("select * from movies where movieId =" + id[i]);
//					if(rs.next()) {
//						tags[i] = rs.getString("genres");
//					}
//					else {
//						tags[i] = "null";
//					}
//				}
//			}catch(SQLException e) {
//				System.out.println("Can not find movie!");
//				tags = null;
//			}
//		}
//		return tags;
//	}
//	//返回电影ID数组用户电影模糊搜索
//	public static long[] getMovieId(String name) {
//		long[] nameid = new long[1];
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
//			e.printStackTrace();
//			System.out.println("Failed to connect Mysql server!");
//		}
//		if(connect != null) {
//			try {
//				Statement stmt = connect.createStatement();
//				int n = 50;
//				nameid = new long[n];
//				ResultSet rs = stmt.executeQuery("select * from movies where title like '%" + name + "%' limit 0,50");
//				int i = 0;
//				while(rs.next()) {
//					nameid[i] = rs.getLong("movieId");
//					i++;
//				}
//			}catch(SQLException e) {
//				System.out.println("Can not find movie!");
//				nameid[0] = -1;
//			}
//		}
//		return nameid;
//	}
//	//返回电影名字
//	public static Name_Id getMovieName(long[] id) {
//		Name_Id movie = new Name_Id(id.length);
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			System.out.println("success!");
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
//				ResultSet rs = null;
//				for(int i = 0;i < id.length;i++) {
//					rs = stmt.executeQuery("select * from movies,links where movies.movieId = links.movieId and movies.movieId =" + id[i]);
//					if(rs.next()) {
//						movie.name[i] = rs.getString("title");
//						movie.id[i] = rs.getInt("tmdbId");
//					}
//					else {
//						movie.name[i] = "null";
//						movie.id[i] = 0;
//					}
//				}
//			}catch(SQLException e) {
//				System.out.println("Can not find movie!");
//				movie = null;
//			}
//		}
//		return movie;
//	}
//	//用于电影标签分类,直接返回最终字符串
//	public static String getTagMovieId(String tag,int page) {
//		long[] movieId = new long[perpagenum];
//		String[] tags = new String[perpagenum];
//		float[] ratings = new float[perpagenum];
//		int totalpage = 0;
//		String finalstr = "";
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			System.out.println("success!");
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
//				String sql = "select * from movies,avgratings WHERE movies.movieId = avgratings.movieId and genres like '%"
//						+ MovieTags.translate(tag, Protocol.CHTOEN)
//						+ "%' ORDER BY avgrating DESC limit " + ((page-1)*perpagenum) + "," + page*perpagenum;
//				ResultSet rs = stmt.executeQuery(sql);
//				int i = 0;
//				while(rs.next()&& i < perpagenum) {
//					movieId[i] = rs.getLong("movieId");
//					tags[i] = rs.getString("genres");
//					ratings[i] = rs.getFloat("avgrating");
//					i++;
//				}
//				if(i<perpagenum) {
//					for(;i<perpagenum;i++) {
//						movieId[i] = -1;
//						tags[i] = null;
//					}
//				}
//				sql = "select count(*) as num from movies,avgratings WHERE movies.movieId = avgratings.movieId and genres like '%"
//						+ MovieTags.translate(tag, Protocol.CHTOEN)+ "%'";
//				rs = stmt.executeQuery(sql);
//				if(rs.next()) {
//					totalpage = rs.getInt("num")/perpagenum + 1;
//				}
//				finalstr = getJSONstr(movieId, tags, ratings, totalpage, page);
//			}catch(SQLException e) {
//				e.printStackTrace();
//				System.out.println("Can not find movie!");
//			}
//		}
//		return finalstr;
//	}
//	//用于物品推荐JSON字符串返回
//	public static String getJSONstr(long[] movieid,int recommendnum,int requestnum) {
//		//得到传回字符串
//        String jsonstr = "{\"length\":" + requestnum;
//        List<Entry<Long, Integer>> item = null;
//        if(movieid.length>0) {
//        	//如果id大于0，则请求推荐
//        	item = RecommendFactory.getRecommendFactory().getItemRecommendList(movieid[0], recommendnum, requestnum);
//    	}
//        if(item!=null) {
//        	int size = requestnum;
//        	if(item.size()<requestnum) {
//        		size = item.size();
//        	}
//        	jsonstr = "{\"length\":" + size;
//        	long[] idnum = new long[size];
//        	//要用的
//        	for(int i = 0;i < requestnum;i++) {
//    			idnum[i] = item.get(i).getKey();
//    		}
//        	//这里把id转换为名称
//        	Name_Id moviename = Movie.getMovieName(idnum);
//        	String[] tags = Movie.getMovieTags(idnum);
//        	if(moviename != null) {
//        		jsonstr += ",\"data\":[";
//            	for(int i = 0;i<requestnum;i++) {
//            		//生成json字符串
//            		String[] tag = tags[i].split("\\|");
//            		for(int j = 0;j<tag.length;j++) {
//                		tag[j] = MovieTags.translate(tag[j], Protocol.ENTOCH);
//             		}
//            		jsonstr += "{\"name\":\"" + moviename.name[i] + "\",\"rating\":"
//            		+ RecommendFactory.getRecommendFactory().getMovieRating(item.get(i).getKey()) + ",\"id\":" + moviename.id[i] + ",\"tags\":[";
//            		for(int j = 0;j<tag.length;j++) {
//            			jsonstr += "\"" + tag[j] + "\"";
//            			if(j + 1 == tag.length) {
//            				break;
//            			}
//            			jsonstr += ",";
//            		}
//            		jsonstr += "]}";
//            		if(i + 1 == requestnum) {
//            			break;
//            		}
//            		jsonstr += ",";
//            	}
//        	jsonstr += "]";
//        	}
//        	else {
//        		jsonstr = "{\"length\":0";
//        	}
//        }
//        else {
//        	jsonstr = "{\"length\":0";
//        }
//        jsonstr += "}";
//        return jsonstr;
//	}
//	//得到电影评分
//	public static float getRating(long movieId) {
//		float rating = 0.0f;
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			System.out.println("success!");
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
//				String sql = "select * from avgratings WHERE movieId = " + movieId;
//				ResultSet rs = stmt.executeQuery(sql);
//				if(rs.next()) {
//					rating = rs.getFloat("avgrating");
//				}
//			}catch(SQLException e) {
//				e.printStackTrace();
//				System.out.println("Can not find movie!");
//			}
//		}
//		return rating;
//	}
//	//用于搜索电影JSON字符串返回
//	public static String getJSONstr(long[] movieid,String[] tags) {
//		//得到传回字符串
//        String jsonstr = "{\"length\":" + movieid.length;
//        if(movieid.length>0) {
//        	//这里查询搜索出来的电影名称
//        	Name_Id moviename = Movie.getMovieName(movieid);
//        	if(moviename != null) {
//        		jsonstr += ",\"data\":[";
//            	for(int i = 0;i<movieid.length;i++) {
//            		//生成json字符串
//            		String[] tag = tags[i].split("\\|");
//            		for(int j = 0;j<tag.length;j++) {
//                		tag[j] = MovieTags.translate(tag[j], Protocol.ENTOCH);
//             		}
//            		jsonstr += "{\"name\":\"" + moviename.name[i] + "\",\"rating\":" + getRating(movieid[i]) + ",\"id\":" + moviename.id[i] + ",\"tags\":[";
//            		for(int j = 0;j<tag.length;j++) {
//            			jsonstr += "\"" + tag[j] + "\"";
//            			if(j + 1 == tag.length) {
//            				break;
//            			}
//            			jsonstr += ",";
//            		}
//            		jsonstr += "]}";
//            		if(i + 1 == movieid.length) {
//            			break;
//            		}
//            		jsonstr += ",";
//            	}
//        	jsonstr += "]";
//        	}
//        	else {
//        		jsonstr = "{\"length\":0";
//        	}
//        }
//        jsonstr += "}";
//		return jsonstr;
//	}
//
//	private static String getJSONstr(long[] movieid,String[] tags , float[] ratings, int totalpage, int page) {
//		//得到传回字符串
//        String jsonstr = "{\"totalpage\":" + totalpage + ",\"page\":" + page + ",\"length\":" + movieid.length;
//        if(movieid.length>0) {
//        	//这里查询搜索出来的电影名称
//        	Name_Id moviename = Movie.getMovieName(movieid);
//        	if(moviename != null) {
//        		jsonstr += ",\"data\":[";
//            	for(int i = 0;i<movieid.length&&!tags[i].equals(null);i++) {
//            		//生成json字符串
//            		String[] tag = tags[i].split("\\|");
//            		for(int j = 0;j<tag.length;j++) {
//                		tag[j] = MovieTags.translate(tag[j], Protocol.ENTOCH);
//             		}
//            		jsonstr += "{\"name\":\"" + moviename.name[i] + "\",\"rating\":" + ratings[i] + ",\"id\":" + moviename.id[i] + ",\"tags\":[";
//            		for(int j = 0;j<tag.length;j++) {
//            			jsonstr += "\"" + tag[j] + "\"";
//            			if(j + 1 == tag.length) {
//            				break;
//            			}
//            			jsonstr += ",";
//            		}
//            		jsonstr += "]}";
//            		if(i + 1 == movieid.length) {
//            			break;
//            		}
//            		jsonstr += ",";
//            	}
//        	jsonstr += "]";
//        	}
//        	else {
//        		jsonstr = "{\"totalpage\":0,\"page\":1,\"length\":0";
//        	}
//        }
//        jsonstr += "}";
//		return jsonstr;
//	}
//}
