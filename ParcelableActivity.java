package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableActivity implements Parcelable {

    public String name = "";
    public int hours;
    public int minuts;

    ParcelableActivity() {}
    protected ParcelableActivity(Parcel in) {
        name = in.readString();
        hours = in.readInt();
        minuts = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(hours);
        dest.writeInt(minuts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableActivity> CREATOR = new Creator<ParcelableActivity>() {
        @Override
        public ParcelableActivity createFromParcel(Parcel in) {
            return new ParcelableActivity(in);
        }

        @Override
        public ParcelableActivity[] newArray(int size) {
            return new ParcelableActivity[size];
        }
    };
}
