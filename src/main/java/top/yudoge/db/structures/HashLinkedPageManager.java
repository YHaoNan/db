package top.yudoge.db.structures;

import top.yudoge.db.constants.Constants;
import top.yudoge.db.interfaces.PageManager;
import top.yudoge.db.interfaces.Table;
import top.yudoge.db.utils.PageFactory;
import top.yudoge.db.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HashLinkedPageManager extends PageManager {

    /**
     * Key:  tablename-pageoffset  eg. user-1 user-2
     * Value: page
     */
    private Map<String, Page> pageMap;
    /**
     * 先进入缓存的页先出
     */
    private Queue<String> pageQueue;


    public HashLinkedPageManager() {
        pageMap = new HashMap<>();
        pageQueue = new ConcurrentLinkedQueue<>();
    }

    private static String pageId(Table table, int pageOffset) {
        return table.name() + "-" + pageOffset;
    }

    private void putPage(String pid, Page page) throws IOException {
        pageQueue.offer(pid);
        pageMap.put(pid, page);
        if (this.pageQueue.size() >= Constants.MAX_PAGE_CACHED) {
            flushSomePage();
        }
    }

    @Override
    public DataPage newDataPage(Table table, int pageOffset) throws IOException {
        synchronized (HashLinkedPageManager.class) {
            DataPage page = PageFactory.newDataPage(table.schema(), pageOffset);
            String pid = pageId(table, pageOffset);
            putPage(pid, page);
            return page;
        }
    }

    @Override
    public TableHeaderPage newTableHeaderPage(Table table) throws IOException {
        synchronized (HashLinkedPageManager.class) {
            TableHeaderPage page = PageFactory.newTableHeaderPage();
            String pid = pageId(table, 0);
            putPage(pid,page);
            return page;
        }
    }

    @Override
    public DataPage getDataPage(Table table, int pageOffset) throws IOException {
        synchronized (HashLinkedPageManager.class) {
            String pid = pageId(table, pageOffset);
            if (pageMap.containsKey(pid)) return (DataPage) pageMap.get(pid);
            File tableFile = new File(Constants.DATA_DIR , table.name() +Constants.TABLE_EXT_NAME);
            DataPage dataPage = new DataPage(table.schema());
            dataPage.fromBytes(Utils.readPage(tableFile, pageOffset), 0);
            putPage(pid, dataPage);
            return dataPage;
        }
    }

    @Override
    public TableHeaderPage getTableHeaderPage(Table table) throws IOException {
        synchronized (HashLinkedPageManager.class) {
            String pid = pageId(table, 0);
            if (pageMap.containsKey(pid)) return (TableHeaderPage) pageMap.get(pid);
            File tableFile = new File(Constants.DATA_DIR , table.name() + Constants.TABLE_EXT_NAME);
            TableHeaderPage thp = new TableHeaderPage();
            thp.fromBytes(Utils.readPage(tableFile, 0), 0);
            putPage(pid, thp);
            return thp;
        }
    }

    @Override
    public void flushSomePage() throws IOException {
        synchronized (HashLinkedPageManager.class) {
            int pageToFlush = (int) (pageQueue.size() * 0.2);
            for (int i=0; i<pageToFlush; i++) {
                String pid = pageQueue.poll();
                File pageFile = new File(Constants.DATA_DIR , pid.substring(0, pid.lastIndexOf("-")) + Constants.TABLE_EXT_NAME);
                Utils.writePage(pageFile, pageMap.get(pid));
                pageMap.remove(pid);
            }
        }
    }

    @Override
    public void flushAllPage() throws IOException {
        synchronized (HashLinkedPageManager.class)  {
            for (int i=0; i < pageQueue.size(); i++) {
                String pid = pageQueue.poll();
                File pageFile = new File(Constants.DATA_DIR , pid.substring(0, pid.lastIndexOf("-")) + Constants.TABLE_EXT_NAME);
                Utils.writePage(pageFile, pageMap.get(pid));
                pageMap.remove(pid);
            }
        }
    }

    @Override
    public int cachedPages() {
        return pageQueue.size();
    }


}
