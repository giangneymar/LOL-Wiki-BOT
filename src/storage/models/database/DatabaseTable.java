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

    @SerializedName("abilities")
    @Expose
    public String abilities;

    @SerializedName("item")
    @Expose
    public String item;

    @SerializedName("item_for_champion")
    @Expose
    public String item_for_champion;
}
