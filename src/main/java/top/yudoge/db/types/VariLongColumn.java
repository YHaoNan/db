package top.yudoge.db.types;

public abstract class VariLongColumn<T extends Comparable> extends AbstractColumn<T>{

    protected int userLength;
    public VariLongColumn(int length) {
        this.userLength = length;
    }

    public int getUserLength() {
        return userLength;
    }

}
