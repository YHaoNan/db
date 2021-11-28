package top.yudoge.db.structures;

import top.yudoge.db.constants.Constants;
import top.yudoge.db.constants.PageType;
import top.yudoge.db.interfaces.ByteFormatter;
import top.yudoge.db.interfaces.RowFilter;
import top.yudoge.db.utils.Requires;
import top.yudoge.db.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringWriter;
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
public class Table {

    private String name;
    private Row schema;
    private File tableFile;
    public Table(String name, Row schema) throws IOException {
        this.name = name;
        this.schema = schema;

        tableFile = new File(Constants.DATA_DIR, name + Constants.TABLE_EXT_NAME);
        Requires.fileExists(tableFile);Requires.fileCanRead(tableFile);Requires.fileCanWrite(tableFile);

    }

    public void addRow(Row row) throws IOException {
        TableHeaderPage tableHeaderPage = getTableHeaderPage();
        DataPage lastDataPage = getDataPage(tableHeaderPage.TABLE_PAGE_TAIL);
        boolean isNewPageCreated = false;

        byte rowBytes[] = row.toBytes();
        // 如果page无法容纳这一行 开启一个新页
        if (Constants.PAGE_SIZE - lastDataPage.page_header.PAGE_TAIL < rowBytes.length) {
            tableHeaderPage.TABLE_PAGE_TAIL += 1;
            lastDataPage = createNewDataPage(schema, tableHeaderPage.TABLE_PAGE_TAIL);
            isNewPageCreated = true;
        }
        lastDataPage.getRows().add(row);
        // 更新当前数据页页尾指针
        lastDataPage.page_header.PAGE_TAIL += rowBytes.length;

        if (isNewPageCreated) Utils.writePage(tableFile, tableHeaderPage);
        Utils.writePage(tableFile, lastDataPage);
    }

    public List<Row> getRows() throws IOException {
        return getRows(row -> true);
    }
    public List<Row> getRows(RowFilter filter) throws IOException {
        TableHeaderPage tableHeaderPage = getTableHeaderPage();
        int currentPage = 1;
        List<Row> resultSet = new ArrayList<>();
        while (currentPage <= tableHeaderPage.TABLE_PAGE_TAIL) {
            DataPage dataPage = new DataPage(schema);
            dataPage.fromBytes(Utils.readPage(tableFile, currentPage), 0);
            for (Row row : dataPage.getRows()) {
                if (filter.filterRow(row)) {
                    resultSet.add(row);
                }
            }
            currentPage++;
        }
        return resultSet;
    }

    private TableHeaderPage getTableHeaderPage() throws IOException {
        TableHeaderPage headerPage = new TableHeaderPage();
        headerPage.fromBytes(Utils.readPage(tableFile, 0), 0);
        return headerPage;
    }
    private DataPage getDataPage(int pageOffset) throws IOException {
        DataPage dataPage = new DataPage(schema);
        dataPage.fromBytes(Utils.readPage(tableFile, pageOffset), 0);
        return dataPage;
    }

    public static Table createNewTable(String tableName, Row schema) throws IOException {
        File tbfile = new File(Constants.DATA_DIR, tableName + Constants.TABLE_EXT_NAME);
        Requires.fileNotExists(tbfile);
        tbfile.createNewFile();

        Table table = new Table(tableName, schema);

        TableHeaderPage tableHeaderPage = new TableHeaderPage();
        tableHeaderPage.file_header = new Page.FILE_HEADER();
        tableHeaderPage.file_header.FILE_PAGE_OFFSET = 0;
        tableHeaderPage.page_header = new Page.PAGE_HEADER();
        tableHeaderPage.page_header.PAGE_TYPE = PageType.PAGE_TYPE_TABLE_HEADER;
        tableHeaderPage.page_header.PAGE_TAIL = 0xffff;
        tableHeaderPage.TABLE_PAGE_TAIL = 1;

        DataPage initialDataPage = createNewDataPage(schema, 1);

        Utils.writePage(table.tableFile, tableHeaderPage);
        Utils.writePage(table.tableFile, initialDataPage);
        return table;
    }

    private static DataPage createNewDataPage(Row schema, int pageOffset) {
        DataPage dataPage = new DataPage(schema);
        dataPage.file_header = new Page.FILE_HEADER();
        dataPage.file_header.FILE_PAGE_OFFSET = pageOffset;
        dataPage.page_header = new Page.PAGE_HEADER();
        dataPage.page_header.PAGE_TYPE = PageType.PAGE_TYPE_DATA;
        dataPage.page_header.PAGE_TAIL = Constants.PAGE_PAGE_HEADER_BYTES + Constants.PAGE_FILE_HEADER_BYTES;
        return dataPage;
    }

}
