package storage.models.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DatabaseTable {
    @SerializedName("wallpaper")
    @Expose
    public String wallpaper;
}
