package utils;

public interface AppData {
    String charset = "utf-8";
    int threadDefault = 5;

    interface Config {
        String root = "/asset";

        interface Input {
            String directory = root + "/in";
            String config = directory + "/config.json";
        }

//        interface Output {
//            String directory = root + "/out";
//        }
    }

    interface Database {
        interface MySQL {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://%s/%s?user=%s&password=%s";
        }
    }
}
