package top.yudoge.db.types;

import top.yudoge.db.interfaces.Column;

public abstract class AbstractColumn<T extends Comparable> implements Column<T> {

    protected String columnName;
    protected T value;

    public Column<T> setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    @Override
    public abstract AbstractColumn<T> setValue(T value);

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String name() {
        return columnName;
    }

    @Override
    public String toString() {
        return "{" +
                "columnName='" + columnName + '\'' +
                ", value=" + (value != null? value.toString() : "[novalue]")+
                '}';
    }
}
