package com.example.as16989.sleep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sleep {

    public String getDateOfSleep() {
        return dateOfSleep;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getEfficiency() {
        return efficiency;
    }

    public String getEndTime() {
        return endTime;
    }

    public Integer getInfoCode() {
        return infoCode;
    }

    public Boolean getMainSleep() {
        return isMainSleep;
    }

    public Levels getLevels() {
        return levels;
    }

    public Integer getLogId() {
        return logId;
    }

    public Integer getMinutesAfterWakeup() {
        return minutesAfterWakeup;
    }

    public Integer getMinutesAsleep() {
        return minutesAsleep;
    }

    public Integer getMinutesAwake() {
        return minutesAwake;
    }

    public Integer getMinutesToFallAsleep() {
        return minutesToFallAsleep;
    }

    public String getStartTime() {
        return startTime;
    }

    public Integer getTimeInBed() {
        return timeInBed;
    }

    public String getType() {
        return type;
    }

    @SerializedName("dateOfSleep")
    @Expose
    public String dateOfSleep;
    @SerializedName("duration")
    @Expose
    public Integer duration;
    @SerializedName("efficiency")
    @Expose
    public Integer efficiency;
    @SerializedName("endTime")
    @Expose
    public String endTime;
    @SerializedName("infoCode")
    @Expose
    public Integer infoCode;
    @SerializedName("isMainSleep")
    @Expose
    public Boolean isMainSleep;
    @SerializedName("levels")
    @Expose
    public Levels levels;
    @SerializedName("logId")
    @Expose
    public Integer logId;
    @SerializedName("minutesAfterWakeup")
    @Expose
    public Integer minutesAfterWakeup;
    @SerializedName("minutesAsleep")
    @Expose
    public Integer minutesAsleep;
    @SerializedName("minutesAwake")
    @Expose
    public Integer minutesAwake;
    @SerializedName("minutesToFallAsleep")
    @Expose
    public Integer minutesToFallAsleep;
    @SerializedName("startTime")
    @Expose
    public String startTime;
    @SerializedName("timeInBed")
    @Expose
    public Integer timeInBed;
    @SerializedName("type")
    @Expose
    public String type;

}