package bot.lol.champion;

import bot.BaseBotDatabase;
import org.jsoup.internal.StringUtil;

import java.sql.PreparedStatement;
import java.util.List;

public class ChampionBotDatabase extends BaseBotDatabase {
    public ChampionBotDatabase() {
        super();
    }
    public void insertAllChampion(
            String table,
            int idChampionList,
            List<String> legacyName,
            List<String> positionName,
            String blueEssence,
            String riotPoints,
            String releaseDate,
            String classes,
            String adaptiveType,
            String resource,
            String health,
            String healthRegen,
            String armor,
            String magicResist,
            String moveSpeed,
            String attackDamage,
            String attackRange,
            String bonusAS,
            String description,
            String tier
    ) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("insert data table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = String.format("INSERT INTO %s ("
                                + "championListId,"
                                + "legacyName,"
                                + "positionName,"
                                + "blueEssence,"
                                + "riotPoints,"
                                + "releaseDate,"
                                + "classes,"
                                + "adaptiveType,"
                                + "resource,"
                                + "health,"
                                + "healthRegen,"
                                + "armor,"
                                + "magicResist,"
                                + "moveSpeed,"
                                + "attackDamage,"
                                + "attackRange,"
                                + "bonusAS,"
                                + "description,"
                                + "tier)"
                                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                        table);
                PreparedStatement pstmt = connection.prepareStatement(sql);
                StringBuilder stringBuilder1 = new StringBuilder();
                StringBuilder stringBuilder2 = new StringBuilder();
                for (String le : legacyName) {
                    stringBuilder1.append(le);
                    stringBuilder1.append(" ");
                }
                String legacy = stringBuilder1.toString().trim();
                for (String po : positionName) {
                    stringBuilder2.append(po);
                    stringBuilder2.append(" ");
                }
                String position = stringBuilder2.toString().trim();
                pstmt.setInt(1, idChampionList);
                pstmt.setString(2, legacy);
                pstmt.setString(3, position);
                pstmt.setString(4, blueEssence);
                pstmt.setString(5, riotPoints);
                pstmt.setString(6, releaseDate);
                pstmt.setString(7, classes);
                pstmt.setString(8, adaptiveType);
                pstmt.setString(9, resource);
                pstmt.setString(10, health);
                pstmt.setString(11, healthRegen);
                pstmt.setString(12, armor);
                pstmt.setString(13, magicResist);
                pstmt.setString(14, moveSpeed);
                pstmt.setString(15, attackDamage);
                pstmt.setString(16, attackRange);
                pstmt.setString(17, bonusAS);
                pstmt.setString(18, description);
                pstmt.setString(19, tier);
                pstmt.executeUpdate();
                toolkit.appLogger.info(String.format("insert success", table));
                pstmt.close();
                connection.close();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("insert all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }

    public void insertListChampion(
            String table,
            int id,
            String name,
            String image
    ) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("insert data table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = String.format("INSERT INTO %s ("
                                + "id,"
                                + "name,"
                                + "image)"
                                + "VALUES(?,?,?)",
                        table);
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setString(3, image);
                pstmt.executeUpdate();
                toolkit.appLogger.info(String.format("insert success", table));
                pstmt.close();
                connection.close();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("insert all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }

    public void updateDesChampion(int id, String des, String table) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("update data into table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = "UPDATE champion "
                        + "SET description = ? "
                        + "WHERE championListId = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, des);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
                pstmt.close();
                connection.close();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("update all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }

    public void updateTierChampion(String name, String table) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("update data into table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = "UPDATE champion "
                        + "SET tier = 's' "
                        + "WHERE name = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.executeUpdate();
                pstmt.close();
                connection.close();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("update all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }
}
