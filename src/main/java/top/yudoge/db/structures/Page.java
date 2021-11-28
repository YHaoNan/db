package top.yudoge.db.structures;


import top.yudoge.db.constants.Constants;
import top.yudoge.db.interfaces.ByteFormatter;
import top.yudoge.db.types.INT;
import top.yudoge.db.types.SMALLINT;
import top.yudoge.db.types.TINYINT;
import top.yudoge.db.utils.Utils;

import java.io.Reader;
import java.util.Arrays;

/**
 * 页结构
 *  文件页头                     |  用来描述该页与文件的关系          |  60字节
 *  页头                         |  描述该页的一些信息               |  60字节
 *  页数据                        |  该页中的数据                    | 直到页尾
 */

/**
 * 文件页头中的内容
 *  * FIL_PAGE_OFFSET              |   该页在文件中的偏移量           |   4字节
 */

/**
 * 页头中的内容
 *  PAGE_TYPE                   |   页类型 0x00 表头页 0x01 数据页 |   1字节
 *  PAGE_TAIL                   |   页尾指针 指向页数据中最后的位置  |  2字节
 */


public abstract class Page implements ByteFormatter {
    public static class FILE_HEADER {
        public int FILE_PAGE_OFFSET;
    }

    public static class PAGE_HEADER {
        public int PAGE_TYPE;
        public int PAGE_TAIL;
    }

    public FILE_HEADER file_header;
    public PAGE_HEADER page_header;

    /**
     * Page.toBytes保证返回页大小的bytes
     * @return
     */
    @Override
    public byte[] toBytes() {
        byte file_header_bytes[] = Utils.copyOf(
                Arrays.asList(
                        INT.i2bs(file_header.FILE_PAGE_OFFSET)
                ),
                Constants.PAGE_FILE_HEADER_BYTES
        );

        byte page_header_bytes[] = Utils.copyOf(
                Arrays.asList(
                        TINYINT.i2bs(page_header.PAGE_TYPE),
                        SMALLINT.i2bs(page_header.PAGE_TAIL)
                ),
                Constants.PAGE_PAGE_HEADER_BYTES
        );

        byte page_data_and_free_space_bytes[] = Utils.copyOf(
                Arrays.asList(
                        pageDataBytes()
                ),
                Constants.PAGE_SIZE - Constants.PAGE_FILE_HEADER_BYTES - Constants.PAGE_FILE_HEADER_BYTES
        );
        return Utils.concatBytes(file_header_bytes, Utils.concatBytes(page_header_bytes, page_data_and_free_space_bytes));
    }

    @Override
    public int fromBytes(byte[] bytes, int offset) {
        offset += resolveFileHeader(bytes, offset);
        offset += resolvePageHeader(bytes, offset);
        offset += resolvePageData(bytes,offset);
        return Constants.PAGE_SIZE;
    }


    /**
     * 返回页面数据区域的数据，子类不需要考虑后面的留空，只返回有数据部分byte[]即可
     * @return
     */
    protected abstract byte[] pageDataBytes();

    /**
     * 解析页面数据区域数据
     * @param bytes
     * @param offset
     * @return
     */
    protected abstract int resolvePageData(byte[] bytes, int offset);


    private int resolveFileHeader(byte[] bytes, int offset) {
        file_header = new FILE_HEADER();
        file_header.FILE_PAGE_OFFSET = INT.bs2i(bytes, offset);
        offset += 4;
        // Do something else...
        return Constants.PAGE_FILE_HEADER_BYTES;
    }

    public int resolvePageHeader(byte[] bytes, int offset) {
        page_header = new PAGE_HEADER();
        page_header.PAGE_TYPE = TINYINT.bs2i(bytes, offset);
        offset += 1;
        page_header.PAGE_TAIL = SMALLINT.bs2i(bytes,offset);
        offset += 2;
        return Constants.PAGE_PAGE_HEADER_BYTES;
    }
}
