package com.cst2335.mend0152;

public class Message {

    String message;
    boolean isSend;
    long id;

    public Message(String message, boolean isSend, long id) {
        this.message = message;
        this.isSend = isSend;
        this.id = id;
    }

    public void update(String message, long id)
    {
        this.message = message;
        this.id = id;
    }

    public String getMessage(){
        return message;
    }

    public boolean getIsSend() {
        return isSend;
    }

    public long getId() {return id; }


}
