package bot;

import crawler.CrawlWallpaper;

import java.io.IOException;

public class WallpaperBot extends BaseBot {
    public WallpaperBot(int maxThread, long restTime) {
        super(maxThread, restTime);
        toolkit.appLogger.info("create");
    }

    public void getWallpaperFromWeb() {
        executor.execute(() -> {
            CrawlWallpaper crawl = new CrawlWallpaper();
            try {
                toolkit.appLogger.info("start crawl");
                toolkit.appLogger.info("crawl wallpaper");
                crawl.listWallpaperOnWeb("https://recmiennam.com/hinh-nen-lien-minh-huyen-thoai-cho-dien-thoai.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void run() {
        super.run();
        toolkit.appLogger.info("prepare");
        getWallpaperFromWeb();
        complete();
    }
}
