package top.yudoge.db.structures;

import top.yudoge.db.types.INT;
import top.yudoge.db.utils.Utils;

public class TableHeaderPage extends Page{
    public int TABLE_PAGE_TAIL;

    @Override
    protected byte[] pageDataBytes() {
        return INT.i2bs(TABLE_PAGE_TAIL);
    }

    @Override
    protected int resolvePageData(byte[] bytes, int offset) {
        int startOffset = offset;
        TABLE_PAGE_TAIL = INT.bs2i(bytes,offset);
        offset += 4;
        return offset - startOffset;
    }

}
