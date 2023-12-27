package com.hspedu.qqclient.service;

import java.util.HashMap;

public class MangeClientConnectServerThread {

    //多个线程放入一个HashMap集合，key就是用户id, value就是线程
    private static HashMap<String,ClientConnectServerThread> hm = new HashMap<>();

    //将牧歌线程加入到集合
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread){
        hm.put(userId,clientConnectServerThread);
    }
    public static  ClientConnectServerThread getClientConnectServerThread(String userId){
        return hm.get(userId);

    }
}
