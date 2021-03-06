package com.taobao.tddl.qatest.matrix.basecrud;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.taobao.tddl.qatest.BaseMatrixTestCase;
import com.taobao.tddl.qatest.BaseTestCase;
import com.taobao.tddl.qatest.ExecuteTableName;
import com.taobao.tddl.qatest.util.EclipseParameterized;

/**
 * Comment for LocalServerInsertTest
 * <p/>
 * Author By: yaolingling.pt Created Date: 2012-2-20 下午01:40:43
 */
@RunWith(EclipseParameterized.class)
public class InsertTest extends BaseMatrixTestCase {

    @Parameters(name = "{index}:table={0}")
    public static List<String[]> prepareData() {
        return Arrays.asList(ExecuteTableName.normaltblTable(dbType));
    }

    public InsertTest(String tableName){
        BaseTestCase.normaltblTableName = tableName;
    }

    @Before
    public void initData() throws Exception {
        tddlUpdateData("delete from  " + normaltblTableName, null);
        mysqlUpdateData("delete from  " + normaltblTableName, null);
    }

    @Test
    public void insertAllFieldTest() throws Exception {
        String sql = "insert into " + normaltblTableName + " values(?,?,?,?,?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(RANDOM_INT);
        param.add(gmtDay);
        param.add(gmt);
        param.add(gmt);
        param.add(null);
        param.add(fl);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = new String[] { "PK", "ID", "GMT_CREATE", "NAME", "FLOATCOL", "GMT_TIMESTAMP",
                "GMT_DATETIME" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);

    }

    @Test
    public void insertMultiValuesTest() throws Exception {
        StringBuilder sql = new StringBuilder("insert into " + normaltblTableName + " values ");
        List<Object> param = new ArrayList<Object>();

        for (int i = 0; i < 40; i++) {
            if (i == 0) {
                sql.append("(?,?,?,?,?,?,?)");
            } else {
                sql.append(",(?,?,?,?,?,?,?)");
            }
            param.add(RANDOM_ID + i);
            param.add(RANDOM_INT + i);
            param.add(gmtDay);
            param.add(gmt);
            param.add(gmt);
            param.add(null);
            param.add(fl);
        }

        execute(sql.toString(), param);

        String selectSql = "select * from " + normaltblTableName;
        String[] columnParam = new String[] { "PK", "ID", "GMT_CREATE", "NAME", "FLOATCOL", "GMT_TIMESTAMP",
                "GMT_DATETIME" };
        selectContentSameAssert(selectSql, columnParam, Collections.EMPTY_LIST);

    }

    @Test
    public void insertLastInsertIdTest() throws Exception {

        String sql = "insert into test_table_autoinc (name) values(?)";
        List<Object> param = new ArrayList<Object>();
        param.add("test");

        execute(sql, param);
        //
        // sql = "select last_insert_id() a";
        //
        // String[] columnParam = new String[] { "a" };
        // selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
    }

    @Test
    public void insertLastInsertIdTransactionTest() throws Exception {

        mysqlConnection.setAutoCommit(false);
        tddlConnection.setAutoCommit(false);

        String sql = "insert into " + normaltblTableName + " values(?,?,?,?,?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(RANDOM_INT);
        param.add(gmtDay);
        param.add(gmt);
        param.add(gmt);
        param.add(null);
        param.add(fl);
        execute(sql, param);

        // sql = "select last_insert_id()";
        //
        // param.clear();

        mysqlConnection.commit();
        tddlConnection.commit();

        mysqlConnection.setAutoCommit(true);
        tddlConnection.setAutoCommit(true);
    }

    @Test
    public void insertIgnoreTest() throws Exception {
        if (normaltblTableName.startsWith("ob")) {
            // ob不支insert ignore
            return;
        }

        String sql = "insert ignore into " + normaltblTableName + " values(?,?,?,?,?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(RANDOM_INT);
        param.add(gmtDay);
        param.add(gmt);
        param.add(gmt);
        param.add(name);
        param.add(fl);

        execute(sql, param);
        execute(sql, param);
        execute(sql, param);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = { "PK", "ID", "GMT_CREATE", "NAME", "FLOATCOL", "GMT_TIMESTAMP", "GMT_DATETIME" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
    }

    @Test
    public void insertLOW_PRIORITYTest() throws Exception {
        if (normaltblTableName.startsWith("ob")) {
            // ob不支持
            return;
        }

        String sql = "insert LOW_PRIORITY  into " + normaltblTableName + " values(?,?,?,?,?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(RANDOM_INT);
        param.add(gmtDay);
        param.add(gmt);
        param.add(gmt);
        param.add(name);
        param.add(fl);

        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = { "PK", "ID", "GMT_CREATE", "NAME", "FLOATCOL", "GMT_TIMESTAMP", "GMT_DATETIME" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
    }

    @Test
    public void insertHIGH_PRIORITYTest() throws Exception {

        if (normaltblTableName.startsWith("ob")) {
            // ob不支持
            return;
        }

        String sql = "insert HIGH_PRIORITY  into " + normaltblTableName + " values(?,?,?,?,?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(RANDOM_INT);
        param.add(gmtDay);
        param.add(gmt);
        param.add(gmt);
        param.add(name);
        param.add(fl);

        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = { "PK", "ID", "GMT_CREATE", "NAME", "FLOATCOL", "GMT_TIMESTAMP", "GMT_DATETIME" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
    }

    @Test
    public void insertSomeFieldTest() throws Exception {
        String sql = "insert into " + normaltblTableName + " (pk,floatCol,gmt_timestamp)values(?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(fl);
        param.add(gmt);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = { "PK", "GMT_TIMESTAMP", "FLOATCOL" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
    }

    @Test
    public void insertGmtStringTest() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String gmtString = df.format(gmt);
        String sql = "insert into " + normaltblTableName + " (pk,gmt_create,gmt_timestamp,gmt_datetime)values(?,?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(df.format(gmtDay));
        param.add(gmtString);
        param.add(gmtString);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = { "PK", "GMT_CREATE", "GMT_TIMESTAMP", "GMT_DATETIME" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
    }

    @Test
    public void insertWithSetTest() throws Exception {
        String sql = "insert into " + normaltblTableName + " set pk=? ,name=?";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(name);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = { "PK", "NAME" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
    }

    @Test
    public void insertWithMutilTest() throws Exception {
        String sql = "insert into " + normaltblTableName + "(pk,id) values(?,?),(?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(RANDOM_INT);
        param.add(RANDOM_ID + 1);
        param.add(RANDOM_INT);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = { "PK", "ID" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID + 1;
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
    }

    @Test
    public void insertWithMutil_sequenceTest() throws Exception {
        if (normaltblTableName.startsWith("ob_")) {
            return;
        }

        String sql = "insert into " + normaltblTableName + "(pk,id) values(" + normaltblTableName + ".nextval,?),("
                     + normaltblTableName + ".nextval,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_INT);
        param.add(RANDOM_INT);
        tddlUpdateData(sql, param);

        sql = "select last_insert_id()";
        ResultSet rc = tddlQueryData(sql, Collections.EMPTY_LIST);
        Assert.assertTrue(rc.next());
        System.out.println(rc.getLong(1));
    }

    @Ignore(value = "目前不支持insert中带select的sql语句")
    @Test
    public void insertWithSelectTest() throws Exception {
        tddlUpdateData("insert into student(id,name,school) values (?,?,?)",
            Arrays.asList(new Object[] { RANDOM_ID, name, school }));
        mysqlUpdateData("insert into student(id,name,school) values (?,?,?)",
            Arrays.asList(new Object[] { RANDOM_ID, name, school }));

        String sql = "insert into " + normaltblTableName + "(pk,name) select id,name from student where school=?";
        List<Object> param = new ArrayList<Object>();
        param.add(school);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = { "name" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);

        tddlUpdateData("delete from student where school=?", Arrays.asList(new Object[] { school }));
        mysqlUpdateData("delete from student where school=?", Arrays.asList(new Object[] { school }));
    }

    @Test
    public void insertPramLowerCaseTest() throws Exception {
        String sql = "insert into " + normaltblTableName + " (pk,floatcol,gmt_create)values(?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(fl);
        param.add(gmtDay);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = { "PK", "GMT_CREATE", "FLOATCOL" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
    }

    @Test
    public void insertPramUppercaseTest() throws Exception {
        String sql = "INSERT INTO " + normaltblTableName + " (PK,FLOATCOL,GMT_CREATE)VALUES(?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(fl);
        param.add(gmtDay);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = { "PK", "GMT_CREATE", "FLOATCOL" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
    }

    /**
     * 不带参数的sql语句测试，暂时不对mysql数据库进行测试
     * 
     * @throws Exception
     */
    @Test
    public void insertWithBdbOutParamTest() throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "insert into " + normaltblTableName + "(pk,gmt_create,gmt_timestamp,gmt_datetime,id) values("
                     + RANDOM_ID + ",'" + df.format(gmtDay) + "','" + df.format(gmt) + "','" + df.format(gmt) + "',"
                     + RANDOM_INT + ")";
        execute(sql, Collections.EMPTY_LIST);

        sql = "select * from " + normaltblTableName + " where pk=" + RANDOM_ID;
        String[] columnParam = { "PK", "GMT_CREATE", "ID", "GMT_TIMESTAMP", "GMT_DATETIME" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);
    }

    @Test
    public void insertWithNullTest() throws Exception {
        String sql = "insert into " + normaltblTableName + "(pk,name) values(?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(null);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=?";
        param.clear();
        param.add(RANDOM_ID);
        String[] columnParam = { "PK", "NAME" };
        selectOrderAssert(sql, columnParam, param);
    }

    @Test
    public void insertWithOutKeyFieldTest() throws Exception {

        String sql = "insert into " + normaltblTableName + " (id,floatCol,gmt_create)values(?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_INT);
        param.add(fl);
        param.add(gmtDay);
        try {
            tddlUpdateData(sql, param);
            Assert.fail();
        } catch (Exception e) {
            // TODO 单库多表抛出"insert not support muti tables",需要以后最终确认应该抛出怎样的异常
            // throw e;
            // Assert.assertTrue(e.getMessage(),e.getMessage().contains("pk must not null"));
            // shenxun : 不一样的异常。。。暂时不用上面的异常吧。。
        }
    }

    @Test
    public void insertWithZoreAndNegativeTest() throws Exception {
        long pk = -1l;
        int id = -1;
        String sql = "insert into " + normaltblTableName + " (pk,id)values(?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(pk);
        param.add(id);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + pk;
        String[] columnParam = { "PK", "ID" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);

        tddlUpdateData("delete from " + normaltblTableName + " where pk=?", Arrays.asList(new Object[] { pk }));
        mysqlUpdateData("delete from " + normaltblTableName + " where pk=" + pk, null);

        pk = 0;
        id = 0;
        sql = "insert into " + normaltblTableName + " (pk,id)values(?,?)";
        param = new ArrayList<Object>();
        param.add(pk);
        param.add(id);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + pk;
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);

        tddlUpdateData("delete from " + normaltblTableName + " where pk=" + pk, null);
        mysqlUpdateData("delete from " + normaltblTableName + " where pk=" + pk, null);
    }

    @Test
    public void insertWithMaxMinTest() throws Exception {
        long pk = Long.MAX_VALUE;
        int id = Integer.MAX_VALUE;
        String sql = "insert into " + normaltblTableName + " (pk,id)values(?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(pk);
        param.add(id);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + pk;
        String[] columnParam = { "PK", "ID" };
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);

        tddlUpdateData("delete from " + normaltblTableName + " where pk=?", Arrays.asList(new Object[] { pk }));
        mysqlUpdateData("delete from " + normaltblTableName + " where pk=" + pk, null);

        pk = Long.MIN_VALUE;
        id = Integer.MIN_VALUE;
        sql = "insert into " + normaltblTableName + " (pk,id)values(?,?)";
        param = new ArrayList<Object>();
        param.add(pk);
        param.add(id);
        execute(sql, param);

        sql = "select * from " + normaltblTableName + " where pk=" + pk;
        selectOrderAssert(sql, columnParam, Collections.EMPTY_LIST);

        tddlUpdateData("delete from " + normaltblTableName + " where pk=?", Arrays.asList(new Object[] { pk }));
        mysqlUpdateData("delete from " + normaltblTableName + " where pk=" + pk, null);
    }

    @Test
    public void insertWithNowTest() throws Exception {
        String sql = "insert into " + normaltblTableName + "(pk,gmt_timestamp,id) values(" + RANDOM_ID + ",now()," + 1
                     + ")";
        mysqlUpdateData(sql, null);
        tddlUpdateData(sql, null);

        sql = "select * from " + normaltblTableName + " where pk=" + 1;
        rs = mysqlQueryData(sql, null);
        rc = tddlQueryData(sql, null);
        String[] columnParam = { "gmt_timestamp" };
        assertOrder(rs, rc, columnParam);
    }

    @Test
    public void insert_SequenceTest() throws Exception {
        String sql = "insert into " + normaltblTableName + " values(" + normaltblTableName + ".nextval,?,?,?,?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_INT);
        param.add(gmtDay);
        param.add(gmt);
        param.add(gmt);
        param.add(null);
        param.add(fl);
        int affect = tddlUpdateData(sql, param);
        Assert.assertEquals(1, affect);
    }

    @Test
    public void insertErrorTypeFiledTest() throws Exception {
        String sql = "insert into " + normaltblTableName + " (pk,gmt_create)values(?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(fl);
        try {
            tddlUpdateData(sql, param);
            if (!normaltblTableName.contains("mysql") && !normaltblTableName.contains("ob")) {
                Assert.fail();
            }
        } catch (Exception ex) {
            if (!normaltblTableName.contains("mysql") && !normaltblTableName.contains("ob")) {
                Assert.assertTrue(ex.getMessage().contains("is not supported"));
            } else {
                Assert.assertTrue(ex.getMessage().contains("is not supported"));
            }
        }

    }

    @Test
    public void insertNotExistFileTest() throws Exception {
        String sql = "insert into " + normaltblTableName + " (pk,gmts)values(?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(gmt);
        try {
            tddlUpdateData(sql, param);
            Assert.fail();
        } catch (Exception ex) {
            // Assert.assertTrue(ex.getMessage().contains("GMTS is not existed "));
        }

    }

    @Test
    public void insertNotMatchFieldTest() throws Exception {
        String sql = "insert into " + normaltblTableName + " (id,floatCol) values(?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(RANDOM_ID);
        param.add(fl);
        param.add(gmt);
        try {
            tddlUpdateData(sql, param);
            Assert.fail();
        } catch (Exception ex) {
            // Assert.assertTrue(ex.getMessage().contains("The size of the columns and values is not matched"));
        }
    }

    @Test
    public void insertNotMatchParameterTest() throws Exception {
        String sql = "insert into " + normaltblTableName + " (id,floatCol) values(?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(gmt);
        try {
            tddlUpdateData(sql, param);
            Assert.fail();
        } catch (Exception ex) {
            Assert.assertNotNull(ex);
        }
    }
}
