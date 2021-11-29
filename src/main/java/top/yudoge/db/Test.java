package top.yudoge.db;

import top.yudoge.db.interfaces.Table;
import top.yudoge.db.structures.Row;
import top.yudoge.db.types.INT;
import top.yudoge.db.types.TINYINT;
import top.yudoge.db.types.VARCHAR;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Test {
    static Row createRow(int id, String name, int age, String address) {
         return new Row(Arrays.asList(
                new INT().setColumnName("id").setValue(id),
                new VARCHAR(20).setColumnName("name").setValue(name),
                new TINYINT().setColumnName("age").setValue((byte) age),
                new VARCHAR(255).setColumnName("address").setValue(address)
        ));
    }
    public static void main(String[] args) throws Exception {

        Row rowSchema = new Row(Arrays.asList(
                new INT().setColumnName("id"),
                new VARCHAR(20).setColumnName("name"),
                new TINYINT().setColumnName("age"),
                new VARCHAR(255).setColumnName("address")
        ));

        DogeDB db = new DogeDB();
        Table table = db.createNewTable("user3", rowSchema);

        System.out.println("开始写入1000w条数据");
        long writeStartTime = new Date().getTime();
        for(int i=0;i < 10000000; i++) {
            table.addRow(createRow(i, "于小狗"+i, new Random().nextInt(100), "辽宁工程技术大学北校区"+i+"舍"));
        }
        long writeEndTime = new Date().getTime();
        System.out.println("写入结束，耗费时间："+((writeEndTime - writeStartTime)/1000.0) + "s");


        long searchStartTime = new Date().getTime();
        System.out.println("开始查询年龄>98的数据：");
        List<Row> rows = table.getRows(row -> biggerThan(row.getColumn("age").getValue(), (byte)98));
        long searchEndTime = new Date().getTime();
        System.out.println("查询完成，共"+ rows.size()+"条数据，耗费时间："+((searchEndTime - searchStartTime)/1000.0)+"s");

        db.getPageManager().flushAllPage();
    }

    static boolean biggerThan(Comparable a, Comparable b) {
        int result = a.compareTo(b);
        return result > 0;
    }

}
