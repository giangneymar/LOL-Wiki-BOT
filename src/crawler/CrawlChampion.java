package crawler;

import bot.mange.champion.ChampionBotDatabase;
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

public class CrawlChampion {
    private AppLogger appLogger = AppLogger.getInstance();
    private AppStorage appStorage = AppStorage.getInstance();
    public void crawlAllChampion(String link, String link1, String link2) throws IOException, SQLException {
        ChampionBotDatabase championBotDatabase = new ChampionBotDatabase();
        Database database = appStorage.config.database;
        championBotDatabase.delete(database.table.champion);
        String imageChamp, name, classes, resoure, blueEssence, riotPoints,
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
            name = null;
            imageChamp = null;
            classes = null;
            blueEssence = null;
            resoure = null;
            riotPoints = null;
            releaseDate = null;
            adaptiveType = null;
            health = null;
            healthRegen = null;
            armor = null;
            attackDamage = null;
            attackRange = null;
            magicResist = null;
            moveSpeed = null;
            bonusAS = null;
            //get link one champ of LINK
            Elements elementsLinkChamp = elementsChampOfLink.getElementsByTag("a");
            String linkChamp = elementsLinkChamp.first().absUrl("href");

            //get image on champ
            Elements elementsImageChamp = elementsChampOfLink.getElementsByTag("img");
            imageChamp = elementsImageChamp.first().absUrl("data-src");

            document = Jsoup.connect(linkChamp).get();
            Elements elementsParser = document.select("div.mw-parser-output p b");
            // get name champ
            for (Element elementName : elementsParser) {
                name = elementName.text();
                break;
            }

            Elements elementsDataValueRedirect = document.select("div.pi-data-value.pi-font a.mw-redirect");
            // get classes champ
            classes = elementsDataValueRedirect.first().text();

            Elements elementsDataValuePiFont = document.select("div.pi-data-value.pi-font a");
            // get resource champ
            for (Element elementResource : elementsDataValuePiFont) {
                if (elementResource.text().contains("Blood Well")
                        || elementResource.text().contains("Mana")
                        || elementResource.text().contains("Energy")
                        || elementResource.text().contains("Manaless")) {
                    resoure = elementResource.text();
                    break;
                }
            }

            // get list legacyName champ
            legacyName = new ArrayList<>();
            for (Element elementLegacy : elementsDataValuePiFont) {
                if (elementLegacy.absUrl("href").contains("Legacy") && elementLegacy.text().length() != 0) {
                    legacyName.add(elementLegacy.text());
                }
            }

            // get list positionName champ
            positionName = new ArrayList<>();
            for (Element elementPosition : elementsDataValuePiFont) {
                if (elementPosition.absUrl("href").contains("champion")
                        && !elementPosition.absUrl("href").contains("BE")
                        && !elementPosition.absUrl("href").contains("RP")) {
                    if (elementPosition.text().length() != 0) {
                        positionName.add(elementPosition.text());
                    }
                }
            }

            // get blueEssence champ
            for (Element elementBlueEssence : elementsDataValuePiFont) {
                if (elementBlueEssence.absUrl("href").contains("BE")
                        && elementBlueEssence.text().length() != 0) {
                    blueEssence = elementBlueEssence.text();
                }
            }

            // get riotPoints champ
            for (Element elementRiotPoints : elementsDataValuePiFont) {
                if (elementRiotPoints.absUrl("href").contains("RP")
                        && elementRiotPoints.text().length() != 0) {
                    riotPoints = elementRiotPoints.text();
                }
            }

            // get releaseDate champ
            releaseDate = elementsDataValuePiFont.first().text();

            // get adaptiveType champ
            for (Element elementAdaptiveType : elementsDataValuePiFont) {
                if (elementAdaptiveType.absUrl("href").contains("force")
                        && elementAdaptiveType.text().length() != 0) {
                    adaptiveType = elementAdaptiveType.text();
                }
            }

            Elements elementsPiSmartDataValue = document
                    .select("div.pi-smart-data-value.pi-data-value.pi-font.pi-item-spacing.pi-border-color span");
            // get health champ
            health = elementsPiSmartDataValue.first().text() + "(" + elementsPiSmartDataValue.get(1).text() + ")";

            // get healthRegen champ
            healthRegen = elementsPiSmartDataValue.get(2).text() + "(" + elementsPiSmartDataValue.get(3).text() + ")";

            // get armor champ
            armor = elementsPiSmartDataValue.get(4).text() + "(" + elementsPiSmartDataValue.get(5).text() + ")";

            // get attackDamage champ
            attackDamage = elementsPiSmartDataValue.get(6).text() + "(" + elementsPiSmartDataValue.get(7).text() + ")";

            // get magicResist champ
            magicResist = elementsPiSmartDataValue.get(8).text() + "(" + elementsPiSmartDataValue.get(9).text() + ")";

            // get moveSpeed champ
            moveSpeed = elementsPiSmartDataValue.get(10).text();

            // get attackRange champ
            for (Element elementAttackRange : elementsPiSmartDataValue) {
                if (elementAttackRange.absUrl("id").contains("AttackRange")) {
                    attackRange = elementAttackRange.text();
                }
            }

            // get bonusAS champ
            for (Element elementBonusAS : elementsPiSmartDataValue) {
                if (elementBonusAS.absUrl("id").contains("AttackSpeedBonus")) {
                    bonusAS = "0" + elementBonusAS.text() + "%";
                }
            }

            championBotDatabase.insertAllChampion(database.table.champion, idChampion, name, imageChamp, legacyName,
                    positionName, blueEssence, riotPoints, releaseDate, classes, adaptiveType, resoure, health,
                    healthRegen, armor, magicResist, moveSpeed, attackDamage, attackRange, bonusAS, "", "");

            i++;
        }
        ////////////////////////////////////////// des

        document = Jsoup.connect(link1).get();
        Elements elementsListChamp = document.select("div.boxs a");
        for (int m = 0; m < elementsListChamp.size(); m++) {
            String linkChamp = elementsListChamp.get(m).absUrl("href");
            document = Jsoup.connect(linkChamp).get();

            // get description champ
            Elements elementsLore = document.select("div.lore p");
            description = elementsLore.text();
            System.out.println("description " + description);

            // id Champion
            idUpdateChamp = m;
            championBotDatabase.updateDesChampion(idUpdateChamp, description, database.table.champion);
        }

        //////////////////////////////////////// tier

        document = Jsoup.connect(link2).get();
        List<String> listNameChamp = new ArrayList<>();
        Elements elementsListName = document.select("div.section a");
        for (Element elementName : elementsListName) {
            listNameChamp.add(elementName.text());
        }
        for (String nameUpdate : listNameChamp) {
            championBotDatabase.updateTierChampion(nameUpdate, database.table.champion);
        }

        appLogger.info(String.format("list champion has %s item", i));
    }
}
