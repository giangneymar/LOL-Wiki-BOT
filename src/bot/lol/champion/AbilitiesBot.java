package bot.lol.champion;

import bot.BaseBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import storage.AppStorage;
import storage.models.database.Database;
import utils.AppLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class AbilitiesBot extends BaseBot {
    private AppLogger appLogger = AppLogger.getInstance();
    private AppStorage appStorage = AppStorage.getInstance();

    public AbilitiesBot(int maxThread, long restTime) {
        super(maxThread, restTime);
        toolkit.appLogger.info("create");
    }

    @Override
    public void run() {
        super.run();
        toolkit.appLogger.info("prepare");
        getAllAbilities();
        complete();
    }

    public void getAllAbilities() {
        executor.execute(() -> {
            try {
                toolkit.appLogger.info("start crawl");
                toolkit.appLogger.info("crawl allChampion");
                crawlAbilitiesChamp("https://www.mobafire.com/league-of-legends/champions");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void crawlAbilitiesChamp(String link) throws IOException {
        ChampionBotDatabase championBotDatabase = new ChampionBotDatabase();
        Database database = appStorage.config.database;
        championBotDatabase.delete(database.table.abilities);
        String name, range, cost, coolDown, description, image, key;
        List<String> listLinkChamp = new ArrayList<>();
        int idChampion;
        int index = 0;

        Document document = Jsoup.connect(link).get();
        Elements elementsListChamp = document.select("div.champ-list.champ-list--details a");
        for (int i = 0; i < elementsListChamp.size(); i++) {
            String linkChamp = elementsListChamp.get(i).absUrl("href");
            listLinkChamp.add(linkChamp);
        }
        Collections.sort(listLinkChamp, String::compareTo);
        for (int i = 0; i < listLinkChamp.size(); i++) {
            index++;
            document = Jsoup.connect(listLinkChamp.get(i)).get();
            Elements elementsItemAbilities = document.select("a.champ__abilities__item");
            for (int j = 0; j < elementsItemAbilities.size(); j++) {
                idChampion = i;

                appLogger.info("get name abilities");
                Elements elementsNameChamp = document.select("div.champ__head");
                String nameChamp = elementsNameChamp.first().text();
                Elements elementsNameAbilities = elementsItemAbilities.get(j).select("div.champ__abilities__item__name");
                String nameString = splitNameAbilities(elementsNameAbilities.text(), splitNameChamp(nameChamp));
                if (!nameString.isEmpty()) {
                    name = nameString;
                } else {
                    continue;
                }

                appLogger.info("get range abilities");
                Elements elementsRangeAbilities = elementsItemAbilities.get(j).select("div.champ__abilities__item__range");
                String rangeString = elementsRangeAbilities.text();
                if (!rangeString.isEmpty()) {
                    range = rangeString;
                } else {
                    range = "updating";
                }

                appLogger.info("get cost abilities");
                Elements elementsCostAbilities = elementsItemAbilities.get(j).select("div.champ__abilities__item__cost");
                String costString = elementsCostAbilities.text();
                if (!costString.isEmpty()) {
                    cost = costString;
                } else {
                    cost = "0";
                }

                appLogger.info("get coolDown abilities");
                Elements elementsCoolDownAbilities = elementsItemAbilities.get(j).select("div.champ__abilities__item__cooldown");
                String coolDownString = elementsCoolDownAbilities.text();
                if (!coolDownString.isEmpty()) {
                    coolDown = coolDownString;
                } else {
                    coolDown = "0";
                }

                appLogger.info("get image abilities");
                Elements elementsImageAbilities = elementsItemAbilities.get(j).select("div.champ__abilities__item__pic img");
                image = "";
                if (elementsImageAbilities.first() != null) {
                    String imageString = elementsImageAbilities.first().absUrl("data-original");
                    if (!imageString.isEmpty()) {
                        image = imageString;
                    } else {
                        image = "updating";
                    }
                }

                appLogger.info("get description abilities");
                Elements elementsDescriptionAbilities = elementsItemAbilities.get(j).select("div.champ__abilities__item__desc");
                String descriptionString = elementsDescriptionAbilities.text();
                if (!descriptionString.isEmpty()) {
                    description = descriptionString;
                } else {
                    description = "updating";
                }

                appLogger.info("get key abilities");
                Elements elementsKeyAbilities = elementsItemAbilities.get(j).select("div.champ__abilities__item__name span em");
                key = "";
                if (elementsKeyAbilities.first() != null) {
                    String keyString = elementsKeyAbilities.first().text();
                    if (!keyString.isEmpty()) {
                        key = keyString;
                    } else {
                        key = "updating";
                    }
                }
                championBotDatabase.insertAllAbilities(database.table.abilities, idChampion, name, range, cost, coolDown, image, description, key);
            }
        }
        appLogger.info(String.format("list abilities has %s item", index));
    }

    private String splitNameAbilities(String s, String name) {
        StringBuilder kq = new StringBuilder();
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            String a = st.nextToken();
            if (!a.contains(name)) {
                kq.append(a);
                kq.append(" ");
            } else {
                break;
            }
        }
        return kq.toString().trim();
    }

    private String splitNameChamp(String s) {
        StringBuilder kq = new StringBuilder();
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            kq.append(st.nextToken());
            kq.append(" ");
            break;
        }
        return kq.toString().trim();
    }

}
