package com.siddhathatravels.siddhathatravels.Model;

import android.os.Parcel;
import android.os.Parcelable;


public class UserProfile implements Parcelable {

    public String studentName;
    public String fatherName;
    public String address;
    public String batch;
    public String stream;
    public String rollNo;
    public boolean isAdmin;
    public String studentPh;
    public String fatherPh;
    public String motherPh;
    public boolean isAvailingAC;
    public String availTransportDate;
    public String registeredPhone;
    public String createdAt;
    public String monthlyCharges;
    public UserProfile()
    {

    }

    protected UserProfile(Parcel in) {
        studentName = in.readString();
        fatherName = in.readString();
        address = in.readString();
        batch = in.readString();
        stream = in.readString();
        rollNo = in.readString();
        studentPh = in.readString();
        registeredPhone = in.readString();
        fatherPh = in.readString();
        motherPh = in.readString();
        availTransportDate = in.readString();
        isAdmin = in.readByte() != 0;
        isAvailingAC = in.readByte() != 0;
        createdAt = in.readString();
        monthlyCharges = in.readString();
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "studentName='" + studentName + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", address='" + address + '\'' +
                ", batch='" + batch + '\'' +
                ", stream='" + stream + '\'' +
                ", rollNo='" + rollNo + '\'' +
                ", isAdmin=" + isAdmin +
                ", studentPh='" + studentPh + '\'' +
                ", fatherPh='" + fatherPh + '\'' +
                ", motherPh='" + motherPh + '\'' +
                ", isAvailingAC=" + isAvailingAC +
                ", availTransportDate='" + availTransportDate + '\'' +
                ", registeredPhone='" + registeredPhone + '\'' +
                '}';
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(studentName);
            dest.writeString(fatherName);
            dest.writeString(address);
            dest.writeString(batch);
            dest.writeString(stream);
            dest.writeString(rollNo);
            dest.writeString(studentPh);
            dest.writeString(registeredPhone);
            dest.writeString(fatherPh);
            dest.writeString(motherPh);
            dest.writeString(availTransportDate);
            dest.writeByte((byte) (isAdmin ? 1 : 0));
            dest.writeByte((byte) (isAvailingAC ? 1 : 0));
            dest.writeString(createdAt);
            dest.writeString(monthlyCharges);
    }
}
