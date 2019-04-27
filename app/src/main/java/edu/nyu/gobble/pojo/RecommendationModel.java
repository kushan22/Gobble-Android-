package edu.nyu.gobble.pojo;

public class RecommendationModel {

    public String name,address;
    public int imageName;

    public int getImageName() {
        return imageName;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImageName(int imageName) {
        this.imageName = imageName;
    }

    public void setName(String name) {
        this.name = name;
    }
}
