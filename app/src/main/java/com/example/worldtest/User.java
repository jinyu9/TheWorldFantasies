package com.example.worldtest;

public class User {
    private String name;            //用户名
    private String password;        //密码
    private String sex;             //性别
    private String number;          //手机号
    public User(String name, String password, String sex, String number) {
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.number = number;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", number'" + number + '\'' +
                '}';
    }
}

