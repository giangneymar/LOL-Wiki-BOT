package bot.mange.wallpaper;

import bot.BaseBotDatabase;
import org.jsoup.internal.StringUtil;

import java.sql.PreparedStatement;
import java.util.List;

public class WallpaperBotDatabase extends BaseBotDatabase {
    public WallpaperBotDatabase() {
        super();
    }
    public void insertWallpaper(String table, List<String> wallpapers) {
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
}
