package com.example.firestore;

public class Note {

    String title, age, contactno, address;


    public Note() {
    }

    public Note(String title, String age, String contactno, String address) {
        this.title = title;
        this.age = age;
        this.contactno = contactno;
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
