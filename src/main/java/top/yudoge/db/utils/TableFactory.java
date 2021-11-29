package top.yudoge.db.utils;

import top.yudoge.db.constants.Constants;
import top.yudoge.db.interfaces.PageManager;
import top.yudoge.db.interfaces.Table;
import top.yudoge.db.structures.DataPage;
import top.yudoge.db.structures.DefaultTable;
import top.yudoge.db.structures.Row;
import top.yudoge.db.structures.TableHeaderPage;

import java.io.File;
import java.io.IOException;

public class TableFactory {
    public static Table createNewTable(String tableName, Row schema, PageManager pageManager) throws IOException {
        File tbfile = new File(Constants.DATA_DIR, tableName + Constants.TABLE_EXT_NAME);
        Requires.fileNotExists(tbfile);
        tbfile.createNewFile();

        DefaultTable table = new DefaultTable(tableName, schema, pageManager);
        TableHeaderPage tableHeaderPage = PageFactory.newTableHeaderPage();
        DataPage initialDataPage = PageFactory.newDataPage(schema, 1);

        Utils.writePage(tbfile, tableHeaderPage);
        Utils.writePage(tbfile, initialDataPage);
        return table;
    }
}
