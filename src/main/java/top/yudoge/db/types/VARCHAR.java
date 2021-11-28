package top.yudoge.db.types;

import top.yudoge.db.interfaces.Column;
import top.yudoge.db.constants.Constants;
import top.yudoge.db.utils.Requires;
import top.yudoge.db.utils.Utils;

// 两字节存储长度 剩下是数据
public class VARCHAR extends VariLongColumn<String> {

    /* 最大字符数，21845保证使用最多3字节的编码时占用的字节数也不超过65535，那么可以用2个字节存储变长列占用字节数 */
    /* 但实际上，Row限制了一行所占用的字节数不能超过16KB，也就是16384，我们也没有溢出页的实现，所以，21845根本用不了 */
    public static final int MAX_CHAR_COUNT = 21845;

    public VARCHAR(int length){
        super(length);
        Requires.numberIsNotBiggerThan(length, MAX_CHAR_COUNT, "VARCHAR LENGTH SHOULD SMALLER THEN "+MAX_CHAR_COUNT +" GOT: "+length);
    }

    @Override
    public byte[] toBytes() {
        byte[] contentBytes = value.getBytes(Constants.CHARSET);
        // 因为封装了setValue，所以此处的bytes长度，使用short必然能够容纳
        byte[] contentByteLength = new SMALLINT().setValue((short) contentBytes.length).toBytes();

        return Utils.concatBytes(contentByteLength, contentBytes);
    }

    @Override
    public int fromBytes(byte[] bytes, int offset) {
        short bytesToRead = SMALLINT.bs2i(bytes, offset);
        offset += 2;

        Requires.byteArrayHasEnoughLength(bytes, offset, bytesToRead);

        byte contentBytes[] = Utils.subBytes(bytes, offset, bytesToRead);
        setValue(new String(contentBytes,0,contentBytes.length,Constants.CHARSET));

        return bytesToRead + 2;
    }

    @Override
    public VARCHAR setValue(String s) {
        Requires.stringIsNotLongerThan(s, getUserLength(), "DATA IS TOO LONG TO SAVE TO VARCHAR("+getUserLength()+"), STRING LENGTH: "+ s.length());
        this.value = s;
        return this;
    }

    @Override
    public String typeName() {
        return "VARCHAR";
    }

    @Override
    public Class javaType() {
        return String.class;
    }
}
