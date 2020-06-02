package com.ambgen.naijahookup.models;

public class ChatsModel {
    int adapterposition;
    String email;
    String isseen;
    String message;
    String receiverid;
    String referencetext;
    String senderid;
    String time;
    String type;
    String status;


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    long timestamp;

    public ChatsModel(int adapterposition, String email, String isseen, String message, String receiverid,
                      String referencetext, String senderid, String time, String type, long timestamp, String status) {
        this.adapterposition = adapterposition;
        this.email = email;
        this.isseen = isseen;
        this.message = message;
        this.receiverid = receiverid;
        this.referencetext = referencetext;
        this.senderid = senderid;
        this.time = time;
        this.type = type;
        this.timestamp = timestamp;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ChatsModel() {
    }

    public int getAdapterposition() {
        return adapterposition;
    }

    public void setAdapterposition(int adapterposition) {
        this.adapterposition = adapterposition;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsseen() {
        return isseen;
    }

    public void setIsseen(String isseen) {
        this.isseen = isseen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public String getReferencetext() {
        return referencetext;
    }

    public void setReferencetext(String referencetext) {
        this.referencetext = referencetext;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



//            chatMap.put("message", message);
//        chatMap.put("time", saveCurrentTime);
//        chatMap.put("type", type);
//        chatMap.put("senderId",FirebaseAuth.getInstance().getCurrentUser().getUid());
//        chatMap.put("receiverid",friendsId);
//        chatMap.put("isSeen", "false");
//        chatMap.put("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
//        chatMap.put("referenceText","none");
//        chatMap.put("adapterPosition",0);
//        chatMap.put("status","unsent");
//        chatMap.put("invertedTimeStamp", ServerValue.TIMESTAMP);
    public static final String MESSAGE="message";
    public static final String TIME="time";
    public static final String TYPE="type";
    public static final String SENDERID="senderid";
    public static final String RECEIVERID="receiverid";
    public static final String ISSEEN="isSeen";
    public static final String EMAIL="email";
    public static final String REFERENCETEXT="referencetext";
    public static final String ADAPTERPOSITION="adapterposition";
    public static final String STATUS="status";
    public static final String TIMESTAMP="timestamp";

    public static final String SENT="sent";
    public static final String UNSENT="unsent";
    public static final String SEEN="seen";


}
