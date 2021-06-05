package socketserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import my.socket.server.*;
import my.socket.utils.Log;
import my.socket.utils.Protocol;

/**
 * SocketThread实现多线程通信
 *
 * @author Administrator
 *
 */

public class SocketThread extends Thread {
    ServerSocket serverSocket = null;
    Socket socket = null;
    boolean online = false;
    State socketState = State.ReceiveMessage;


    public enum State
    {
        ReceiveFile,
        SendFile,
        ReceiveMessage
    }

    public SocketThread(ServerSocket serverSocket,Socket socket) {
        super();
        this.serverSocket = serverSocket;
        this.socket = socket;
        this.online = true;
        socketState = State.ReceiveMessage;
    }
    public SocketThread(Socket socket) {
        super();
        this.socket = socket;
        this.online = true;
    }
    public void out(String out) {
        try {
            socket.getOutputStream().write(out.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //给客户端发送消息
    public void publish(String message){
        System.out.println("Get Message:"+message);
        String str = DataProcess.getReturnString(message);
        System.out.println("Return Message:"+str);
        if(str.equals(Protocol.SEND_FILE))
            socketState = State.ReceiveFile;
        SenssionManager.getSenssionManager().publish_present(this, str + "\n");
    }
    //循环监听客户端消息
    @Override
    public void run() {
        // TODO Auto-generated method stub
        BufferedReader socketIn = null;
        PrintWriter socketOut = null;
        DataInputStream dis = null;
        String inMess = null;
        String savePath = "./";

        try {

            InputStream is = socket.getInputStream();
            socketIn = new BufferedReader(new InputStreamReader(is));
            socketOut = new PrintWriter(socket.getOutputStream());
            while (this.online) {
                if(socketState == State.ReceiveMessage)
                {
                    inMess = socketIn.readLine();   //客户端数据
                    if(inMess != null) {

                        publish(inMess);
                    }
                }
                else if(socketState == State.ReceiveFile)
                {
                    try
                    {
                        if(DataProcess.savePath == null)
                            socketState = State.ReceiveMessage;
                        savePath = DataProcess.savePath;
                        if(dis == null)
                            dis = new DataInputStream(new BufferedInputStream(is));
                        int size = 1024;

                        byte[] buf = new byte[size];
                        int passedlen = 0;
                        long len = 0;

                        savePath += dis.readUTF();
                        DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(savePath)));
                        len = dis.readLong();
                        System.out.println("len:" + len +" KB");
                        System.out.println("Start Receiving!");
                        int read = 0;
                        while( (read = dis.read(buf)) > 0){
                            passedlen += read;
                            System.out.println("Has received :" + passedlen * 100 / len + "%");
                            fileOut.write(buf, 0, read);
                            //fileOut.flush();
                            if((passedlen * 100 / len) == 100)
                                break;
                        }

                        socketState = State.ReceiveMessage;
                        System.out.println("Received Finshed..." + savePath);
                        SenssionManager.getSenssionManager().publish_present(this, Protocol.SEND_SUCCESS + "\n");
                        System.out.println("Return Message:"+Protocol.SEND_SUCCESS);

                        fileOut.close();
                        //dis.close();
                    }
                    catch (IOException e)
                    {
                        Log.print(e.getMessage());
                    }

                }
                if("bye".equals(inMess)){
                    SenssionManager.getSenssionManager().remove(this);
                    socketOut.close();
                    socketIn.close();
                    socket.close();
                    System.out.println("End Session");
                    this.online = false;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            SenssionManager.getSenssionManager().remove(this);
            try {
                socketOut.close();
                socketIn.close();
                socket.close();
            }catch(IOException e2) {
                e2.printStackTrace();
            }
            System.out.println("End Session:"+e.getMessage());
            this.online = false;
            e.printStackTrace();
        }

    }

}
