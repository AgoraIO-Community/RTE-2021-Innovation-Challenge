//package my.socket.server;
//
//import java.util.Calendar;
//import java.util.List;
//import java.util.Map.Entry;
//
//import my.socket.mysql.*;
//import my.socket.utils.*;
//
//
//public class MovieRecommendManager {
//	private static int recommendnum = 100;	//推荐列表数量
//	private static int requestnum = 50;		//推荐请求数量
//
//	public static String getResultString(String message) {
//		//JSONObject jObject1=new JSONObject(json1);
//		String[] info = message.split("-");
//        String finalstr = "{\"length\":0}";
//        Calendar calendar = Calendar.getInstance();
//        System.out.println("客户端消息：" + message + " 当前时间：" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1)
//                + "-" + calendar.get(Calendar.DATE) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE)
//                + ":" + calendar.get(Calendar.SECOND));
//        try {
//        	switch(info[0]) {
//        	case Protocol.GETRECOMMEND:{finalstr = itemRecommend(info[1]);}break;	//物品推荐
//        	case Protocol.GETSORTRECOMMEND:{finalstr = sortRecommend(info[1]);}break;		//标签推荐
//            case Protocol.GETUSERRECOMMEND:{finalstr = userRecommend(info[1]);}break;	//用户推荐
//            case Protocol.SEARCH:{finalstr = search(info[1]);}break;				//搜索
//
//            case Protocol.LOGIN:{finalstr = login(info[1]);}break;					//登录
//
//            case Protocol.REGISTER:{finalstr = register(info[1],info[2]);}break;			//注册
//            case Protocol.LOGOUT:{finalstr = logout(info[1]);}break;			//注销
//            case Protocol.SETCOMMENT:{finalstr = setComment(info[1]);}break;		//设置用户评论
//            case Protocol.GETCOMMENT:{finalstr = getComment(info[1]);}break;		//物品推荐
//            case Protocol.SORTMOVIE:{finalstr = sortMovie(info[1]);}break;				//电影分类
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(finalstr);
//        return finalstr;
//	}
//    //登录代码
//	public static String login(String info) {
//		String[] userinfo = info.split("\\|");
//		return UserOpration.getUserOprator().longin(userinfo[0], userinfo[1]);
//	}
//	//注册代码
//	public static String register(String info,String tags) {
//		String[] userinfo = info.split("\\|");
//		String[] usertags = tags.split("\\|");
//		return UserOpration.getUserOprator().register(userinfo[0], userinfo[1],usertags);
//	}
//	//注销
//	public static String logout(String info) {
//		return UserOpration.getUserOprator().logout(info);
//	}
//	//基于用户的推荐
//	public static String userRecommend(String info) {
//		//得到传回字符串
//        String jsonstr = "{\"length\":0}";
//		if(UserOpration.getUserOprator().isOnline(info)) {
//			if(UserOpration.getUserOprator().rated(info)) {
//				List<Entry<Long, Float>> item = RecommendFactory.getRecommendFactory()
//						.getUserRecommendList(UserOpration.getUserOprator().getUserId(info), recommendnum, requestnum);
//				jsonstr = getRecommendStr(item);
//			}
//			else {
//				jsonstr = "{\"length\":0}";
//			}
//		}
//		else {
//			jsonstr = "{\"length\":0}";
//		}
//		return jsonstr;
//	}
//	//根据标签的推荐
//	public static String sortRecommend(String info) {
//		List<Entry<Long, Float>> item = RecommendFactory.getRecommendFactory().getSortRecommndList(info, recommendnum, requestnum);
//		return getRecommendStr(item);
//	}
//	//电影分类
//	public static String sortMovie(String info) {
//		String[] m = info.split("\\|");
//		String tag = m[0];
//		int page = Integer.parseInt(m[1]);
//		return Movie.getTagMovieId(tag, page);
//	}
//	//得到用户评论
//	public static String getComment(String info) {
//		String str = CommentManager.getComment(info);
//		return str;
//	}
//	//写入用户评论
//	public static String setComment(String info) {
//		String str = CommentManager.setComment(info);
//		return str;
//	}
//	//基于物品推荐
//    public static String itemRecommend(String info) {
//    	//获取电影id
//        long[] movieid = Movie.getMovieId(info);
//        //得到传回字符串
//        String jsonstr = Movie.getJSONstr(movieid, recommendnum, requestnum);
//        return jsonstr;
//    }
//    //搜索函数
//    public static String search(String info) {
//    	//获取电影id
//        long[] movieid = Movie.getMovieId(info);
//        //得到电影标签
//        String[] tags = Movie.getMovieTags(movieid);
//        //得到传回字符串
//        String jsonstr = Movie.getJSONstr(movieid, tags);
//        return jsonstr;
//    }
//    //得到推荐电影的字符串
//    public static String getRecommendStr(List<Entry<Long, Float>> item) {
//    	String jsonstr = "{\"length\":" + requestnum;
//    	if(item!=null) {
//        	int size = requestnum;
//        	if(item.size()<requestnum) {
//        		size = item.size();
//        	}
//        	jsonstr = "{\"length\":" + size;
//        	long[] idnum = new long[size];
//        	//要用的
//        	for(int i = 0;i < size;i++) {
//    			idnum[i] = item.get(i).getKey();
//    		}
//        	//这里把id转换为名称
//        	Name_Id moviename = Movie.getMovieName(idnum);
//        	String[] tags = Movie.getMovieTags(idnum);
//        	if(moviename != null) {
//        		jsonstr += ",\"data\":[";
//            	for(int i = 0;i<size;i++) {
//            		//生成json字符串
//            		String[] tag = tags[i].split("\\|");
//            		for(int j = 0;j<tag.length;j++) {
//                		tag[j] = MovieTags.translate(tag[j], Protocol.ENTOCH);
//             		}
//            		jsonstr += "{\"name\":\"" + moviename.name[i] + "\",\"rating\":" + item.get(i).getValue()
//            				+ ",\"id\":" + moviename.id[i] + ",\"tags\":[";
//            		for(int j = 0;j<tag.length;j++) {
//            			jsonstr += "\"" + tag[j] + "\"";
//            			if(j + 1 == tag.length) {
//            				break;
//            			}
//            			jsonstr += ",";
//            		}
//            		jsonstr += "]}";
//            		if(i + 1 == size) {
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
//    }
//}
