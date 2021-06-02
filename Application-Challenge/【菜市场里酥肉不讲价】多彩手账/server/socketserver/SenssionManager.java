package socketserver;

import java.util.Vector;

public class SenssionManager {
	// 实现单例化
    private SenssionManager() {
    };
    private static final SenssionManager cm = new SenssionManager();
    public static SenssionManager getSenssionManager() {// 返回值为ChatManager
        return cm;
    }
    // 单例化完成
    Vector<SocketThread> vector = new Vector<SocketThread>();
 
    public void add(SocketThread st) {// 为当前集合添加SocketThread对象
        vector.add(st);
    }
     
    public void remove(SocketThread st) {// 当前客户端关闭连接
        vector.remove(st);
    }
     
    public void removeall() {// 关闭所有连接
        for (int i = 0; i < vector.size(); i++) {// 遍历所有的线程
            SocketThread csChatSocket = vector.get(i);
            if(csChatSocket!=null){
                vector.remove(csChatSocket);
            }
                 
        }
    }
 
    // 某一个线程向其他的客户端发送信息
    public void publish(SocketThread st, String out) {
        for (int i = 0; i < vector.size(); i++) {// 遍历所有的线程
            SocketThread csChatSocket = vector.get(i);
            if (csChatSocket != st)// 判断不是当前线程就发送此消息
                csChatSocket.out(out + "\n");
        }
    }
 
    // 向当前线程发信息
    public void publish_present(SocketThread st, String out) {
        st.out(out);
    }
}
