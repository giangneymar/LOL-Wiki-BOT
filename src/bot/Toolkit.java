package bot;

import com.google.gson.Gson;
import storage.AppStorage;
import utils.AppLogger;

public class Toolkit {
    public AppLogger appLogger;
    public AppStorage appStorage;
    public Gson gson;

    public Toolkit() {
        this.appLogger = AppLogger.getInstance();
        this.appStorage = AppStorage.getInstance();
        this.gson = new Gson();
    }
}
