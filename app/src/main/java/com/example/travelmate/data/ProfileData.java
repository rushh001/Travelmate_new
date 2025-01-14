package com.example.travelmate.data;

public class ProfileData {

    private String userId;
    private String userName;
    private String userPhoneNumber;
    private String userOrigin;
    private String userYear;
    private String userGender;

    public ProfileData() {
        // Default constructor required for calls to DataSnapshot.getValue(UserDetails.class)
    }

    public ProfileData(String userId, String userName, String userPhoneNumber, String userOrigin, String userYear, String userGender) {
        this.userId = userId;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userOrigin = userOrigin;
        this.userYear = userYear;
        this.userGender = userGender;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserPhoneNumber() { return userPhoneNumber; }
    public void setUserPhoneNumber(String userPhoneNumber) { this.userPhoneNumber = userPhoneNumber; }

    public String getUserOrigin() { return userOrigin; }
    public void setUserOrigin(String userOrigin) { this.userOrigin = userOrigin; }

    public String getUserYear() { return userYear; }
    public void setUserYear(String userYear) { this.userYear = userYear; }

    public String getUserGender() { return userGender; }
    public void setUserGender(String userGender) { this.userGender = userGender; }
}
