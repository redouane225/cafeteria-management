package com.example.cafeteriamanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuItem implements Parcelable {
    private int id;
    private String name;
    private double price;
    private String isAvailable;
    private String categorie;  // New category variable

    public MenuItem(int id, String name, double price, String isAvailable, String categorie) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
        this.categorie = categorie;  // Initialize category
    }

    protected MenuItem(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readDouble();
        isAvailable = in.readString();
        categorie = in.readString();  // Read category from parcel
    }

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeString(isAvailable);
        parcel.writeString(categorie);  // Write category to parcel
    }
}