package com.example.sayhi;

public class Information {
    public String name;
    public String email;
    public String check;
    public String verified;
    public String uid;



    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Information(String name, String email, String check,String verified, String uid) {
        this.name = name;
        this.email = email;
        this.check = check;
        this.verified = verified;
        this.uid = uid;
    }

    public Information() {
      //
    }
}
