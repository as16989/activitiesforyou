package com.example.as16989.sleep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SleepGetter {

    public List<Sleep> getSleep() {
        return sleep;
    }

    public SummaryShort getSummary() {
        return summary;
    }

    @SerializedName("sleep")
    @Expose
    public List<Sleep> sleep = null;
    @SerializedName("summary")
    @Expose
    public SummaryShort summary;

}

