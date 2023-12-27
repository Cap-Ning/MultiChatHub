package com.hspedu.qqclient.service;

import com.hspedu.qqcomment.Message;
import com.hspedu.qqcomment.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientConnectServerThread extends Thread {

    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //因为Thread需要在后台和服务器通信，因此我们while循环
        while (true) {
            try {
                System.out.println("客户端线程, 等待读取从服务器端发送消息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                Message message = (Message) ois.readObject();

                //如果读取到的是 服务端返回的在线用户列表
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    //去除在线列表信息
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n========当前在线用户列表========");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println(onlineUsers[i]);

                    }

                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {

                    System.out.println("\n" + message.getSender() + " 对 " + message.getGetter() + " 说:" + message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)) {
                    System.out.println("\n" + message.getSender() + " 对大家说:" + message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    System.out.println("\n" + message.getSender() + " 给 " + message.getGetter()
                            + "发文件: " + message.getSrc() + " 到目录: " + message.getDest());
                    //取出message的文件字节数组，通过文件输出流写入磁盘
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n 保存文件成功~");
                } else {
                    System.out.println("是其他种类的message,暂时不处理");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    public Socket getSocket() {
        return socket;
    }
}
