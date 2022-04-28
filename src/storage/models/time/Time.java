package storage.models.time;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Time {
    @SerializedName("maxThread")
    @Expose
    public int maxThread;
    @SerializedName("restTime")
    @Expose
    public long restTime;
}
