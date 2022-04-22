package bot;

import utils.AppData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseBot {
    protected boolean isRunning;
    protected long lastRun;
    protected long restTime;
    protected Toolkit toolkit;
    protected ExecutorService executor;

    public BaseBot(int maxThread, long restTime) {
        this.isRunning = false;
        this.restTime = restTime;
        this.lastRun = 0;
        this.toolkit = new Toolkit();
        this.executor = Executors.newFixedThreadPool(Math.max(maxThread, AppData.threadDefault));
    }

    public void run() {
        toolkit.appLogger.info("running");
        isRunning = true;
    }

    public void complete() {
        toolkit.appLogger.info("complete");
        isRunning = false;
        lastRun = System.currentTimeMillis();
    }

    public boolean isNeedRun(long now) {
        //  run();
        toolkit.appLogger.debug(String.format("isRunning[%s] - last[%s] - rest[%s] - now[%s]", isRunning, lastRun, restTime, now));
        return (!isRunning && (lastRun + restTime < now));
    }
}
