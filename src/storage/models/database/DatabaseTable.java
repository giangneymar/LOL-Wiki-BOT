package storage.models.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatabaseTable {
    @SerializedName("wallpaper")
    @Expose
    public String wallpaper;

    @SerializedName("champion")
    @Expose
    public String champion;

    @SerializedName("champion_list")
    @Expose
    public String champion_list;
}
