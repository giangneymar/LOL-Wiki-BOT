import bot.mange.wallpaper.WallpaperBotDatabase;
import com.google.gson.Gson;
import spark.Route;
import spark.Spark;
import storage.models.Wallpaper;

import java.util.ArrayList;

import static spark.Spark.*;

public class Server {
    public void API() {
        Spark.init();

        get("/LOLWiki/wallpaper", allWallpaper);
    }

    private Route allWallpaper = (request, response) -> {
        WallpaperBotDatabase database = new WallpaperBotDatabase();
        ArrayList<Wallpaper> wallpapers;
        wallpapers = database.getAllWallpaper();
        String json = (new Gson()).toJson(wallpapers);
        System.out.println("aaaaa");
        return json;
    };
}
