package com.example.travelmate.data;

public class user_details {



    public String userId;
    public String userName;
 public String userProfile;
 public String userAge;
   public String userGender;
  public  String userOrigin;
    public String userSource;
    public String userDestination;
   public String userYear;
 public  String userEmail;
 public  String userTime;
 public  String userDate;
    public String userPhoneNumber;

   public String proctorName;
   public String proctorEmail;
   public String proctorPhoneNumber;
   public String fatherName;
   public String fatherEmail;
   public String fatherPhoneNumber;
   public String motherName;
   public String motherPhoneNumber;
   public String cabCarNumberPlate;
   public String cabDriverName;
   public String cabDriverNumber;


    public user_details() {
    }


    public user_details(String userId, String userName, String userProfile, String userAge,
                        String userGender, String userOrigin, String userSource, String userDestination,
                        String userYear, String userEmail, String userPhoneNumber
                       ,String proctorName, String proctorEmail, String proctorPhoneNumber,
                        String fatherName, String fatherEmail, String fatherPhoneNumber,
                        String motherName, String motherPhoneNumber, String cabCarNumberPlate,
                        String cabDriverName, String cabDriverNumber) {
        this.userId = userId;
        this.userName = userName;
        this.userProfile = userProfile;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userOrigin = userOrigin;
        this.userSource = userSource;
        this.userDestination = userDestination;
        this.userYear = userYear;
        this.userEmail = userEmail;
        this.userTime= userTime;
        this.userDate=userDate;
        this.userPhoneNumber=userPhoneNumber;

        this.proctorName = proctorName;
        this.proctorEmail = proctorEmail;
        this.proctorPhoneNumber = proctorPhoneNumber;
        this.fatherName = fatherName;
        this.fatherEmail = fatherEmail;
        this.fatherPhoneNumber = fatherPhoneNumber;
        this.motherName = motherName;
        this.motherPhoneNumber = motherPhoneNumber;
        this.cabCarNumberPlate = cabCarNumberPlate;
        this.cabDriverName = cabDriverName;
        this.cabDriverNumber = cabDriverNumber;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserOrigin() {
        return userOrigin;
    }

    public void setUserOrigin(String userOrigin) {
        this.userOrigin = userOrigin;
    }

    public String getUserSource() {
        return userSource;
    }

    public void setUserSource(String userSource) {
        this.userSource = userSource;
    }

    public String getUserDestination() {
        return userDestination;
    }

    public void setUserDestination(String userDestination) {
        this.userDestination = userDestination;
    }

    public String getUserYear() {
        return userYear;
    }

    public void setUserYear(String userYear) {
        this.userYear = userYear;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getProctorName() {
        return proctorName;
    }

    public void setProctorName(String proctorName) {
        this.proctorName = proctorName;
    }

    public String getProctorEmail() {
        return proctorEmail;
    }

    public void setProctorEmail(String proctorEmail) {
        this.proctorEmail = proctorEmail;
    }

    public String getProctorPhoneNumber() {
        return proctorPhoneNumber;
    }

    public void setProctorPhoneNumber(String proctorPhoneNumber) {
        this.proctorPhoneNumber = proctorPhoneNumber;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherEmail() {
        return fatherEmail;
    }

    public void setFatherEmail(String fatherEmail) {
        this.fatherEmail = fatherEmail;
    }

    public String getFatherPhoneNumber() {
        return fatherPhoneNumber;
    }

    public void setFatherPhoneNumber(String fatherPhoneNumber) {
        this.fatherPhoneNumber = fatherPhoneNumber;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherPhoneNumber() {
        return motherPhoneNumber;
    }

    public void setMotherPhoneNumber(String motherPhoneNumber) {
        this.motherPhoneNumber = motherPhoneNumber;
    }

    public String getCabCarNumberPlate() {
        return cabCarNumberPlate;
    }

    public void setCabCarNumberPlate(String cabCarNumberPlate) {
        this.cabCarNumberPlate = cabCarNumberPlate;
    }

    public String getCabDriverName() {
        return cabDriverName;
    }

    public void setCabDriverName(String cabDriverName) {
        this.cabDriverName = cabDriverName;
    }

    public String getCabDriverNumber() {
        return cabDriverNumber;
    }

    public void setCabDriverNumber(String cabDriverNumber) {
        this.cabDriverNumber = cabDriverNumber;
    }

    @Override
    public String toString() {
        return "user_details{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userProfile='" + userProfile + '\'' +
                ", userAge='" + userAge + '\'' +
                ", userGender='" + userGender + '\'' +
                ", userOrigin='" + userOrigin + '\'' +
                ", userSource='" + userSource + '\'' +
                ", userDestination='" + userDestination + '\'' +
                ", userYear='" + userYear + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userTime='" + userTime + '\'' +
                ", userDate='" + userDate + '\'' +
                ", userPhoneNumber='" + userPhoneNumber +'\''+
                ", proctorName='" + proctorName + '\'' +
                ", proctorEmail='" + proctorEmail + '\'' +
                ", proctorPhoneNumber='" + proctorPhoneNumber + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", fatherEmail='" + fatherEmail + '\'' +
                ", fatherPhoneNumber='" + fatherPhoneNumber + '\'' +
                ", motherName='" + motherName + '\'' +
                ", motherPhoneNumber='" + motherPhoneNumber + '\'' +
                ", cabCarNumberPlate='" + cabCarNumberPlate + '\'' +
                ", cabDriverName='" + cabDriverName + '\'' +
                ", cabDriverNumber='" + cabDriverNumber + '\'' +
                '}';


    }
//    public static user_details fromDocumentSnapshot(DocumentSnapshot document) {
//        return document.toObject(user_details.class);
//    }
}

