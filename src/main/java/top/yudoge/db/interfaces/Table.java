package top.yudoge.db.interfaces;

import top.yudoge.db.structures.Row;

import java.util.List;

public interface Table {
    String name();
    Row schema();
    void addRow(Row row) throws Exception;
    List<Row> getRows() throws Exception;
    List<Row> getRows(RowFilter filter) throws Exception;
}
