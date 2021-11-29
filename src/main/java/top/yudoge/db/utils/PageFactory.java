package top.yudoge.db.utils;

import top.yudoge.db.constants.Constants;
import top.yudoge.db.constants.PageType;
import top.yudoge.db.structures.*;

public class PageFactory {
    public static TableHeaderPage newTableHeaderPage() {
        TableHeaderPage tableHeaderPage = new TableHeaderPage();
        tableHeaderPage.file_header = new Page.FILE_HEADER();
        tableHeaderPage.file_header.FILE_PAGE_OFFSET = 0;
        tableHeaderPage.page_header = new Page.PAGE_HEADER();
        tableHeaderPage.page_header.PAGE_TYPE = PageType.PAGE_TYPE_TABLE_HEADER;
        tableHeaderPage.page_header.PAGE_TAIL = 0xffff;
        tableHeaderPage.TABLE_PAGE_TAIL = 1;
        return tableHeaderPage;
    }

    public static DataPage newDataPage(Row schema, int pageOffset) {
        DataPage dataPage = new DataPage(schema);
        dataPage.file_header = new Page.FILE_HEADER();
        dataPage.file_header.FILE_PAGE_OFFSET = pageOffset;
        dataPage.page_header = new Page.PAGE_HEADER();
        dataPage.page_header.PAGE_TYPE = PageType.PAGE_TYPE_DATA;
        dataPage.page_header.PAGE_TAIL = Constants.PAGE_PAGE_HEADER_BYTES + Constants.PAGE_FILE_HEADER_BYTES;
        return dataPage;
    }
}
