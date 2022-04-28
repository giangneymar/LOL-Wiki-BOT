package bot.lol.wallpaper;

import bot.BaseBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import storage.AppStorage;
import storage.models.database.Database;
import utils.AppLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WallpaperBot extends BaseBot {
    private AppLogger appLogger = AppLogger.getInstance();
    private AppStorage appStorage = AppStorage.getInstance();

    public WallpaperBot(int maxThread, long restTime) {
        super(maxThread, restTime);
        toolkit.appLogger.info("create");
    }

    @Override
    public void run() {
        super.run();
        toolkit.appLogger.info("prepare");
        getWallpaperFromWeb();
        complete();
    }

    public void getWallpaperFromWeb() {
        executor.execute(() -> {
            try {
                toolkit.appLogger.info("start crawl");
                toolkit.appLogger.info("crawl wallpaper");
                listWallpaperOnWeb("https://recmiennam.com/hinh-nen-lien-minh-huyen-thoai-cho-dien-thoai.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public void listWallpaperOnWeb(String pageURL) throws IOException {
        WallpaperBotDatabase wallpaperBotDatabase = new WallpaperBotDatabase();
        Database database = appStorage.config.database;
        Document document = Jsoup.connect(pageURL).get();
        List<String> wallpapers = new ArrayList<>();
        Elements e = document.getElementsByTag("img");
        for (int i = 0; i < e.size(); i++) {
            String url = e.get(i).absUrl("src");
            if (url.equals("")) {
                continue;
            }
            wallpapers.add(url);
        }
        appLogger.info(String.format("list wallpaper has %s item", wallpapers.size()));
        wallpaperBotDatabase.delete(database.table.wallpaper);
        wallpaperBotDatabase.insertWallpaper(database.table.wallpaper, wallpapers);
    }

}
