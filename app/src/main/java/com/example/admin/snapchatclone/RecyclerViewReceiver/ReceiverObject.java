package com.example.admin.snapchatclone.RecyclerViewReceiver;

public class ReceiverObject {
    private String email;
    private String uid;
    private boolean receive;

    public ReceiverObject(String email, String uid,Boolean receive){
        this.email=email;
        this.uid=uid;
        this.receive=receive;
    }

    public boolean isReceive() {
        return receive;
    }

    public void setReceive(boolean receive) {
        this.receive = receive;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
