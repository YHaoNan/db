package top.yudoge.db.utils;

import java.io.File;

public class Requires {

    public static class RequirementNotMetException extends RuntimeException {
        public RequirementNotMetException(String message) {super(message);}
    }

    /**
     * byte数组是否具有指定长度，以保证读取不会越界
     *
     *    数组的最大长度maxLength为 bytes.length
     *    如果 maxLength < offset + lengthNeeded
     *    throw RequirementNotMetException
     *
     * @param bytes byte数组
     * @param offset 读取的起始位置
     * @param lengthNeeded 需要读取的长度
     */
    public static void byteArrayHasEnoughLength(byte bytes[], int offset, int lengthNeeded) {
        byteArrayHasEnoughLength(bytes,offset,lengthNeeded,"Byte array doesn't have enough length to read. Bytes Length: "+ lengthNeeded +", Max index you wanna read: "+(offset + lengthNeeded - 1));
    }
    public static void byteArrayHasEnoughLength(byte bytes[], int offset, int lengthNeeded, String message) {
        if (bytes.length < offset + lengthNeeded) throw new RequirementNotMetException(message);
    }

    /**
     * String长度不大于length
     * @param string
     * @param length
     */
    public static void stringIsNotLongerThan(String string, int length) {
        stringIsNotLongerThan(string, length, "String is too long. Max length is : "+length +", got : "+string.length());
    }
    public static void stringIsNotLongerThan(String string, int length, String message) {
        if (string.length()> length) throw new RequirementNotMetException(message);
    }

    public static void numberIsNotBiggerThan(int num, int target) {
        numberIsNotBiggerThan(num,target,"Number is to big. Max : "+target +", got : "+num);
    }
    public static void numberIsNotBiggerThan(int num, int target, String message) {
        if (num > target) throw new RequirementNotMetException(message);
    }

    public static void fileExists(File file) {
        if (!file.exists()) throw new RequirementNotMetException("file "+ file.getName() +" is not found!");
    }
    public static void fileNotExists(File file) {
        if (file.exists()) throw new RequirementNotMetException("file "+ file.getName() +" is already exists!");
    }


    public static void fileCanRead(File file) {
        if (!file.canRead()) throw new RequirementNotMetException("file "+file.getName()+" is not readable. ");
    }
    public static void fileCanWrite(File file) {
        if (!file.canWrite()) throw new RequirementNotMetException("file "+file.getName()+" is not writable. ");
    }
}
