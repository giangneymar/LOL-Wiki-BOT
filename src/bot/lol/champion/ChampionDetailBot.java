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
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class ChampionDetailBot extends BaseBot {
    private AppLogger appLogger = AppLogger.getInstance();
    private AppStorage appStorage = AppStorage.getInstance();

    public ChampionDetailBot(int maxThread, long restTime) {
        super(maxThread, restTime, 2);
        toolkit.appLogger.info("create");
    }

    @Override
    public void run() {
        super.run();
        toolkit.appLogger.info("prepare");
        updateDetailChampion();
        complete();
    }

    public void updateDetailChampion() {
        executor.execute(() -> {
            try {
                toolkit.appLogger.info("start crawl");
                toolkit.appLogger.info("crawl allChampion");
                crawlChampionDetail("https://leagueoflegends.fandom.com/wiki/League_of_Legends_Wiki",
                        "https://lol.garena.com/champions",
                        "https://mobalytics.gg/blog/lol-tier-list-for-climbing-solo-queue/");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void crawlChampionDetail(String link, String link1, String link2) throws IOException, SQLException {
        ChampionBotDatabase championBotDatabase = new ChampionBotDatabase();
        Database database = appStorage.config.database;
        String classes, resource, blueEssence, riotPoints,
                releaseDate, adaptiveType, health, healthRegen, armor, attackDamage,
                magicResist, moveSpeed, attackRange, bonusAS, description;
        int idChampion, idUpdateChamp;
        List<String> legacyName;
        List<String> positionName;

        Document document = Jsoup.connect(link).get();
        Elements elementsListChampOfLink = document.select("ol.champion_roster li");
        int i = 0;
        for (Element elementsChampOfLink : elementsListChampOfLink) {
            idChampion = i;
            blueEssence = null;
            resource = null;
            riotPoints = null;
            adaptiveType = null;
            attackRange = null;
            bonusAS = null;

            appLogger.info("get link one champ");
            Elements elementsLinkChamp = elementsChampOfLink.getElementsByTag("a");
            String linkChamp = elementsLinkChamp.first().absUrl("href");
            document = Jsoup.connect(linkChamp).get();
            Elements elementsDataValueRedirect = document.select("div.pi-data-value.pi-font a.mw-redirect");

            appLogger.info("get classes champ");
            classes = elementsDataValueRedirect.first().text();
            Elements elementsDataValuePiFont = document.select("div.pi-data-value.pi-font a");

            appLogger.info("get resource champ");
            for (Element elementResource : elementsDataValuePiFont) {
                if (elementResource.text().contains("Blood Well") || elementResource.text().contains("Mana") || elementResource.text().contains("Energy") || elementResource.text().contains("Manaless")) {
                    resource = elementResource.text();
                    break;
                }
            }

            appLogger.info("get list legacyName champ");
            legacyName = new ArrayList<>();
            for (Element elementLegacy : elementsDataValuePiFont) {
                if (elementLegacy.absUrl("href").contains("Legacy") && elementLegacy.text().length() != 0) {
                    legacyName.add(elementLegacy.text());
                }
            }

            appLogger.info("get list positionName champ");
            positionName = new ArrayList<>();
            for (Element elementPosition : elementsDataValuePiFont) {
                if (elementPosition.absUrl("href").contains("champion") && !elementPosition.absUrl("href").contains("BE") && !elementPosition.absUrl("href").contains("RP")) {
                    if (elementPosition.text().length() != 0) {
                        positionName.add(elementPosition.text());
                    }
                }
            }

            appLogger.info("get blueEssence champ");
            for (Element elementBlueEssence : elementsDataValuePiFont) {
                if (elementBlueEssence.absUrl("href").contains("BE") && elementBlueEssence.text().length() != 0) {
                    blueEssence = elementBlueEssence.text();
                }
            }

            appLogger.info("get riotPoints champ");
            for (Element elementRiotPoints : elementsDataValuePiFont) {
                if (elementRiotPoints.absUrl("href").contains("RP") && elementRiotPoints.text().length() != 0) {
                    riotPoints = elementRiotPoints.text();
                }
            }

            appLogger.info("get releaseDate champ");
            releaseDate = elementsDataValuePiFont.first().text();

            appLogger.info("get adaptiveType champ");
            for (Element elementAdaptiveType : elementsDataValuePiFont) {
                if (elementAdaptiveType.absUrl("href").contains("force") && elementAdaptiveType.text().length() != 0) {
                    adaptiveType = elementAdaptiveType.text();
                }
            }
            Elements elementsPiSmartDataValue = document.select("div.pi-smart-data-value.pi-data-value.pi-font.pi-item-spacing.pi-border-color span");

            appLogger.info("get health champ");
            health = elementsPiSmartDataValue.first().text() + "(" + elementsPiSmartDataValue.get(1).text() + ")";

            appLogger.info("get healthRegen champ");
            healthRegen = elementsPiSmartDataValue.get(2).text() + "(" + elementsPiSmartDataValue.get(3).text() + ")";

            appLogger.info("get armor champ");
            armor = elementsPiSmartDataValue.get(4).text() + "(" + elementsPiSmartDataValue.get(5).text() + ")";

            appLogger.info("get attackDamage champ");
            attackDamage = elementsPiSmartDataValue.get(6).text() + "(" + elementsPiSmartDataValue.get(7).text() + ")";

            appLogger.info("get magicResist champ");
            magicResist = elementsPiSmartDataValue.get(8).text() + "(" + elementsPiSmartDataValue.get(9).text() + ")";

            appLogger.info("get moveSpeed champ");
            moveSpeed = elementsPiSmartDataValue.get(10).text();

            appLogger.info("get attackRange champ");
            for (Element elementAttackRange : elementsPiSmartDataValue) {
                if (elementAttackRange.absUrl("id").contains("AttackRange")) {
                    attackRange = elementAttackRange.text();
                }
            }

            appLogger.info("get bonusAS champ");
            for (Element elementBonusAS : elementsPiSmartDataValue) {
                if (elementBonusAS.absUrl("id").contains("AttackSpeedBonus")) {
                    bonusAS = "0" + elementBonusAS.text() + "%";
                }
            }

            appLogger.info(String.format("update champion where id = %d", idChampion));
            championBotDatabase.updateChampionById(idChampion, database.table.champion, legacyName, positionName,
                    blueEssence, riotPoints, releaseDate, classes, adaptiveType, resource, health, healthRegen, armor, magicResist
                    , moveSpeed, attackDamage, attackRange, bonusAS);
            i++;
        }

        appLogger.info("update description champ");
        document = Jsoup.connect(link1).get();
        Elements elementsListChamp = document.select("div.boxs a");
        for (int m = 0; m < elementsListChamp.size(); m++) {
            String linkChamp = elementsListChamp.get(m).absUrl("href");
            document = Jsoup.connect(linkChamp).get();

            appLogger.info("get description champ");
            Elements elementsLore = document.select("div.lore p");
            description = elementsLore.text();

            appLogger.info("get id Champion");
            idUpdateChamp = m;
            championBotDatabase.updateDesChampionById(idUpdateChamp, description, database.table.champion);
        }

        appLogger.info("update tier champ");
        document = Jsoup.connect(link2).get();
        List<String> listNameChamp = new ArrayList<>();
        Elements elementsListName = document.select("div.section a");
        for (Element elementName : elementsListName) {
            listNameChamp.add(elementName.text());
        }
        for (String nameUpdate : listNameChamp) {
            championBotDatabase.updateTierChampionByName(nameUpdate, database.table.champion);
        }

        appLogger.info(String.format("list champion has %s item", i));
    }

}
