package top.yudoge.db.structures;

import top.yudoge.db.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DataPage extends Page {

    private Row schema;
    private List<Row> rows;

    public DataPage(Row schema) {
        this.schema = schema;
        this.rows = new ArrayList<>();
    }

    @Override
    protected byte[] pageDataBytes() {
        AtomicInteger totBytes = new AtomicInteger();
        return Utils.copyOf(rows.stream().map(row -> {
            byte rowBytes[] = row.toBytes();
            totBytes.addAndGet(rowBytes.length);
            return rowBytes;
        }).collect(Collectors.toList()), totBytes.get());
    }

    @Override
    protected int resolvePageData(byte[] bytes, int offset) {
        int startOffset = offset;
        while (offset < page_header.PAGE_TAIL) {
            Row row = schema.createEmpty();
            offset += row.fromBytes(bytes,offset);
            rows.add(row);
        }
        return offset - startOffset;
    }

    public List<Row> getRows() {
        return rows;
    }
}
