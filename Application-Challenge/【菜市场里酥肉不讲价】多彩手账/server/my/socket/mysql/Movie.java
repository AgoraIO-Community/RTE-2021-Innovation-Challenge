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
//	//���ص�Ӱ��ǩ����
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
//			//����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������
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
//			//����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������
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
//	//���ص�ӰID�����û���Ӱģ������
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
//			//����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������
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
//	//���ص�Ӱ����
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
//			//����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������
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
//	//���ڵ�Ӱ��ǩ����,ֱ�ӷ��������ַ���
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
//			//����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������
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
//	//������Ʒ�Ƽ�JSON�ַ�������
//	public static String getJSONstr(long[] movieid,int recommendnum,int requestnum) {
//		//�õ������ַ���
//        String jsonstr = "{\"length\":" + requestnum;
//        List<Entry<Long, Integer>> item = null;
//        if(movieid.length>0) {
//        	//���id����0���������Ƽ�
//        	item = RecommendFactory.getRecommendFactory().getItemRecommendList(movieid[0], recommendnum, requestnum);
//    	}
//        if(item!=null) {
//        	int size = requestnum;
//        	if(item.size()<requestnum) {
//        		size = item.size();
//        	}
//        	jsonstr = "{\"length\":" + size;
//        	long[] idnum = new long[size];
//        	//Ҫ�õ�
//        	for(int i = 0;i < requestnum;i++) {
//    			idnum[i] = item.get(i).getKey();
//    		}
//        	//�����idת��Ϊ����
//        	Name_Id moviename = Movie.getMovieName(idnum);
//        	String[] tags = Movie.getMovieTags(idnum);
//        	if(moviename != null) {
//        		jsonstr += ",\"data\":[";
//            	for(int i = 0;i<requestnum;i++) {
//            		//����json�ַ���
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
//	//�õ���Ӱ����
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
//			//����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������
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
//	//����������ӰJSON�ַ�������
//	public static String getJSONstr(long[] movieid,String[] tags) {
//		//�õ������ַ���
//        String jsonstr = "{\"length\":" + movieid.length;
//        if(movieid.length>0) {
//        	//�����ѯ���������ĵ�Ӱ����
//        	Name_Id moviename = Movie.getMovieName(movieid);
//        	if(moviename != null) {
//        		jsonstr += ",\"data\":[";
//            	for(int i = 0;i<movieid.length;i++) {
//            		//����json�ַ���
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
//		//�õ������ַ���
//        String jsonstr = "{\"totalpage\":" + totalpage + ",\"page\":" + page + ",\"length\":" + movieid.length;
//        if(movieid.length>0) {
//        	//�����ѯ���������ĵ�Ӱ����
//        	Name_Id moviename = Movie.getMovieName(movieid);
//        	if(moviename != null) {
//        		jsonstr += ",\"data\":[";
//            	for(int i = 0;i<movieid.length&&!tags[i].equals(null);i++) {
//            		//����json�ַ���
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
