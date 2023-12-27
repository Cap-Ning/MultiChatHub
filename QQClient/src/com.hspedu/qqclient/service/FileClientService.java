package com.hspedu.qqclient.service;

import com.hspedu.qqcomment.Message;
import com.hspedu.qqcomment.MessageType;

import java.io.*;

//该类完成文件传输服务
public class FileClientService {

    /**
     * @param src      源文件
     * @param dest     把该文件传输到对方的哪个目录
     * @param senderId 发送者id
     * @param getterId 接收者id
     */
    public void sendFileToOne(String src, String dest, String senderId, String getterId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSrc(src);
        message.setDest(dest);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSendTime(new java.util.Date().toString());

        //读取文件
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int) new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);//将src文件写入到字节数组
            //将文件对应的字节数组设置message
            message.setFileBytes(fileBytes);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        //提示信息
        System.out.println("\n" + senderId + " 给 " + getterId + " 发送文件: " + src + " 到对方电脑的目录 " + dest);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(MangeClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
