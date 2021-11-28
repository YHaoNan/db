package top.yudoge.db.interfaces;

import top.yudoge.db.structures.Row;

@FunctionalInterface
public interface RowFilter {
    boolean filterRow(Row row);
}
