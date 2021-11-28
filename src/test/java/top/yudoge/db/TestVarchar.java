package top.yudoge.db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import top.yudoge.db.types.SMALLINT;
import top.yudoge.db.types.VARCHAR;
import top.yudoge.db.utils.Utils;

import java.nio.charset.StandardCharsets;

public class TestVarchar {
    @Test
    void testVarchar() {
        VARCHAR varchar = new VARCHAR(200).setValue("hello,于小狗");

        byte bytesLength[] = new SMALLINT().setValue((short) 15).toBytes();
        assertArrayEquals(Utils.concatBytes(bytesLength, "hello,于小狗".getBytes(StandardCharsets.UTF_8)), varchar.toBytes());
    }

    @Test
    void testVarchar2() {
        VARCHAR varchar = new VARCHAR(200);
        byte bytesLength[] = new SMALLINT().setValue((short) 15).toBytes();
        varchar.fromBytes(Utils.concatBytes(bytesLength,"hello,于小狗".getBytes(StandardCharsets.UTF_8)),0);
        assertEquals(varchar.getValue(), "hello,于小狗");
    }
}
