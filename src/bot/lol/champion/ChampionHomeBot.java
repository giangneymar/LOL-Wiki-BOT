package bot.lol.champion;

import bot.BaseBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import storage.AppStorage;
import storage.models.database.Database;
import utils.AppLogger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.TimerTask;

public class ChampionHomeBot extends BaseBot {
    private AppLogger appLogger = AppLogger.getInstance();
    private AppStorage appStorage = AppStorage.getInstance();

    public ChampionHomeBot(int maxThread, long restTime) {
        super(maxThread, restTime);
        toolkit.appLogger.info("create");
    }

    @Override
    public void run() {
        super.run();
        toolkit.appLogger.info("prepare");
//         getListChampion();
        complete();
    }

    public void getListChampion() {
        executor.execute(() -> {
            try {
                toolkit.appLogger.info("start crawl");
                toolkit.appLogger.info("crawl allChampion");
                crawlListChampion("https://www.leagueoflegends.com/en-us/champions/");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public void crawlListChampion(String link) throws IOException, SQLException {
        ChampionBotDatabase championBotDatabase = new ChampionBotDatabase();
        Database database = appStorage.config.database;
        championBotDatabase.delete(database.table.champion);
        String imageChamp, name;
        int idChampion;

        Document document = Jsoup.connect(link).get();
        Elements elementsListChampOfLink = document.select("div.style__List-sc-13btjky-2.dLJiol a");
        int i = 0;
        for (Element elementsChampOfLink : elementsListChampOfLink) {
            idChampion = i;

            appLogger.info(String.format("get image champion %s", i));
            Elements elementsImageChamp = elementsChampOfLink.getElementsByTag("img");
            imageChamp = elementsImageChamp.first().absUrl("src");

            appLogger.info(String.format("get name champion %s", i));
            Elements elementsNameChampion = elementsChampOfLink.select("span.style__Text-n3ovyt-3.gMLOLF");
            name = elementsNameChampion.first().text();
            championBotDatabase.insertListChampion(database.table.champion, idChampion, name, imageChamp);
            i++;
        }
        appLogger.info(String.format("list champion has %s item", i));
    }

}
