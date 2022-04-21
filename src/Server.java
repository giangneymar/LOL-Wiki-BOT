import bot.WallpaperDatabase;
import com.google.gson.Gson;
import spark.Route;
import spark.Spark;
import storage.models.Wallpaper;

import java.util.ArrayList;

import static spark.Spark.*;

public class Server {
    public void API() {
        Spark.init();

        get("/wallpaper", allWallpaper);
    }

    private Route allWallpaper = (request, response) -> {
        WallpaperDatabase database = new WallpaperDatabase();
        ArrayList<Wallpaper> wallpapers;
        wallpapers = database.getAllWallpaper();
        String json = (new Gson()).toJson(wallpapers);
        return json;
    };
}
