package bot.lol.champion;

import bot.BaseBotDatabase;
import org.jsoup.internal.StringUtil;

import java.sql.PreparedStatement;
import java.util.List;

public class ChampionBotDatabase extends BaseBotDatabase {
    public ChampionBotDatabase() {
        super();
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
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, image);
                ps.executeUpdate();
                ps.close();
                connection.close();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("insert all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }

    public void updateChampionById(
            int id, String table, List<String> legacyName,
            List<String> positionName, String blueEssence, String riotPoints,
            String releaseDate, String classes, String adaptiveType, String resource,
            String health, String healthRegen, String armor, String magicResist,
            String moveSpeed, String attackDamage, String attackRange, String bonusAS
    ) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("update data into table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = String.format("UPDATE %s "
                                + "SET legacyName = ?, "
                                + "positionName = ?, "
                                + "blueEssence = ?, "
                                + "riotPoints = ?, "
                                + "releaseDate = ?, "
                                + "classes = ?, "
                                + "adaptiveType = ?, "
                                + "resource = ?, "
                                + "health = ?, "
                                + "healthRegen = ?, "
                                + "armor = ?, "
                                + "magicResist = ?, "
                                + "moveSpeed = ?, "
                                + "attackDamage = ?, "
                                + "attackRange = ?, "
                                + "bonusAS = ? "
                                + "WHERE id = ?",
                        table);
                PreparedStatement ps = connection.prepareStatement(sql);
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
                ps.setString(1, legacy);
                ps.setString(2, position);
                ps.setString(3, blueEssence);
                ps.setString(4, riotPoints);
                ps.setString(5, releaseDate);
                ps.setString(6, classes);
                ps.setString(7, adaptiveType);
                ps.setString(8, resource);
                ps.setString(9, health);
                ps.setString(10, healthRegen);
                ps.setString(11, armor);
                ps.setString(12, magicResist);
                ps.setString(13, moveSpeed);
                ps.setString(14, attackDamage);
                ps.setString(15, attackRange);
                ps.setString(16, bonusAS);
                ps.setInt(17, id);
                ps.executeUpdate();
                ps.close();
                connection.close();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("update all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }

    public void updateDesChampionById(int id, String des, String table) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("update data into table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = String.format("UPDATE %s "
                        + "SET description = ? "
                        + "WHERE id = ?",
                        table);
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, des);
                ps.setInt(2, id);
                ps.executeUpdate();
                ps.close();
                connection.close();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("update all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }

    public void updateTierChampionByName(String name, String table) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("update data into table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = String.format("UPDATE %s "
                        + "SET tier = 's' "
                        + "WHERE name = ?",
                        table);
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, name);
                ps.executeUpdate();
                ps.close();
                connection.close();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("update all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }

    public void insertAllAbilities(
            String table,
            int championId,
            String name,
            String range,
            String cost,
            String coolDown,
            String image,
            String description,
            String key
    ) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("insert data table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = String.format("INSERT INTO %s ("
                                + "championId,"
                                + "name,"
                                + "rangeAbilities,"
                                + "cost,"
                                + "coolDown,"
                                + "image,"
                                + "description,"
                                + "keyAbilities)"
                                + "VALUES(?,?,?,?,?,?,?,?)",
                        table);
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, championId);
                ps.setString(2, name);
                ps.setString(3, range);
                ps.setString(4, cost);
                ps.setString(5, coolDown);
                ps.setString(6, image);
                ps.setString(7, description);
                ps.setString(8, key);
                ps.executeUpdate();
                ps.close();
                connection.close();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("insert all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }
}
