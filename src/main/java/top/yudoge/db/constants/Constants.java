package top.yudoge.db.constants;

import java.nio.charset.Charset;

public class Constants {
    public static final Charset CHARSET = Charset.forName("UTF-8");
    public static final int PAGE_SIZE = 16 * 1024;
    public static final String DATA_DIR = "D:\\source\\java\\db";
    public static final int PAGE_FILE_HEADER_BYTES = 60;
    public static final int PAGE_PAGE_HEADER_BYTES = 60;
    public static final String TABLE_EXT_NAME = ".yudoge";
}
