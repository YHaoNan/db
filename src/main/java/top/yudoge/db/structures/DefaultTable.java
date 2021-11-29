package top.yudoge.db.structures;

import top.yudoge.db.DogeDB;
import top.yudoge.db.constants.Constants;
import top.yudoge.db.interfaces.PageManager;
import top.yudoge.db.interfaces.RowFilter;
import top.yudoge.db.interfaces.Table;
import top.yudoge.db.utils.PageFactory;
import top.yudoge.db.utils.Requires;
import top.yudoge.db.utils.Utils;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 格式：
 *  表描述页                                 |   [页大小]
 *  数据页列表
 *
 */

/**
 * 表描述页格式
 *  TABLE_PAGE_TAIL                         |   表中最后一个页的偏移量      |    4字节
 *
 */
public class DefaultTable implements Table {

    private String name;
    private Row schema;
    private File tableFile;
    private PageManager pageManager;

    public DefaultTable(String name, Row schema, PageManager manager) throws IOException {
        this.name = name;
        this.schema = schema;
        this.pageManager = manager;

        tableFile = new File(Constants.DATA_DIR, name + Constants.TABLE_EXT_NAME);
        Requires.fileExists(tableFile);Requires.fileCanRead(tableFile);Requires.fileCanWrite(tableFile);

    }


    public void addRow(Row row) throws IOException {
        TableHeaderPage tableHeaderPage = pageManager.getTableHeaderPage(this);
        DataPage lastDataPage = pageManager.getDataPage(this, tableHeaderPage.TABLE_PAGE_TAIL);
        boolean isNewPageCreated = false;

        byte rowBytes[] = row.toBytes();

        // 如果page无法容纳这一行 开启一个新页
        if (Constants.PAGE_SIZE - lastDataPage.page_header.PAGE_TAIL < rowBytes.length) {
            tableHeaderPage.TABLE_PAGE_TAIL += 1;
            lastDataPage = pageManager.newDataPage(this, tableHeaderPage. TABLE_PAGE_TAIL);
            isNewPageCreated = true;
        }
        lastDataPage.getRows().add(row);

        // 更新当前数据页页尾指针
        lastDataPage.page_header.PAGE_TAIL += rowBytes.length;

//        if (isNewPageCreated) Utils.writePage(tableFile, tableHeaderPage);
//        Utils.writePage(tableFile, lastDataPage);
    }

    public List<Row> getRows() throws IOException {
        return getRows(row -> true);
    }

    public List<Row> getRows(RowFilter filter) throws IOException {
        TableHeaderPage tableHeaderPage = pageManager.getTableHeaderPage(this);
        int currentPage = 1;
        List<Row> resultSet = new ArrayList<>();
        while (currentPage <= tableHeaderPage.TABLE_PAGE_TAIL) {
            DataPage dataPage = pageManager.getDataPage(this, currentPage);
//            dataPage.fromBytes(Utils.readPage(tableFile, currentPage), 0);
            for (Row row : dataPage.getRows()) {
                if (filter.filterRow(row)) {
                    resultSet.add(row);
                }
            }
            currentPage++;
        }
        return resultSet;
    }


    @Override
    public String name() {
        return name;
    }

    @Override
    public Row schema() {
        return schema;
    }

}
