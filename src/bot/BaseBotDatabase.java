package bot;

import org.jsoup.internal.StringUtil;
import storage.models.Wallpaper;
import utils.AppSql;

import java.sql.PreparedStatement;
<<<<<<< HEAD
import java.sql.SQLException;
import java.sql.Statement;
=======
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
>>>>>>> origin/master
import java.util.List;

public class BaseBotDatabase {
    protected Toolkit toolkit;
    protected AppSql appSql;
    public BaseBotDatabase() {
        this.toolkit = new Toolkit();
        this.appSql = new AppSql();
    }
<<<<<<< HEAD
=======

    public void insert(String table, List<String> wallpapers) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("insert data into table[%s]", table));
        appSql.connect(connection -> {
            try {
                connection.setAutoCommit(false);
                String sql = String.format("INSERT INTO %s (image) VALUES (?)", table);
                PreparedStatement ps = connection.prepareStatement(sql);
                for (String wallpaper : wallpapers) {
                    ps.setObject(1, wallpaper);
                    ps.addBatch();
                }
                ps.executeBatch();
                connection.commit();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ArrayList<Wallpaper> getAllWallpaper() {
        ArrayList<Wallpaper> wallpapers = new ArrayList<>();
        appSql.connect(connection -> {
            String sql = "SELECT * FROM wallpaper";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Wallpaper wallpaper = new Wallpaper(
                        rs.getInt(1),
                        rs.getString(2)
                );
                wallpapers.add(wallpaper);
            }
        });
        return wallpapers;
    }

>>>>>>> origin/master
    public void delete(String table) {
        if (StringUtil.isBlank(table)) {
            toolkit.appLogger.info("null table name");
        }
        toolkit.appLogger.info(String.format("clear all data table[%s]", table));
        appSql.connect(connection -> {
            try {
                String sql = String.format("DELETE FROM %s", table);
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.executeUpdate();
            } catch (Exception e) {
                toolkit.appLogger.warning(String.format("clear all data table[%s] has error[%s]", table, e.getMessage()));
            }
        });
    }
}
