package bot;

import org.jsoup.internal.StringUtil;
import utils.AppSql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class BaseBotDatabase {
    protected Toolkit toolkit;
    protected AppSql appSql;
    public BaseBotDatabase() {
        this.toolkit = new Toolkit();
        this.appSql = new AppSql();
    }
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
