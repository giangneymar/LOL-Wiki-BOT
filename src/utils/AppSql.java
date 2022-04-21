package utils;

import storage.AppStorage;
import storage.models.database.Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class AppSql {
    private Connection connection;
    private AppLogger appLogger;
    private AppStorage appStorage;

    public AppSql() {
        appLogger = AppLogger.getInstance();
        appStorage = AppStorage.getInstance();
    }

    public void connect(Callback callback) {
        try {
            Class.forName(AppData.Database.MySQL.driver);
            Database database = appStorage.config.database;
            if (database == null) return;
            String connectString = String.format(AppData.Database.MySQL.url,
                    database.domain + ":" + database.port,
                    database.schema,
                    database.user,
                    database.pass);
            connection = DriverManager.getConnection(connectString);
            callback.execute(connection);
        } catch (Exception e) {
            appLogger.warning(String.format("database execute fail : %s", e.getMessage()));
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                appLogger.warning(String.format("database close connection fail : %s", e.getMessage()));
            }
        }
    }

    public interface Callback {
        void execute(Connection connection);
    }
}
