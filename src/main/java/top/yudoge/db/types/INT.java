package top.yudoge.db.types;

import top.yudoge.db.interfaces.Column;
import top.yudoge.db.utils.Requires;

public class INT extends AbstractColumn<Integer> {

    @Override
    public byte[] toBytes() {
        return new byte[] {
                (byte) (value >> 24 & 0xff),
                (byte) (value >> 16 & 0xff),
                (byte) (value >> 8 & 0xff) ,
                (byte) (value & 0xff)
        };
    }

    @Override
    public int fromBytes(byte[] bytes, int offset) {
        Requires.byteArrayHasEnoughLength(bytes,offset,4);
        this.value = (bytes[offset] & 0xff) << 24 | (bytes[offset+1] & 0xff) << 16 | (bytes[offset+2] & 0xff) << 8 | bytes[offset+3] & 0xff;
        return 4;
    }


    @Override
    public String typeName() {
        return "INT";
    }

    @Override
    public Class javaType() {
        return int.class;
    }

    @Override
    public INT setValue(Integer value) {
        this.value = value;
        return this;
    }

    public static byte[] i2bs(int value) {
        return new INT().setValue(value).toBytes();
    }

    public static int bs2i(byte bytes[], int offset) {
        INT _int = new INT();
        _int.fromBytes(bytes, offset);
        return _int.getValue();
    }
}
