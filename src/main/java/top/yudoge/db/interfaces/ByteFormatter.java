package top.yudoge.db.interfaces;

/**
 * 一个ByteFormatter可以将自己转化成Byte数组，也可以从指定的Byte数组中装载自己
 */
public interface ByteFormatter {
    /**
     * 将自己转换成byte数组
     * @return
     */
    byte[] toBytes();

    /**
     * 从byte数组中装载自己
     * @param bytes byte数组
     * @param offset 从该byte数组的哪个位置开始
     * @return 本次装载读取的字节数
     */
    int fromBytes(byte[] bytes, int offset);
}
