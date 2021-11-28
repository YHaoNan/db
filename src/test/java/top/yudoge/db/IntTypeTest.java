package top.yudoge.db;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import top.yudoge.db.types.INT;
import top.yudoge.db.types.SMALLINT;
import top.yudoge.db.types.TINYINT;
import top.yudoge.db.types.VARCHAR;
import top.yudoge.db.utils.Requires;

public class IntTypeTest {
    @Test
    void testTinyInt() {
        TINYINT tinyint = new TINYINT().setValue(Byte.valueOf((byte) 0xbf));
        assertArrayEquals(tinyint.toBytes(), new byte[]{(byte)0xbf});
        tinyint.fromBytes(new byte[]{(byte)0xab, (byte)0xce},0);
        assertEquals(tinyint.getValue().byteValue(),(byte)0xab);
    }

    @Test
    void testSmallInt() {
        SMALLINT smallint = new SMALLINT().setValue((short) 0xffba);
        assertArrayEquals(smallint.toBytes(), new byte[]{(byte) 0xff, (byte) 0xba});
        smallint.fromBytes(new byte[]{(byte) 0xab,(byte) 0xcd,(byte) 0xef}, 0);
        assertEquals(smallint.getValue().shortValue(), (short) 0xabcd);
    }

    @Test
    void testInt() {
        INT myInt = new INT().setValue(0xabcdefed);
        assertArrayEquals(myInt.toBytes(), new byte[]{(byte) 0xab, (byte) 0xcd, (byte) 0xef, (byte) 0xed});
        myInt.fromBytes(new byte[]{(byte)0xfe,(byte) 0xdc,(byte) 0xba,(byte) 0xab}, 0);
        assertEquals(myInt.getValue().intValue(), 0xfedcbaab);
    }

    @Test
    void testLengthIsNotEnough() {
        INT myInt = new INT();
        assertThrows(Requires.RequirementNotMetException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                myInt.fromBytes(new byte[]{0x00,0x00,0x00},0);
            }
        });
    }
}
