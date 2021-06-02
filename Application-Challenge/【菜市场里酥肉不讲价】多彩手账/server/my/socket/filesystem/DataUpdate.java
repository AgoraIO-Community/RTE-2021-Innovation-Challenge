package my.socket.filesystem;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class DataUpdate {
	private static String filepath = "/home/ratings.csv";//本地测试：D:/Java codes/socketserver/src/ratings.csv
															//服务器测试：/home/ratings.csv
	public static void updateFile(long userId, long movieId, float rating, long timestamp) {
		 try {
			 	DecimalFormat df = new DecimalFormat("#.#");//输出保留一位小数
	            String content = userId + "," + movieId + "," + df.format(rating) + "," + timestamp + "\n";
	            FileWriter writer = new FileWriter(filepath, true);
	            writer.write(content);
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
}
