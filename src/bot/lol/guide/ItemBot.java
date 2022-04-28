package bot.lol.guide;

import bot.BaseBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import storage.models.database.Database;

import java.io.IOException;
import java.util.*;

public class ItemBot extends BaseBot {

    public ItemBot(int maxThread, long restTime) {
        super(maxThread, restTime, 2);
        toolkit.appLogger.info("create");
    }

    @Override
    public void run() {
        super.run();
        toolkit.appLogger.info("prepare");
        getAllItemForChampion();
        complete();
    }

    public void getAllItemForChampion() {
        executor.execute(() -> {
            toolkit.appLogger.info("start crawl");
            toolkit.appLogger.info("crawl equipment for champion");
            try {
                crawlItem("https://www.mobafire.com/league-of-legends/items");
                crawlItemForChampion("https://www.mobafire.com/league-of-legends/champions");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void crawlItem(String url) throws IOException {
        ItemBotDatabase itemBotDatabase = new ItemBotDatabase();
        Database database = toolkit.appStorage.config.database;
        itemBotDatabase.delete(database.table.item);

        int id = -1;
        String name = "", totalPrice = "", recipePrice = "", sellPrice = "", image = "", desc = "";
        ArrayList<String> listLinkItem = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements elementsListItem = document.select("div.self-clear.item-list.item-list--list a");
        for (Element element : elementsListItem) {
            String linkItem = element.absUrl("href");
            listLinkItem.add(linkItem);
            document = Jsoup.connect(linkItem).get();
            Elements elementsName = document.select("div.mf-responsive__leftCol #view-title");
            name = elementsName.text();

            Elements elementsPrice = document.select("div.mf-responsive__leftCol #view-title span");
            if (elementsPrice.size() == 2) {
                totalPrice = elementsPrice.get(0).text();
                recipePrice = "0";
                sellPrice = elementsPrice.get(1).text();
            } else if (elementsPrice.size() == 3) {
                totalPrice = elementsPrice.get(0).text();
                recipePrice = elementsPrice.get(1).text();
                sellPrice = elementsPrice.get(2).text();
            }

            Elements elementsImage = document.select("div.mf-responsive__leftCol .self-clear #view-item img");
            image = elementsImage.first().absUrl("src");

            Elements elementsDesc = document.select("div .item-info.float-left");
            desc = elementsDesc.text();

            id++;
            itemBotDatabase.insertItem(database.table.item, id, splitNameEquipment(name), totalPrice, recipePrice, sellPrice, image, desc);
        }
    }


    public void crawlItemForChampion(String url) throws IOException {
        ItemBotDatabase itemBotDatabase = new ItemBotDatabase();
        Database database = toolkit.appStorage.config.database;
        itemBotDatabase.delete(database.table.item_for_champion);

        int championListId, itemId;
        String name = "";

        Document document = Jsoup.connect(url).get();
        Elements elementsListLinkChampion = document.select("div.champ-list.champ-list--details a");
        ArrayList<String> listLinkChampion = new ArrayList<>();
        for (Element element : elementsListLinkChampion) {
            String linkChamp = element.absUrl("href");
            listLinkChampion.add(linkChamp);
        }
        Collections.sort(listLinkChampion);
        for (int i = 0; i < listLinkChampion.size(); i++) {
            ArrayList<String> listLinkItem = new ArrayList<>();
            document = Jsoup.connect(listLinkChampion.get(i)).get();

            Elements elementsCoreItems = document.select("div.champ-build.active div.champ-build__section.champ-build__section--twoSixth div.champ-build__section__content div.champ-build__section__content__tab.current a");
            if (elementsCoreItems.size() != 0) {
                for (Element elementsCoreItem : elementsCoreItems) {
                    String linkEquipment = elementsCoreItem.absUrl("href");
                    listLinkItem.add(linkEquipment);
                }
            }

            Elements elementsBoot = document.select("div.champ-build.active div.champ-build__section.champ-build__section--oneSixth.champ-build__section--toggleDrop.champ-build__section--mobileNoMarg div.champ-build__section__content div.champ-build__section__content__tab.current a");
            if (elementsBoot.size() != 0) {
                String linkEquipment = elementsBoot.first().absUrl("href");
                listLinkItem.add(linkEquipment);
            }

            Elements elementsLuxuryItems = document.select("div.champ-build.active div.champ-build__section.champ-build__section--half.luxury-half.champ-build__section--toggleDrop.champ-build__section--tabletNoMarg div.champ-build__section__content div.champ-build__section__content__tab.current a");
            if (elementsLuxuryItems.size() != 0) {
                for (Element elementsLuxuryItem : elementsLuxuryItems) {
                    String linkEquipment = elementsLuxuryItem.absUrl("href");
                    listLinkItem.add(linkEquipment);
                }
            }

            for (String link : listLinkItem) {
                championListId = i;
                document = Jsoup.connect(link).get();
                Elements elementsName = document.select("div.mf-responsive__leftCol #view-title");
                name = elementsName.text();
                itemId = itemBotDatabase.getItemIDByItemName(database.table.item, splitNameEquipment(name));
                itemBotDatabase.insertItemForChampion(database.table.item_for_champion, championListId, itemId);
            }
        }
    }

    private static String splitNameEquipment(String s) {
        StringBuilder kq = new StringBuilder();
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            String a = st.nextToken();
            if (!a.contains("Total")) {
                kq.append(a);
                kq.append(" ");
            } else {
                break;
            }
        }
        return kq.toString().trim();
    }

}
