package com.jnsw.android.xunjian.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jnsw.android.xunjian.db.data.ChangLiang;
import com.jnsw.android.xunjian.db.data.Note;
import com.jnsw.android.xunjian.db.data.TaskRecord;
import com.jnsw.android.xunjian.db.data.TiaoyaxiangTupian;
import com.jnsw.android.xunjian.db.data.WangLuoLianJie;
import com.jnsw.android.xunjian.db.data.XunjianDian;
import com.jnsw.android.xunjian.db.data.XunjiandianGongzuo;
import com.jnsw.android.xunjian.db.data.XunjianQiandao;
import com.jnsw.android.xunjian.db.data.PaiBan;
import com.jnsw.android.xunjian.db.data.GuzhangTupian;
import com.jnsw.android.xunjian.db.data.GuzhangDian;
import com.jnsw.android.xunjian.db.data.GuzhangdianPeizhi;
import com.jnsw.android.xunjian.db.data.XianLu;
import com.jnsw.android.xunjian.db.data.XianluFenpei;
import com.jnsw.android.xunjian.db.data.XianluJiedian;
import com.jnsw.android.xunjian.db.data.TiaoxiaxiangJilu;
import com.jnsw.android.xunjian.db.data.Yonghu;
import com.jnsw.core.CustomApplication;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class XunjianDatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "巡检系统平板端数据库.db";
    private static final  String DATABASE_DIR= Environment.getExternalStorageDirectory().getPath() + "/巡检系统/"; //"/sdcard/巡检系统/";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = BuildConfig.XJ_DB_VERSION;

    // the DAO object we use to access the SimpleData table
    private Dao<XunjianDian, Integer> 巡检点Dao = null;
    private Dao<XunjiandianGongzuo, Integer> 选检点工作Dao = null;
    private Dao<XunjianQiandao, Integer> 巡检签到Dao = null;
    private Dao<PaiBan, Integer> 排班Dao = null;
    private Dao<GuzhangTupian, Integer> 故障图片Dao = null;
    private Dao<GuzhangDian, Integer> 故障点Dao = null;
    private Dao<GuzhangdianPeizhi, Integer> 故障点配置Dao = null;
    private Dao<XianLu, Integer> 线路Dao = null;
    private Dao<XianluFenpei, Integer> 线路分配Dao = null;
    private Dao<XianluJiedian, Integer> 线路节点Dao = null;
    private Dao<TiaoxiaxiangJilu, Integer> 调压箱记录Dao = null;
    private  Dao<Yonghu,Integer> 用户Dao =null;
    private  Dao<WangLuoLianJie,Integer> 网络连接Dao =null;
    private  Dao<ChangLiang,Integer> 常量配置Dao =null;
    private  Dao<Note,Integer> NoteDao =null;
    private Dao<TiaoyaxiangTupian, Integer> tiaoyaxiangJiluTupianDao = null;
    private Dao<TaskRecord, Integer> taskRecordDao = null;

    public synchronized Dao<TaskRecord, Integer> getTaskRecordDao() throws SQLException {
        if (taskRecordDao == null) {
            taskRecordDao = getDao(TaskRecord.class);
        }
        return taskRecordDao;
    }

    public synchronized  Dao<TiaoyaxiangTupian,Integer> getTiaoyaxiangJiluTupianDao() throws SQLException {
        if (tiaoyaxiangJiluTupianDao == null) {
            tiaoyaxiangJiluTupianDao = getDao(TiaoyaxiangTupian.class);
        }
        return tiaoyaxiangJiluTupianDao;
    }
    public synchronized Dao<Note, Integer> getNoteDao() throws SQLException {
        if (NoteDao == null) {
            NoteDao = getDao(Note.class);
        }
        return NoteDao;
    }
    public synchronized Dao<XunjianDian, Integer> get巡检点Dao() throws SQLException {
        if (巡检点Dao == null) {
            巡检点Dao = getDao(XunjianDian.class);
        }
        return 巡检点Dao;
    }

    public synchronized Dao<XunjiandianGongzuo, Integer> get选检点工作Dao() throws SQLException {
        if (选检点工作Dao == null)
            选检点工作Dao = getDao(XunjiandianGongzuo.class);
        return 选检点工作Dao;
    }

    public synchronized Dao<XunjianQiandao, Integer> get巡检签到Dao() throws SQLException {
        if (巡检签到Dao == null)
            巡检签到Dao = getDao(XunjianQiandao.class);
        return 巡检签到Dao;
    }

    public synchronized Dao<PaiBan, Integer> get排班Dao() throws SQLException {
        if (排班Dao == null)
            排班Dao = getDao(PaiBan.class);
        return 排班Dao;
    }

    public synchronized Dao<GuzhangTupian, Integer> get故障图片Dao() throws SQLException {
        if (故障图片Dao == null)
            故障图片Dao = getDao(GuzhangTupian.class);
        return 故障图片Dao;
    }

    public synchronized Dao<GuzhangDian, Integer> get故障点Dao() throws SQLException {
        if (故障点Dao == null)
            故障点Dao = getDao(GuzhangDian.class);
        return 故障点Dao;
    }

    public synchronized Dao<GuzhangdianPeizhi, Integer> get故障点配置Dao() throws SQLException {
        if (故障点配置Dao == null)
            故障点配置Dao = getDao(GuzhangdianPeizhi.class);
        return 故障点配置Dao;
    }

    public synchronized Dao<XianLu, Integer> get线路Dao() throws SQLException {
        if (线路Dao == null)
            线路Dao = getDao(XianLu.class);
        return 线路Dao;

    }

    public synchronized Dao<XianluFenpei, Integer> get线路分配Dao() throws SQLException {
        if (线路分配Dao == null)
            线路分配Dao = getDao(XianluFenpei.class);
        return 线路分配Dao;
    }

    public synchronized Dao<XianluJiedian, Integer> get线路节点Dao() throws SQLException {
        if(线路节点Dao ==null)
            线路节点Dao =getDao(XianluJiedian.class);
        return 线路节点Dao;
    }

    public synchronized Dao<TiaoxiaxiangJilu, Integer> get调压箱记录Dao() throws SQLException {
        if(调压箱记录Dao ==null)
            调压箱记录Dao =getDao(TiaoxiaxiangJilu.class);
        return 调压箱记录Dao;
    }
    public synchronized Dao<Yonghu, Integer> get用户Dao() throws SQLException {
        if(用户Dao ==null)
            用户Dao =getDao(Yonghu.class);
        return 用户Dao;
    }
    public synchronized Dao<WangLuoLianJie, Integer> get网络连接Dao() throws SQLException {
        if(网络连接Dao ==null)
            网络连接Dao =getDao(WangLuoLianJie.class);
        return 网络连接Dao;
    }
    public synchronized Dao<ChangLiang, Integer> get常量配置Dao() throws SQLException {
        if(常量配置Dao ==null)
            常量配置Dao =getDao(ChangLiang.class);
        return 常量配置Dao;
    }

    public XunjianDatabaseHelper(Context context) {
        super(context,DATABASE_DIR+ DATABASE_NAME, null, DATABASE_VERSION,R.raw.xunjian_db_config);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
           if(!isFolderExists(DATABASE_DIR))
               throw new RuntimeException("data base can not creat");
            Log.d("oncreatdatabase", "run here");
//			Log.i(XunjianDatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, XunjianDian.class);
            TableUtils.createTable(connectionSource, XunjiandianGongzuo.class);
            TableUtils.createTable(connectionSource, XunjianQiandao.class);
            TableUtils.createTable(connectionSource, PaiBan.class);
            TableUtils.createTable(connectionSource, GuzhangTupian.class);
            TableUtils.createTable(connectionSource, GuzhangDian.class);
            TableUtils.createTable(connectionSource, GuzhangdianPeizhi.class);
            TableUtils.createTable(connectionSource, XianLu.class);
            TableUtils.createTable(connectionSource, XianluFenpei.class);
            TableUtils.createTable(connectionSource, XianluJiedian.class);
            TableUtils.createTable(connectionSource, TiaoxiaxiangJilu.class);
            TableUtils.createTable(connectionSource, Yonghu.class);
            TableUtils.createTable(connectionSource, WangLuoLianJie.class);
            TableUtils.createTable(connectionSource, ChangLiang.class);
            TableUtils.createTable(connectionSource, Note.class);
            TableUtils.createTable(connectionSource, TiaoyaxiangTupian.class);
            TableUtils.createTable(connectionSource, TaskRecord.class);
//            TableUtils.createTable(connectionSource, XunjianDian.class);
            try {
                generateTestData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
//			// here we try inserting data in the on-create as a test
//			Dao<SimpleData, Integer> dao = getSimpleDataDao();
//			long millis = System.currentTimeMillis();
//			// create some entries in the onCreate
//			SimpleData simple = new SimpleData(millis);
//			dao.create(simple);
//			simple = new SimpleData(millis + 1);
//			dao.create(simple);
//			Log.i(XunjianDatabaseHelper.class.getName(), "created new entries in onCreate: " + millis);
        } catch (SQLException e) {
            Log.e(XunjianDatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
}

    private void generateTestData() throws ParseException {
        Yonghu yonghu = new Yonghu();
        yonghu.set登录名("大坪用户1");
        yonghu.set密码("222");
        yonghu.set保存密码("1");
        yonghu.setID(4);
        yonghu.set登录日期(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-07-24 12:12:12"));
        Yonghu yonghu2 = new Yonghu();
        yonghu2.set登录名("user3");
        yonghu2.set密码("222");
        yonghu2.set保存密码("1");
        yonghu2.setID(2);
        yonghu2.set登录日期(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-07-25 12:12:12"));
        WangLuoLianJie wangLuoLianJie = new WangLuoLianJie();
        wangLuoLianJie.setID(1);
        wangLuoLianJie.setIP("jnsw.vvfox.com");
        wangLuoLianJie.set端口号("7777");
        wangLuoLianJie.set启用状态("1");
        wangLuoLianJie.set程序状态("1");
        wangLuoLianJie.set类型("网络");
        WangLuoLianJie wangLuoLianJie2 = new WangLuoLianJie();
        wangLuoLianJie2.setID(2);
        wangLuoLianJie2.setIP("jnsw.vvfox.com");
        wangLuoLianJie2.set端口号("7778");
        wangLuoLianJie2.set启用状态("1");
        wangLuoLianJie2.set程序状态("1");
        wangLuoLianJie2.set类型("文件");
        XianLu  xianLu = new XianLu();
        xianLu.setID(2);
        xianLu.setLID(1);
//        xianLu.set线路名称("二环东路");
//        xianLu.set线路类型("常规线路");
//        xianLu.set线路描述("站--->弯头--->弯头--->节点4--->节点4--->节点4");
//        xianLu.set巡检周期(3);
//        xianLu.set是否历史("否");
//        xianLu.set创建时间(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-07-25 12:12:12"));
        PaiBan ban = new PaiBan();
        ban.setID(1);
        ban.set应巡点数(3);
        ban.set用户ID(5);
        ban.set已巡点数(0);
        ban.set执行状态("否");
        ban.set线路ID(2);
        ban.set计划日期(new SimpleDateFormat("yyyy-MM-dd").parse("2015-07-30"));
        ban.set是否历史("否");
        ban.set版本(3);
      try {
          get用户Dao().create(yonghu);
          get用户Dao().create(yonghu2);
          get网络连接Dao().create(wangLuoLianJie);
          get网络连接Dao().create(wangLuoLianJie2);
          get线路Dao().create(xianLu);
          get排班Dao().create(ban);
      }catch (Exception e){
          System.out.println(e.getMessage()+"-----------------------------------");
      }
    }
    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            deleteFile(DATABASE_DIR+DATABASE_NAME);
            Log.i(XunjianDatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, XunjianDian.class, true);
            TableUtils.dropTable(connectionSource, XunjiandianGongzuo.class, true);
            TableUtils.dropTable(connectionSource, XunjianQiandao.class, true);
            TableUtils.dropTable(connectionSource, PaiBan.class, true);
            TableUtils.dropTable(connectionSource, GuzhangTupian.class, true);
            TableUtils.dropTable(connectionSource, GuzhangDian.class, true);
            TableUtils.dropTable(connectionSource, GuzhangdianPeizhi.class, true);
            TableUtils.dropTable(connectionSource, XianLu.class, true);
            TableUtils.dropTable(connectionSource, XianluFenpei.class, true);
            TableUtils.dropTable(connectionSource, XianluJiedian.class, true);
            TableUtils.dropTable(connectionSource, TiaoxiaxiangJilu.class, true);
            TableUtils.dropTable(connectionSource, Yonghu.class,true);
            TableUtils.dropTable(connectionSource,WangLuoLianJie.class,true);
            TableUtils.dropTable(connectionSource,ChangLiang.class,true);
            TableUtils.dropTable(connectionSource,Note.class,true);
           TableUtils.dropTable(connectionSource,TiaoyaxiangTupian.class,true);
            TableUtils.dropTable(connectionSource,TaskRecord.class,true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(XunjianDatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */


    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        巡检点Dao = null;
        选检点工作Dao = null;
        巡检签到Dao = null;
        排班Dao = null;
        故障图片Dao = null;
        故障点Dao = null;
        故障点配置Dao = null;
        线路Dao = null;
        线路分配Dao = null;
        线路节点Dao = null;
        调压箱记录Dao = null;
        用户Dao = null;
        网络连接Dao = null;
        常量配置Dao = null;
        NoteDao = null;
        tiaoyaxiangJiluTupianDao = null;
        taskRecordDao = null;
    }

    private  void  deleteFile(String path){
        File file = new File(path);
        file.deleteOnExit();
    }

    boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            } else {
                return false;

            }
        }
        return true;

    }

    static XunjianDatabaseHelper instace;
    public static XunjianDatabaseHelper getInstace() {
        if (instace == null) {
            instace = OpenHelperManager.getHelper(CustomApplication.getInstance(), XunjianDatabaseHelper.class);
        }
        return instace;
    }

}
