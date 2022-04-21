package storage.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import storage.models.database.Database;
import storage.models.logger.Logger;
import storage.models.setting.Setting;

public class Config {
    @SerializedName("database")
    @Expose
    public Database database;
    @SerializedName("setting")
    @Expose
    public Setting setting;
    @SerializedName("logger")
    @Expose
    public Logger logger;
}
