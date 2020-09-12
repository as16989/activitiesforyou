package com.example.as16989;

public class CurrentActivityItem {

    private int mImageResource;
    private String mHeading, mDesc, mStart;

    public CurrentActivityItem(int imageResource, String heading, String desc, String start) {
        mImageResource = imageResource;
        mHeading = heading;
        mDesc = desc;
        mStart = start;
    }

    public int getImageResource() { return mImageResource; }
    public String getHeading() { return mHeading; }
    public String getDesc() { return mDesc; }
    public String getStart() { return mStart; }
    public void setStart(String mStart) { this.mStart = mStart; }

}
