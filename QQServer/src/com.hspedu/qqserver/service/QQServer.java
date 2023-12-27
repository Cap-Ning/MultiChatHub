package com.hspedu.qqserver.service;


import com.hspedu.qqcomment.Message;
import com.hspedu.qqcomment.MessageType;
import com.hspedu.qqcomment.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

//这是服务端，监听9999端口，等待客户端的连接并保持通讯
public class QQServer implements Serializable {

    private ServerSocket ss = null;

    //创建一个集合存放多个用户，如果是这些用户登录就认为是合法的
    //这里也可以用  ConcurrentHashMap,可以处理并发的集合，没有线程安全
    //HashMap 没有处理线程安全，因此在多线程情况下是不安全的
    //ConcurrentHashMap处理的线程安全，即线程同步处理，在多线程的情况下是安全的。

    private static HashMap<String,User> validUsers = new HashMap<>();
    static {
        validUsers.put("冯晓宁", new User("冯晓宁","123456"));
        validUsers.put("王元昊", new User("王元昊","123456"));
        validUsers.put("孙光照", new User("孙光照","123456"));
        validUsers.put("许衍胜", new User("许衍胜","123456"));
        validUsers.put("张国栋", new User("张国栋","123456"));
        validUsers.put("于海波", new User("于海波","123456"));


    }

    //验证用户是否有效的方法
    private boolean checkUser(String userId,String passwd){

        User user = validUsers.get(userId);
        //过关的验证方式
        if(user == null){
            return false;
        }
        if(!user.getPasswd().equals(passwd)){
            return false;
        }
        return true;
    }

    public QQServer() {

        try {
            System.out.println("服务器在9999端口监听...");
            //启动推送新闻的进程
            new Thread(new SendNewsToAllService()).start();
            ss = new ServerSocket(9999);

            while (true){ //当和某个客户端连接后，会继续监听，因为不止一个客户端，因此是while
                Socket socket = ss.accept();
                //得到socket关联的输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                //得到socket关联的输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                User u = (User)ois.readObject();
                //创建一个Message对象
                Message message = new Message();
                //验证
                if(checkUser(u.getUserId(),u.getPasswd())){//登陆成功
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //将message对象回复给客户端
                    oos.writeObject(message);
                    //创建一个线程，和客户端保持通信，该线程需要持有socket对象

                    ServerConnectClientThread serverConnectClientThread =
                            new ServerConnectClientThread(socket, u.getUserId());
                    //启动该线程
                    serverConnectClientThread.start();
                    //把该线程对象，放入到一个集合中，统一管理。
                    ManageClientThreads.addClientThread(u.getUserId(),serverConnectClientThread);
                }else{//登陆失败
                    System.out.println("用户 id=" + u.getUserId() + " pwd="+u.getPasswd()+" 验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    //关闭socket
                    socket.close();

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //如果服务端退出了while循环，说明服务器端不在监听，因此需要关闭ServerSocket
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
