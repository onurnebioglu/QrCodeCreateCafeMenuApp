package com.example.mobilyonetim.Model;

public class Detail {
    private String quantity;
    private String price;
    private String produName;




    public Detail() {
    }

    public Detail(String produName, String quantity, String price) {
        this.quantity = quantity;
        this.price=price;
        this.produName=produName;
    }



    public String getProduName() {
        return produName;
    }

    public void setProduName(String produName) {
        this.produName = produName;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
