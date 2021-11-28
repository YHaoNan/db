package top.yudoge.db.types;

import top.yudoge.db.interfaces.Column;
import top.yudoge.db.utils.Requires;

public class SMALLINT extends AbstractColumn<Short> {

    @Override
    public byte[] toBytes() {
        return new byte[] { (byte) (value >> 8 & 0xff), (byte) (value & 0xff) };
    }

    @Override
    public int fromBytes(byte[] bytes, int offset) {
        Requires.byteArrayHasEnoughLength(bytes,offset,2);
        value = (short) ((bytes[offset] & 0xff) << 8 | bytes[offset+1] & 0xff);
        return 2;
    }

    @Override
    public String typeName() {
        return "SMALLINT";
    }

    @Override
    public Class javaType() {
        return Short.class;
    }

    @Override
    public SMALLINT setValue(Short value) {
        this.value = value;
        return this;
    }

    /**
     *
     * @param value 虽然是int，但请保证传递的数值在有符号short范围内
     * @return
     */
    public static byte[] i2bs(int value) {
        return new SMALLINT().setValue((short) value).toBytes();
    }

    public static short bs2i(byte bytes[], int offset) {
        SMALLINT _int = new SMALLINT();
        _int.fromBytes(bytes, offset);
        return _int.getValue();
    }
}
