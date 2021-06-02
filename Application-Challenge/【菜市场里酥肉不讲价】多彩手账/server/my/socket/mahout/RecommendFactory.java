//package my.socket.mahout;
//
//import java.io.File;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.util.*;
//import java.util.Map.Entry;
//
//import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
//import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
//import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
//import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
//import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
//import org.apache.mahout.cf.taste.model.DataModel;
//import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
//import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
//import org.apache.mahout.cf.taste.recommender.RecommendedItem;
//import org.apache.mahout.cf.taste.recommender.Recommender;
//import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
//import org.apache.mahout.cf.taste.similarity.UserSimilarity;
//
//import com.mysql.cj.jdbc.MysqlDataSource;
//import java.sql.*;
//
//import my.socket.mysql.Movie;
//import my.socket.utils.MovieTags;
//import my.socket.utils.Protocol;
//
//
//
//public class RecommendFactory{
//
//	private int requestnum = 20;
//	private int recommendnum = 100;	//推荐列表数量
//	private DataModel model = null;
//	private ItemBasedRecommender itemRecommender;
//	private Recommender userRecommender;
//	private List<RecommendedItem> recommendedItemList;
//	private MysqlDataSource dataSource;
//	private Connection connect = null;
//	private final static String localname = "/home/ratings.csv";//本地测试：D:/Java codes/socketserver/src/ratings.csv
//	private final static String sqlbase = "jdbc:mysql://localhost:3306/movie?characterEncoding=utf8";//服务器测试：/home/ratings.csv
//	private final static String user = "root";
//	private final static String password = "mysql";
//	private RecommendFactory() {
//		initDatabase();
//		initModel();
//		initItemRecommender();
//		initUserRecommender();
//	}
//	private static RecommendFactory recommendFactory = new RecommendFactory();
//	public static RecommendFactory getRecommendFactory() {return recommendFactory;}
//	//调用物品推荐函数
// 	public List<Entry<Long, Integer>> getItemRecommendList(long itemid, int recommendnum, int requestnum){
//		this.requestnum = requestnum;
//		this.recommendnum = recommendnum;
//		requstItemRecommend(itemid);
//		String[] tags = Movie.getMovieTags(itemid);
//		return getGenreRecommendedList(tags);
//	}
// 	//调用用户推荐函数
// 	public List<Entry<Long, Float>> getUserRecommendList(long userId, int recommendnum, int requestnum){
// 		this.requestnum = requestnum;
// 		this.recommendnum = recommendnum;
// 		requstUserRecommend(userId);
//		return getRatingRecommendedList();
// 	}
// 	//调用分类的推荐函数
// 	public List<Entry<Long, Float>> getSortRecommndList(String username, int recommendnum, int requestnum){
// 		this.requestnum = requestnum;
// 		this.recommendnum = recommendnum;
// 		Map<Long, Float>map = new TreeMap<Long, Float>();
// 		String tags = "";
// 		try {
// 			String sql = "select tags from userinfo where username = '" + username +"'";
// 			Statement stmt = connect.createStatement();
//	    	ResultSet rs = stmt.executeQuery(sql);
//	    	if(rs.next()) {
//	    		tags = rs.getString("tags");
//	    	}
// 		}catch(SQLException e) {
// 			e.printStackTrace();
// 		}
// 		String[] usrgenrelist = tags.split("\\|");
// 		for(int i = 0;i<usrgenrelist.length;i++) {
// 			usrgenrelist[i] = MovieTags.translate(usrgenrelist[i], Protocol.CHTOEN);
// 		}
//    	//初始化查询语句
//    	String sql = "SELECT * FROM movies,avgratings WHERE movies.movieId = avgratings.movieId and (";
//    	int index = 0;
//		for (String usrgenre:usrgenrelist){
//			if (index==0){
//				sql+="genres like '%"+usrgenre+"%'";
//			}
//			else{
//				sql+=" OR genres like '%"+usrgenre+"%'";
//			}
//			index++;
//		}
//		sql += ") ORDER BY avgrating DESC";
//		try {
//			Statement stmt = connect.createStatement();
//	    	ResultSet rs = stmt.executeQuery(sql);
//	    	int i = 0;
//		    while(rs.next()&&i<requestnum) {
//		    	i++;
//		    	long movieId = rs.getLong("movieId");
//		    	map.put(movieId, this.getMovieRating(rs.getLong("movieId")));
//		    }
//			}catch(SQLException e) {
//				e.printStackTrace();
//			}
//		List<Entry<Long, Float>> entryArrayList = new ArrayList<Entry<Long, Float>>(map.entrySet());
//		return entryArrayList;
// 	}
// 	//针对评分排序
//  	private List<Entry<Long, Float>> getRatingRecommendedList(){
// 		Map<Long, Float>map = new TreeMap<Long, Float>();
// 		for(RecommendedItem recommendedItem : recommendedItemList) {
// 			map.put(recommendedItem.getItemID(), getMovieRating(recommendedItem.getItemID()));
// 		}
// 		if(!map.isEmpty()){
// 			List<Entry<Long, Float>> entryArrayList = new ArrayList<Entry<Long, Float>>(map.entrySet());
// 			Collections.sort(entryArrayList,new Comparator<Map.Entry<Long, Float>>(){
// 				public int compare(Map.Entry<Long, Float> it1, Map.Entry<Long, Float> it2) {
// 					if(it1.getValue()>it2.getValue()) {
// 						return -1;
// 					}
// 					else if(it2.getValue()>it1.getValue()) {
// 						return 1;
// 					}
// 					return 0;
//                }
// 			});
// 			if(entryArrayList.size()>20) {
// 				return entryArrayList.subList(0, requestnum);
// 			}
// 			else {
// 				return entryArrayList;
// 			}
// 		}
// 		else {
// 			System.out.println("map is null");
// 			return null;
// 		}
// 	}
//	//针对标签排序
//	private List<Entry<Long, Integer>> getGenreRecommendedList(String[] tags){
//    	Map<Long, Integer>map = new TreeMap<Long, Integer>();
//    	//初始化查询语句
//    	int index=0;
//    	for (RecommendedItem recommendedItem : recommendedItemList) {
//    		String sql = "SELECT COUNT(*) FROM movietags WHERE movieId =? AND (";
//    		if(recommendedItem.getValue()==1.0 || index<recommendedItemList.size()){
//    			int innerIndex = 0;
//        		for (String usrgenre:tags){
//        			if (innerIndex==0){
//        				sql+="genres='"+usrgenre+"'";
//        			}
//        			else{
//        				sql+=" OR genres='"+usrgenre+"'";
//        			}
//        			innerIndex++;
//        		}
//    		}
//    		else {
//    			break;
//    		}
//			sql+=")";
//			try{
//				PreparedStatement pstmt = connect.prepareStatement(sql);
//	        	pstmt.setLong(1, recommendedItem.getItemID());
//	        	ResultSet rs = pstmt.executeQuery();
//	    	    rs.next();
//	    		map.put(recommendedItem.getItemID(), rs.getInt(1));
//	    		index++;
//				}catch(SQLException e) {
//					e.printStackTrace();
//				}
//    	}
//    	//排序
//    	if(!map.isEmpty()){
//            List<Entry<Long, Integer>> entryArrayList = new ArrayList<Entry<Long, Integer>>(map.entrySet());
//            Collections.sort(entryArrayList, new Comparator<Entry<Long, Integer>>() {
//                public int compare(Entry<Long, Integer> o1, Entry<Long, Integer> o2) {
//    				// TODO Auto-generated method stub
//    				return o2.getValue()-o1.getValue();
//    			}
//            });
////        	System.out.println(entryArrayList.subList(0, 20));
//            if(entryArrayList.size()>20) {
// 				return entryArrayList.subList(0, requestnum);
// 			}
// 			else {
// 				return entryArrayList;
// 			}
//    	}
//    	else{
//    		System.out.println("map is null");
//    		return null;
//    	}
//    }
//	//得到基于物品的推荐
//	private void requstItemRecommend(long itemid) {
//		try {
//			System.out.println("recommending!");
//			recommendedItemList = itemRecommender.mostSimilarItems(itemid, recommendnum);
//			System.out.println("recommend commplete!");
//			if(!recommendedItemList.isEmpty()) {
//		      for (@SuppressWarnings("unused") RecommendedItem recommendedItem : recommendedItemList) {
//		    	  //System.out.println(recommendedItem);
//		      }
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	//得到基于用户的推荐
//	private void requstUserRecommend(long userid) {
//		try {
//			System.out.println("recommending!");
//			recommendedItemList = userRecommender.recommend(userid, recommendnum);
//			System.out.println("recommend commplete!");
//			if(!recommendedItemList.isEmpty()) {
//		      for (@SuppressWarnings("unused") RecommendedItem recommendedItem : recommendedItemList) {
//		    	  System.out.println(recommendedItem);
//		      }
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	//初始化数据模型
//	private void initModel() {
//		try {
//			model = new FileDataModel(new File(localname));
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	//初始化用于排序的数据库
//	private void initDatabase() {
//		dataSource=new MysqlDataSource();
//		dataSource.setUrl(sqlbase);
//		dataSource.setUser(user);
//		dataSource.setPassword(password);
//		try {
//			dataSource.setUseSSL(false);
//			connect = dataSource.getConnection();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	//初始化基于物品的推荐模型
//	private void initItemRecommender(){
//		try {
//			System.out.println("starting mysql datasource");
//			ItemSimilarity itemSimilarity =new PearsonCorrelationSimilarity(model);
//			itemRecommender = new GenericItemBasedRecommender(model, itemSimilarity);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	//初始化基于用户的推荐模型
//	private void initUserRecommender() {
//		try {
//			System.out.println("starting mysql datasource");
//			UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(model);
//			UserNeighborhood  neighborhood = new NearestNUserNeighborhood(2, userSimilarity, model);
//			userRecommender = new GenericUserBasedRecommender(model, neighborhood, userSimilarity);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//	//得到电影的评分
//	public float getMovieRating(long movieId) {
//		String sql = "SELECT avgrating from avgratings where movieId = " + movieId;
//			try {
//				Statement stmt = connect.createStatement();
//	 			ResultSet rs = stmt.executeQuery(sql);
//	 			if(rs.next()) {
//	 				return rs.getFloat("avgrating");
//	 			}
//	 			else {
//	 				return 0.0f;
//	 			}
//			}catch(SQLException e) {
//				e.printStackTrace();
//				return 0.0f;
//			}
//	}
//}
