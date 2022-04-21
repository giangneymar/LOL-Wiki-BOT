package storage.models.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Database {
    @SerializedName("domain")
    @Expose
    public String domain;
    @SerializedName("port")
    @Expose
    public Integer port;
    @SerializedName("schema")
    @Expose
    public String schema;
    @SerializedName("user")
    @Expose
    public String user;
    @SerializedName("pass")
    @Expose
    public String pass;
    @SerializedName("table")
    @Expose
    public DatabaseTable table;
}
