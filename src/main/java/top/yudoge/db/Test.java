package top.yudoge.db;

import top.yudoge.db.constants.Constants;
import top.yudoge.db.interfaces.Column;
import top.yudoge.db.structures.Row;
import top.yudoge.db.structures.Table;
import top.yudoge.db.types.INT;
import top.yudoge.db.types.TINYINT;
import top.yudoge.db.types.VARCHAR;

import java.io.*;
import java.util.Arrays;
import java.util.Date;

public class Test {
    static Row createRow(int id, String name, int age, String address) {
         return new Row(Arrays.asList(
                new INT().setColumnName("id").setValue(id),
                new VARCHAR(10).setColumnName("name").setValue(name),
                new TINYINT().setColumnName("age").setValue((byte) age),
                new VARCHAR(255).setColumnName("address").setValue(address)
        ));
    }
    public static void main(String[] args) throws IOException {

        Row rowSchema = new Row(Arrays.asList(
                new INT().setColumnName("id"),
                new VARCHAR(10).setColumnName("name"),
                new TINYINT().setColumnName("age"),
                new VARCHAR(255).setColumnName("address")
        ));

        Table table = Table.createNewTable("user-"+new Date().getTime(), rowSchema);
        table.addRow(createRow(1, "于小狗", 20, "辽宁工程技术大学北校区5舍"));
        table.addRow(createRow(2, "李弯钩", 10, "辽宁工程技术大学北校区1舍"));
        table.addRow(createRow(3, "张亏内", 80, "辽宁工程技术大学北校区3舍"));

        // 获取年龄大于10的行
        table.getRows(row -> biggerThan(row.getColumn("age").getValue(), (byte) 10))
                .forEach(System.out::println);

    }

    static boolean biggerThan(Comparable a, Comparable b) {
        int result = a.compareTo(b);
        return result > 0;
    }

}
