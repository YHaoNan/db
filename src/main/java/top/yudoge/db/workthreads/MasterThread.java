package top.yudoge.db.workthreads;

import top.yudoge.db.DogeDB;
import top.yudoge.db.constants.Constants;

import java.io.IOException;

public class MasterThread extends Thread {
    private final DogeDB db;

    public MasterThread(DogeDB db) {
        this.db = db;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);
                if (db.getPageManager().cachedPages() > Constants.MAX_PAGE_CACHED * 0.6)
                    db.getPageManager().flushSomePage();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
