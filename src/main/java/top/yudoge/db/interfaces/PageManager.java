package top.yudoge.db.interfaces;

import top.yudoge.db.structures.DataPage;
import top.yudoge.db.structures.Page;
import top.yudoge.db.structures.Row;
import top.yudoge.db.structures.TableHeaderPage;

import javax.xml.crypto.Data;
import java.io.IOException;

public abstract class PageManager {
    public abstract DataPage newDataPage(Table table, int pageOffset) throws IOException;
    public abstract TableHeaderPage newTableHeaderPage(Table table) throws IOException;
    public abstract DataPage getDataPage(Table table, int pageOffset) throws IOException;
    public abstract TableHeaderPage getTableHeaderPage(Table table) throws IOException;
    public abstract void flushSomePage() throws IOException;
    public abstract void flushAllPage() throws IOException;
    public abstract int cachedPages();
}
