package com.hspedu.qqclient.service;

import com.hspedu.qqcomment.Message;
import com.hspedu.qqcomment.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Date;

//该类，提供和消息相关的服务方法
public class MessageClientService {

    /**
     * @param content  内容
     * @param senderId 发送用户Id
     * @param getterId 接收用户Id
     */
    public void sendMessageToOne(String content, String senderId, String getterId) {
        //构建message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setContent(content);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSendTime(new Date().toString());
        System.out.println(senderId + " 对 " + getterId + " 说 " + content);

        try {
            //发送给服务端
            ObjectOutputStream oos =
                    new ObjectOutputStream(MangeClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param content 内容
     * @param senderId 发送者
     */
    public void sendMessageToAll(String content, String senderId) {
        /* 构建message */
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
        message.setContent(content);
        message.setSender(senderId);
        message.setSendTime(new Date().toString());

        System.out.println(senderId + " 对大家说 " + content);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(MangeClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
