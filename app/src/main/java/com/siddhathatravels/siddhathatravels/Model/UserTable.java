package com.siddhathatravels.siddhathatravels.Model;

public class UserTable {

    public static final String TABLE_NAME = "notes";

    public static final String STUDENT_NAME = "name";
    public static final String FATHER_NAME = "fathername";
    public static final String ADDRESS = "address";
    public static final String BATCH = "batch";
    public static final String STREAM = "stream";
    public static final String ROLLNO = "rollno";
    public static final String ISADMIN = "isAdmin";
    public static final String STUDENTPH = "studentPhone";
    public static final String FATHERPH = "fatherPhone";
    public static final String MOTHERPH = "motherPhone";
    public static final String ISAVAILINGAC = "isAvailingAC";
    public static final String AVAILTRANSPORTDATE = "availTransportDate";
    public static final String REGISTEREDPHONE ="registeredPhone";
    public static final String CREATEDAT = "createdAt";
    private UserProfile userProfile;

    public UserTable() {

    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + STUDENT_NAME + " TEXT,"
                    + FATHER_NAME + " TEXT,"
                    + ADDRESS + " TEXT,"
                    + BATCH + " TEXT,"
                    + STREAM + " TEXT,"
                    + ROLLNO + " TEXT,"
                    + ISADMIN + " INTEGER DEFAULT 0,"
                    + STUDENTPH + " TEXT,"
                    + FATHERPH + " TEXT,"
                    + MOTHERPH + " TEXT,"
                    + REGISTEREDPHONE + " TEXT,"
                    + ISAVAILINGAC + " INTEGER DEFAULT 0,"
                    + AVAILTRANSPORTDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    +  CREATEDAT + " TEXT"
                    + ")";


}
