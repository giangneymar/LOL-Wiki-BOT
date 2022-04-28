package bot.lol.guide;

import bot.BaseBotDatabase;
import org.jsoup.internal.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemBotDatabase extends BaseBotDatabase {
    public ItemBotDatabase() {
        super();
    }

    public void insertItem(String table, int id, String name, String totalPrice, String recipePrice, String sellPrice, String image, String description) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("insert data table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = String.format("INSERT INTO %s (id,name,totalPrice,recipePrice,sellPrice," +
                        "image,description) VALUES (?,?,?,?,?,?,?)", table);
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, totalPrice);
                ps.setString(4, recipePrice);
                ps.setString(5, sellPrice);
                ps.setString(6, image);
                ps.setString(7, description);
                ps.executeUpdate();
                ps.close();
                connection.close();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("insert all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }

    public void insertItemForChampion(String table, int championId, int itemId) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("insert data table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = String.format("INSERT INTO %s (championId,itemId) VALUES (?,?)", table);
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, championId);
                ps.setInt(2, itemId);
                ps.executeUpdate();
                ps.close();
                connection.close();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("insert all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }

    public int getItemIDByItemName(String table, String name) {
        Item item = new Item();
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("get data table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = "SELECT item.id FROM item WHERE item.name = \"" + name + "\"";
                Statement ps = connection.createStatement();
                ResultSet rs = ps.executeQuery(sql);
                while (rs.next()) {
                    item.id = rs.getInt(1);
                }
                ps.close();
                connection.close();
            } catch (SQLException e) {
                toolkit.appLogger.warning(String.format("get data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
        return item.id;
    }

    public class Item {
        int id = - 1;
    }

}
