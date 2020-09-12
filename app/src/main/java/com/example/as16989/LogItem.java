package com.example.as16989;

public class LogItem {
    private int mImageResource;
    private String mHeading, mStart, mEnd;

    public LogItem(int imageResource, String heading, String start, String end) {
        mImageResource = imageResource;
        mHeading = heading;
        mStart = start;
        mEnd = end;
    }

    public int getImageResource() {
        return mImageResource;
    }
    public String getHeading() {
        return mHeading;
    }
    public String getStart() {
        return mStart;
    }
    public void setStart(String mStart) {
        this.mStart = mStart;
    }
    public String getEnd() {
        return mEnd;
    }
}
