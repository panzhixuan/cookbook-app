package com.example.cookbook.util;

public class User {
    private static User Userstance;
    private int userId;

    private String userEmail;

    private String userPassword;

    private int userTaste;

    private int userCuisine;

    private int userOccasion;

    private String userPhoto;

    private String userName;

    private String userAddress;

    private User(){ }

    public static User getInstance(){
        if(Userstance == null){
            Userstance=new User();
        }
        return Userstance;
    }

    public void init(int userId,String userEmail,String userPassword,int userTaste,int userCuisine,int userOccasion,String userPhoto,String userName,String userAddress){
        this.userId=userId;
        this.userEmail=userEmail;
        this.userPassword=userPassword;
        this.userTaste=userTaste;
        this.userCuisine=userCuisine;
        this.userOccasion=userOccasion;
        this.userPhoto=userPhoto;
        this.userName=userName;
        this.userAddress=userAddress;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserCuisine(int userCuisine) {
        this.userCuisine = userCuisine;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserOccasion(int userOccasion) {
        this.userOccasion = userOccasion;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setUserTaste(int userTaste) {
        this.userTaste = userTaste;
    }

    public int getUserId() {
        return userId;
    }

    public int getUserCuisine() {
        return userCuisine;
    }

    public int getUserOccasion() {
        return userOccasion;
    }

    public int getUserTaste() {
        return userTaste;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPhoto() {
        return userPhoto;
    }
}
