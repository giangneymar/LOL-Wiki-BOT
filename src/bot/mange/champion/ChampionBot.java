package bot.mange.champion;

import bot.BaseBot;
import crawler.CrawlChampion;

import java.io.IOException;
import java.sql.SQLException;

public class ChampionBot extends BaseBot {

    public ChampionBot(int maxThread, long restTime) {
        super(maxThread, restTime);
        toolkit.appLogger.info("create");
    }

    public void getAllChampion() {
        executor.execute(() -> {
            CrawlChampion crawl = new CrawlChampion();
            try {
                toolkit.appLogger.info("start crawl");
                toolkit.appLogger.info("crawl allChampion");
                crawl.crawlAllChampion(
                        "https://leagueoflegends.fandom.com/wiki/League_of_Legends_Wiki",
                        "https://lol.garena.com/champions",
                        "https://mobalytics.gg/blog/lol-tier-list-for-climbing-solo-queue/");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void run() {
        super.run();
        toolkit.appLogger.info("prepare");
        getAllChampion();
        complete();
    }
}
