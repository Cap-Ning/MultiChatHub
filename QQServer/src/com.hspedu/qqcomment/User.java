package com.hspedu.qqcomment;

import java.io.Serializable;

public class User implements Serializable {


    //在对象序列化和反序列化过程中确保类版本的一致性，以避免因类版本不同导致的兼容性问题
    private static final long serialVersionUID = 1L;

    private String userId;
    private String passwd;

    public User() {
    }

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
