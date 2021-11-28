package top.yudoge.db.types;

import top.yudoge.db.interfaces.Column;
import top.yudoge.db.utils.Requires;

public class TINYINT extends AbstractColumn<Byte> {

    @Override
    public byte[] toBytes() {
        return new byte[] { value };
    }

    @Override
    public int fromBytes(byte[] bytes, int offset) {
        Requires.byteArrayHasEnoughLength(bytes,offset,1);
        this.value = bytes[offset];
        return 1;
    }

    @Override
    public String typeName() {
        return "TINYINT";
    }

    @Override
    public Class javaType() {
        return Byte.class;
    }

    @Override
    public TINYINT setValue(Byte value) {
        this.value = value;
        return this;
    }

    public static byte[] i2bs(int value) {
        return new TINYINT().setValue((byte) value).toBytes();
    }

    public static byte bs2i(byte bytes[], int offset) {
        TINYINT _int = new TINYINT();
        _int.fromBytes(bytes, offset);
        return _int.getValue();
    }
}
