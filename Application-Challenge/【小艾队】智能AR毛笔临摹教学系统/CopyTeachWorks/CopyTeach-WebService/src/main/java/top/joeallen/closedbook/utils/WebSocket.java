package top.joeallen.closedbook.utils;
 
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
 
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

 
/**
 *
 * @author JoeAllen
 *
 */
 
@Slf4j
@Component
@ServerEndpoint("/websocket/{name}")
public class WebSocket {
 
    /**
     *  与某个客户端的连接对话，需要通过它来给客户端发送消息
     */
    private Session session;
 
     /**
     * 标识当前连接客户端的用户名
     */
    private String name;
 
    /**
     *  用于存所有的连接服务的客户端，这个对象存储是安全的
     */
    private static ConcurrentHashMap<String,WebSocket> webSocketSet = new ConcurrentHashMap<>();
 
 
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "name") String name){
        this.session = session;
        this.name = name;
        // name是用来表示唯一客户端，如果需要指定发送，需要指定发送通过name来区分
        webSocketSet.put(name,this);
        log.info("[WebSocket] 连接成功，当前连接人数为：={}",webSocketSet.size());
    }
 
 
    @OnClose
    public void onClose(){
        webSocketSet.remove(this.name);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}",webSocketSet.size());
    }
 
    @OnMessage
    public void onMessage(String message){
        log.info("[WebSocket] 收到消息：{}",message);
        //判断是否需要指定发送，具体规则自定义
        if(message.indexOf("TOUSER") == 0){
            String name = message.substring(message.indexOf("TOUSER")+6,message.indexOf(";"));
            appointSending(name,message.substring(message.indexOf(";")+1));
        }else{
            groupSending(message);
        }
 
    }
 
    /**
     * 群发
     * @param message
     */
    public void groupSending(String message){
        for (String name : webSocketSet.keySet()){
            try {
                webSocketSet.get(name).session.getBasicRemote().sendText(message);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
 
    /**
     * 指定发送
     * @param name
     * @param message
     */
    public void appointSending(String name, String message){
        try {
            webSocketSet.get(name).session.getBasicRemote().sendText(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}