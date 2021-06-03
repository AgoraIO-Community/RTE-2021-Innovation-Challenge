package socketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketServerMain 
{
	public static Socket socket = null;
	public static ServerSocket serverSocket = null;
	public static boolean runtime = true;
	public static String str = "";
	public static ScheduledExecutorService service;
	public static Integer port = 1081;
	public static void main(String[] args){
    	try {
        	serverSocket = new ServerSocket(port);
        	socket = null;
            System.out.println("Wait For Connection");
            //多线程监听对服务端的指令
            Runnable runnable = new Runnable() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                public void run() {  
                    // task to run goes here 
                	try {
                		str = reader.readLine();
                		switch(str) {
                		case "quit":{
                			if(socket != null) {
                    			socket.close();
                    		}
                            serverSocket.close();
                            runtime = false;
                            service.shutdown();
                		}break;
                		case "port":{
                			System.out.print("请输入你要更改的端口号(当前端口：1080)：");
                			str = reader.readLine();
                			serverSocket = new ServerSocket(Integer.parseInt(str));
                    		System.out.println("端口已更改为：" + str);
                		}break;
                		}

                	}catch(Exception e) {
                		e.printStackTrace();
                		//System.out.println("当前端口已经正在使用！");
                	}
                }  
            };
            service = Executors.newSingleThreadScheduledExecutor();  
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间  
            service.scheduleAtFixedRate(runnable, 2, 1, TimeUnit.SECONDS);
            while (runtime) {
            	if(!runtime) {
            		System.out.println("服务器已退出！");
            		break;
            	}
            	socket = serverSocket.accept();
       		 	System.out.println("New Client ");
                SocketThread st = new SocketThread(socket);
                st.start();
                SenssionManager.getSenssionManager().add(st);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	SenssionManager.getSenssionManager().removeall();
        	System.out.println("服务器已退出！");
        }
    }
}

