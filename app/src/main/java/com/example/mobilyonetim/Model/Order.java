package com.example.mobilyonetim.Model;
public class Order {

    private String ProduName;
    private String Quantity;
    private String Total;
    private String Adress;
    private String Phone;
    private String Status;

    public Order( String produName, String quantity, String total, String phone, String status) {
        ProduName = produName;
        Quantity = quantity;
        Total = total;
        Phone = phone;
        Status = status;
    }

    public String getProduName() {
        return ProduName;
    }

    public void setProduName(String produName) {
        ProduName = produName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }
    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Order() {

    }}
