package com.example.sayhi;

public class ChatItem {

    private int personImage;

    private String personName;

    public ChatItem(int personImage, String personName) {
        this.personImage = personImage;
        this.personName = personName;
    }

    public int getPersonImage() {
        return personImage;
    }

    public void setPersonImage(int personImage) {
        this.personImage = personImage;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
