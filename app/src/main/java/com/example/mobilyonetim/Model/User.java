package com.example.mobilyonetim.Model;
public class User {

    private String Name;
    private String Password;
    private String Phone;
    private String IsStaff;
    private int Adress;

    public User() {

    }
    public User(String name, String password, String phone, String isStaff, int adress) {
        Name = name;
        Password = password;
        Phone = phone;
        IsStaff = isStaff;
        Adress = adress;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public String getPhone() {
        return Phone;
    }
    public void setPhone(String phone) {
        Phone = phone;
    }
    public String getIsStaff() {
        return IsStaff;
    }
    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
    public int getAdress() {
        return Adress;
    }
    public void setAdress(int adress) {
        Adress = adress;
    }

}
