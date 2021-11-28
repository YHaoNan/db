package top.yudoge.db.structures;

import top.yudoge.db.constants.Constants;
import top.yudoge.db.interfaces.ByteFormatter;
import top.yudoge.db.interfaces.Column;
import top.yudoge.db.types.AbstractColumn;
import top.yudoge.db.types.SMALLINT;
import top.yudoge.db.types.VariLongColumn;
import top.yudoge.db.utils.Requires;
import top.yudoge.db.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

// 2字节存储行数据占用字节数 剩余的是数据，列不允许空值
public class Row implements ByteFormatter {

    // 这里限制了一行的最大长度，如果添加B+树页，那么这个长度应该除2
    private static final int MAX_ROW_LENGTH = Constants.PAGE_SIZE;

    private List<Column> columns;

    public Row() {
        this.columns = new ArrayList<>();
    }
    public Row(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public byte[] toBytes() {
        byte bytes[] = new byte[]{};
        for (int i=0;i<columns.size();i++) {
            Column column = columns.get(i);
            bytes = Utils.concatBytes(bytes, column.toBytes());
        }

        // 构造  行数据占用字节数 行数据
        bytes = Utils.concatBytes(SMALLINT.i2bs(bytes.length), bytes);

        Requires.numberIsNotBiggerThan(bytes.length, MAX_ROW_LENGTH, "A ROW CAN'T LONGER THAN "+MAX_ROW_LENGTH+" BYTES, BUT GOT : "+bytes.length);
        return bytes;
    }

    @Override
    public int fromBytes(byte[] bytes, int offset) {
        int startOffset = offset;
        SMALLINT smallint = new SMALLINT();
        offset += smallint.fromBytes(bytes,offset);

        for (int i=0;i<columns.size();i++) {
            Column column = columns.get(i);
            offset += column.fromBytes(bytes,offset);
        }

        return offset - startOffset;
    }

    public Row setColumnValue(int index, Comparable value) {
        columns.get(index).setValue(value);
        return this;
    }

    public Column getColumn(int i) {
        return columns.get(i);
    }

    public Column getColumn(String name) {
        for (int i=0;i<columns.size();i++) {
            if (columns.get(i).name().equals(name)) return columns.get(i);
        }
        return null;
    }

    public Row createEmpty() {
        Row newRow = new Row();
        for (int i=0;i<columns.size();i++) {
            Column column = columns.get(i);
            Column newColumn = null;
            try {
                if (column instanceof VariLongColumn) {
                    newColumn = column.getClass().getConstructor(int.class).newInstance(((VariLongColumn)column).getUserLength());
                }else {
                    newColumn = getColumn(i).getClass().getConstructor().newInstance();
                }
                ((AbstractColumn)newColumn).setColumnName(((AbstractColumn)column).name());
                newRow.columns.add(newColumn);
            } catch (Exception e) {
                throw new RuntimeException("Can not create a empty column, error column {" + getColumn(i)+"," +getColumn(i).name() +","+getColumn(i).typeName()+"}, please check the column has specific public constructor!\n" + e.getMessage());
            }
        }
        return newRow;
    }

    @Override
    public String toString() {
        return "Row{" +
                "columns=" + columns +
                '}';
    }
}
