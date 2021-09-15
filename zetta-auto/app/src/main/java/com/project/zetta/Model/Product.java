package com.project.zetta.Model;

public class Product {
    private String pname, description, price, image, quantity, pid, date, time, discount, origin, seller, status, warrenty, sold;

    public Product() {
    }

    public Product(String pname, String description, String price, String image, String quantity, String pid, String date, String time, String discount, String origin, String seller, String status, String warrenty, String sold) {
        this.pname = pname;
        this.description = description;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.discount = discount;
        this.origin = origin;
        this.seller = seller;
        this.status = status;
        this.warrenty = warrenty;
        this.sold = sold;
    }

    public String getPname() {
        return pname;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWarrenty() {
        return warrenty;
    }

    public void setWarrenty(String warrenty) {
        this.warrenty = warrenty;
    }
}
