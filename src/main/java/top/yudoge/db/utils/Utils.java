package top.yudoge.db.utils;

import top.yudoge.db.constants.Constants;
import top.yudoge.db.structures.Page;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

public class Utils {
    public static byte[] concatBytes(byte[] arr1, byte[] arr2) {
        byte[] arr3 = Arrays.copyOf(arr1,arr1.length + arr2.length);
        for (int i=0; i<arr2.length; i++) {
            arr3[arr1.length + i] = arr2[i];
        }

        return arr3;
    }


//    public static byte[] subBytes(byte[] bytes, int start) {
//        return subBytes(bytes,start,bytes.length - start);
//    }

    public static byte[] subBytes(byte[] bytes, int start, int length) {
        byte sub[] = new byte[length];
        for (int i = start ; i < start + length ; i++) {
            sub[i - start] = bytes[i];
        }
        return sub;
    }

    public static byte[] copyOf(List<byte[]> bytes, int length) {
        int tot_length = 0;
        for (int i=0;i<bytes.size();i++) tot_length+=bytes.get(i).length;
        Requires.numberIsNotBiggerThan(tot_length, length, "Can not copy bytes to new byte array, `length` is not bigger enough to copy that. Length: "+length +", elements to copy: "+tot_length);

        int offset = 0;
        byte result [] = new byte[length];
        for (int i=0;i<bytes.size();i++){
            for (int j=0;j<bytes.get(i).length;j++){
                result[offset++] = bytes.get(i)[j];
            }
        }
        return result;
    }

    public static byte[] readPage(File file, int pageOffset) throws IOException {
        byte bytes[] = new byte[Constants.PAGE_SIZE];
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        randomAccessFile.skipBytes(pageOffset * Constants.PAGE_SIZE);
        randomAccessFile.read(bytes, 0 , Constants.PAGE_SIZE);
        randomAccessFile.close();
        return bytes;
    }

    public static void writePage(File file, Page page) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.skipBytes(page.file_header.FILE_PAGE_OFFSET * Constants.PAGE_SIZE);
        randomAccessFile.write(page.toBytes(), 0 , Constants.PAGE_SIZE);
        randomAccessFile.close();
    }

}
