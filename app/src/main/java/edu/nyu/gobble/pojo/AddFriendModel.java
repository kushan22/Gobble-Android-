package edu.nyu.gobble.pojo;

public class AddFriendModel {
    private String fullName,userId;
    private boolean isSelected = false;

    public AddFriendModel(String fullName,String userId) {
        this.fullName = fullName;
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserId() {
        return userId;
    }



    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
