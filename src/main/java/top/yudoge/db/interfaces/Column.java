package top.yudoge.db.interfaces;

public interface Column<T extends Comparable> extends ByteFormatter {
    T getValue();
    Column<T> setValue(T t);
    String name();
    String typeName();
    Class javaType();

}
