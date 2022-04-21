package storage;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import storage.models.Config;
import utils.AppData;
import utils.AppLogger;

import java.io.File;
import java.io.IOException;

public class AppStorage {
    private static AppStorage instance;
    private AppLogger appLogger;
    public String jarPath;
    public Config config;

    private AppStorage() {
        appLogger = AppLogger.getInstance();
    }

    public static AppStorage getInstance() {
        if (instance == null) {
            instance = new AppStorage();
        }
        return instance;
    }

    public void loadConfig(Gson gson) {
        try {
            appLogger.info("loading current directory");
            jarPath = System.getProperty("user.dir");
            appLogger.debug(String.format("running directory [ %s ]", jarPath));

            appLogger.info("loading configure");
            File file = new File(jarPath + AppData.Config.Input.config);
            String string = FileUtils.readFileToString(file, AppData.charset);
            config = gson.fromJson(string, Config.class);
            config.setting.timeCheck = config.setting.timeCheck * 60 * 1000;
            appLogger.debug(gson.toJson(config));
        } catch (IOException e) {
            appLogger.warning(String.format("load configure fail : %s", e.getMessage()));
        }
    }
}
