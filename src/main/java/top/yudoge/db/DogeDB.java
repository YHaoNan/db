package top.yudoge.db;

import top.yudoge.db.interfaces.PageManager;
import top.yudoge.db.interfaces.Table;
import top.yudoge.db.structures.*;
import top.yudoge.db.utils.TableFactory;

import java.io.IOException;

public class DogeDB {

    private PageManager pageManager;

    public DogeDB() {
        pageManager = new HashLinkedPageManager();

    }

    public Table createNewTable(String tableName, Row rowSchema) throws IOException {
        return TableFactory.createNewTable(tableName, rowSchema, pageManager);
    }

    public PageManager getPageManager() {
        return pageManager;
    }
}
