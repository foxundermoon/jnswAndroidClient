package com.jnsw.android.xunjian.db;

import com.google.common.base.Strings;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jnsw.android.xunjian.db.data.ChangLiang;
import com.jnsw.android.xunjian.db.data.DataTables;
import com.jnsw.android.xunjian.db.data.GuzhangdianPeizhi;
import com.jnsw.android.xunjian.db.data.TiaoxiaxiangJilu;
import com.jnsw.android.xunjian.db.data.XunjianDian;
import com.jnsw.android.xunjian.db.exception.DataBaseTableClazzHasNoFields;
import com.jnsw.android.xunjian.db.exception.NotDatabaseTableClazzException;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.anotation.LocalField;
import com.jnsw.core.anotation.RemoteTable;
import com.jnsw.core.data.Callback;
import com.jnsw.core.data.Column;
import com.jnsw.core.data.Command;
import com.jnsw.core.data.Message;
import com.jnsw.core.data.Operation;
import com.jnsw.core.data.Row;
import com.jnsw.core.data.Table;
import com.jnsw.core.util.L;
import com.jnsw.core.util.Tip;

import org.json.JSONException;

import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by foxundermoon on 2015/7/29.
 */
public class DataSyncHelper {
    static  boolean debug = BuildConfig.XJ_DB_DEBYG;
    static DataSyncHelper instance;
    public XunjianDatabaseHelper xunjianDatabaseHelper;
    public String lastError;

    public static DataSyncHelper getInstance() {
        if (instance == null) {
            instance = new DataSyncHelper();
        }
        return instance;
    }

    private DataSyncHelper() {
        xunjianDatabaseHelper = OpenHelperManager.getHelper(CustomApplication.getInstance(), XunjianDatabaseHelper.class);
    }

    public void downloadSync(Class<?> dataTableClazz) {
        try {
            syncServer(dataTableClazz);
        } catch (NotDatabaseTableClazzException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void release() {
        xunjianDatabaseHelper = null;
        instance = null;
        lastError = null;
    }

    public void downloadSyncAll() {
        for (Class<?> clazz : DataTables.getTableClazzs()) {
            try {
                syncServer(clazz);
            } catch (NotDatabaseTableClazzException e) {
                e.printStackTrace();
                if(false)
                Tip.longTip(e.getMessage() + clazz.getCanonicalName());
            } catch (SQLException e) {
                e.printStackTrace();
                if(debug)
                Tip.longTip(e.getMessage() + "-----" + clazz.getCanonicalName());
            }
        }
    }

    public void uploadSync(Class<?> dataTableClazz) {
        try {
            syncLocal(dataTableClazz);
        } catch (NotDatabaseTableClazzException e) {
            e.printStackTrace();
            if(debug)
            Tip.longTip(e.getMessage() + "-----" + dataTableClazz.getCanonicalName());

        } catch (SQLException e) {
            e.printStackTrace();
            if(debug)
            Tip.longTip(e.getMessage() + "-----" + dataTableClazz.getCanonicalName());

        } catch (DataBaseTableClazzHasNoFields dataBaseTableClazzHasNoFields) {
            dataBaseTableClazzHasNoFields.printStackTrace();
            if(debug)
            Tip.longTip(dataBaseTableClazzHasNoFields.getMessage() + "-----" + dataTableClazz.getCanonicalName());

        } catch (JSONException e) {
            e.printStackTrace();
            if(debug)
            Tip.longTip(e.getMessage() + "-----" + dataTableClazz.getCanonicalName());

        }
    }

    public void uploadSyncAll() {
        for (Class<?> clazz : DataTables.getTableClazzs()) {
            uploadSync(clazz);
        }
    }

    void syncServer(Class<?> dataTableClazz) throws NotDatabaseTableClazzException, SQLException {
        syncServer(dataTableClazz, null);
    }

    void syncServer(Class<?> dataTableClazz, String conditions) throws NotDatabaseTableClazzException, SQLException {
        DatabaseTable tbanno = dataTableClazz.getAnnotation(DatabaseTable.class);
        RemoteTable remoteTable = dataTableClazz.getAnnotation(RemoteTable.class);
        boolean hasVersion = false;
        try {
            Field version = dataTableClazz.getDeclaredField("版本");
            if (version != null) {
                hasVersion = true;
            }
        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
        }
        if (tbanno == null || remoteTable == null) {
            throw new NotDatabaseTableClazzException();
        }
        try {
            Dao<?, ?> dao = OpenHelperManager.getHelper(CustomApplication.getInstance(), XunjianDatabaseHelper.class).getDao(dataTableClazz);
            syncServer(dao, remoteTable.tableName(), tbanno.tableName(), conditions, hasVersion);
        } catch (SQLException e) {
            e.printStackTrace();

            throw e;
        }
    }

    void syncServer(final Dao<?, ?> dao, String serverTableName, final String localTbName, String conditions, boolean hasVersion) {
        int biggestID = 0;
        try {

            GenericRawResults<String[]> rawResults = dao.queryRaw("select ID from " + localTbName + " order by ID DESC limit 0,10;");
            if (rawResults != null) {
                String[] firstRow = rawResults.getFirstResult();
                if (firstRow != null) {
                    biggestID = Integer.parseInt(firstRow[0]);
                }
            }
            Message query = new Message();
            Class<?> tbClazz = dao.getDataClass();
            final Field[] fields = tbClazz.getDeclaredFields();
            StringBuilder stringBuilder = new StringBuilder("SELECT ");
            for (Field field : fields) {
                if (field.getAnnotation(LocalField.class) == null) {
                    stringBuilder.append(field.getName()).append(" ,");
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append(" FROM ").append(serverTableName);
            String querySql = stringBuilder.toString()+"  WHERE ID >" + biggestID;
            if (!Strings.isNullOrEmpty(conditions)) {
                querySql += " ";
                querySql += conditions;
            }
            query.creatCommand().setName(Command.MySqlDataTable).setOperation(Operation.query)
                    .setSql(querySql);
            query.setCallback(new Callback<Message>() {
                @Override
                public void onCallback(Message message) {
                    if (message.hasError) {
                        Tip.longTip(message.errorMessage);
                    } else if (message.getDataTable() != null && message.getDataTable().getColumns().size() > 0) {
                        for (Row r : message.getDataTable().getRows()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("INSERT INTO ").append(localTbName)
                                    .append("(");
                            StringBuilder sb2 = new StringBuilder();
                            try {

                                for (Map.Entry<String, Object> pair : r.getData().entrySet()) {
                                    if (!"LID".equalsIgnoreCase(pair.getKey())) {
                                        sb.append(pair.getKey()).append(",");
                                        Object v = pair.getValue();
                                        String append = "";
                                        if (v == null || v.toString().equalsIgnoreCase("null")) {
                                        } else {
                                            append = v.toString();
                                        }
                                        sb2.append("'").append(append).append("',");
                                    }
                                }
                            } catch (Exception e) {
                                L.e(DataSyncHelper.class, e.getMessage());
                            }

                            sb.deleteCharAt(sb.length() - 1);
                            sb2.deleteCharAt(sb2.length() - 1);
                            sb.append(")").append(" VALUES (").append(sb2).append(")");
                            try {
                                int effectrow = dao.executeRawNoArgs(sb.toString());
                            } catch (SQLException e) {
                                e.printStackTrace();
                                L.e(DataSyncHelper.class, e.getMessage());
                                lastError = e.getMessage();
                                Tip.shortTip(e.getMessage());
//                                return false;
                            }
                        }

                    }
                }
            });
            try {
                query.send();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //本地数据库有数据的情况
            if (biggestID > 0 && hasVersion) {
                GenericRawResults<String[]> localRawResults = dao.queryRaw("select ID ,版本 from " + localTbName + ";");
                if (localRawResults != null) {
                    List<String[]> result = localRawResults.getResults();
                    if (result != null) {
                        Message update = new Message();
                        String condition = "ID=@ID and 版本>@版本";
                        if (!Strings.isNullOrEmpty(conditions)) {
                            condition += " and ";
                            condition += conditions;
                        }
                        update.creatCommand().setName(Command.MySqlDataTable)
                                .setOperation(Operation.mutiQuery)
                                .setCondition(condition)
                                .setSql(stringBuilder.toString()+" WHERE "+condition);
                        Table tb = update.createTable(new Column("ID", "int"), new Column("版本", "int"))
                                .setName(serverTableName);
                        for (String[] r : result) {
                            try {
                                tb.createRow().put("ID", r[0]).put("版本", r[1]);
                            } catch (Exception e) {
                                e.printStackTrace();
                                L.e(DataSyncHelper.class, e.getMessage());
                                lastError = e.getMessage();
                                Tip.shortTip(e.getMessage());
                            }
                        }
                        update.setCallback(new Callback<Message>() {
                            @Override
                            public void onCallback(Message message) {
                                if (message.hasError) {
                                    Tip.longTip(message.errorMessage);
                                } else if (message.getDataTable().getColumns().size() > 0) {
                                    for (Row r : message.getDataTable().getRows()) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("UPDATE ").append(localTbName)
                                                .append(" SET ");

                                        String id = "";
                                        for (Map.Entry<String, Object> pair : r.getData().entrySet()) {
                                            if (pair.getKey().equalsIgnoreCase("ID")) {
                                                id = pair.getValue().toString();
                                            }
                                            sb.append(pair.getKey()).append("='").append(pair.getValue().toString()).append("' ,");
                                        }
                                        sb.deleteCharAt(sb.length() - 1);
                                        sb.append(" WHERE ID='").append(id).append("';");
                                        try {
                                            int effectrow = dao.executeRawNoArgs(sb.toString());
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                            lastError = e.getMessage();
                                            Tip.shortTip(e.getMessage());
//                                return false;
                                        }
                                    }

                                }

                            }
                        });
                        try {
                            update.send();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            lastError = e.getMessage();

                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void syncLocal(Class<?> dataTableClazz) throws NotDatabaseTableClazzException, SQLException, DataBaseTableClazzHasNoFields, JSONException {
        final DatabaseTable tbanno = dataTableClazz.getAnnotation(DatabaseTable.class);
        RemoteTable remoteTable = dataTableClazz.getAnnotation(RemoteTable.class);
        if (tbanno == null || remoteTable == null) {
            throw new NotDatabaseTableClazzException();
        }
        final Dao<?, ?> dao = OpenHelperManager.getHelper(CustomApplication.getInstance(), XunjianDatabaseHelper.class).getDao(dataTableClazz);
        Field[] fields = dataTableClazz.getDeclaredFields();
        if (fields == null || fields.length < 1) {
            throw new DataBaseTableClazzHasNoFields();
        }
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update ").append(remoteTable.tableName())
                .append(" set ");
        Message message = new Message();
        message.creatCommand().setName(Command.MySqlDataTable)
                .setOperation(Operation.insert);
        StringBuilder sb = new StringBuilder("SELECT ");
        List<Column> columns = new ArrayList<>();
        for (Field field : fields) {
            boolean need = false;
            if (field.getAnnotation(DatabaseField.class) != null) {
                if (field.getAnnotation(LocalField.class) != null) {
                    if (field.getAnnotation(DatabaseField.class).generatedId()) {
                        need = true;
                    }
                } else {
                    need = true;
                }
            }
            if (need) {
                //insert
                sb.append(" ").append(field.getName()).append(" ,");
                columns.add(new Column(field.getName()));
                //update
                updateSql.append(" ").append(field.getName()).append("=@").append(field.getName()).append(",");
            }
        }
        updateSql.deleteCharAt(updateSql.length() - 1)
                .append(" where ID=@ID");
        Table tb = message.createTable(columns);
        tb.setName(remoteTable.tableName());
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" FROM ").append(tbanno.tableName());
//        sb.append(" WHERE ID ='0' ;");
        GenericRawResults<String[]> rawResults = dao.queryRaw(sb.toString() + " WHERE ID ='0' ;");
        if (rawResults != null) {
            boolean hasData = false;
            String[] localColumns = rawResults.getColumnNames();
            for (String[] r : rawResults) {
                Row msgR = tb.createRow();
                hasData = true;
                int i = 0;
                for (String v : r) {
                    try {
                        msgR.put(localColumns[i++], v);
                    } catch (Exception e) {
                        e.printStackTrace();
                        L.e(DataSyncHelper.class, e.getMessage());
                    }
                }
            }
            message.setCallback(new Callback<Message>() {
                @Override
                public void onCallback(Message message) {
                    if (message.hasError) {
                        lastError = message.errorMessage;
                    } else if (message.getDataTable() != null && message.getDataTable().getRows().size() > 0) {
                        for (Row r : message.getDataTable().getRows()) {
                            try {
                                String sql = String.format("update %s set ID='%s' , 数据状态='%s' where LID='%s' ;", tbanno.tableName(), r.get("ID"), DataStatus.Synced.toString(), r.get("LID"));
                                dao.executeRaw(sql);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            if (hasData) {
                message.send();
            }
        }
        // update fixed
        Message upload = new Message();

        upload.creatCommand().setName(Command.MySqlDataTable)
                .setOperation(Operation.update)
                .setSql(updateSql.toString());
        Table tbupdate = upload.createTable(columns);
        GenericRawResults<String[]> updateResults = dao.queryRaw(sb.toString() + " WHERE ID != '0' AND 数据状态='" + DataStatus.Updated + "'");

        if (updateResults != null) {
            boolean hasData = false;
            String[] localColumns = updateResults.getColumnNames();
            for (String[] row : updateResults) {
                hasData = true;
                Row r = tbupdate.createRow();
                int i = 0;
                for (String v : row) {
                    try {
                        r.put(localColumns[i++], v);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            message.setCallback(new Callback<Message>() {
                @Override
                public void onCallback(Message message) {
                    if (message.hasError) {
                        lastError = message.errorMessage;
                    } else if (message.getDataTable() != null && message.getDataTable().getRows().size() > 0) {
                        for (Row r : message.getDataTable().getRows()) {
                            try {
                                String sql = String.format("update %s set  数据状态='%s' where ID='%s' ;", tbanno.tableName(), DataStatus.Synced.toString(), r.get("ID"));
                                dao.executeRaw(sql);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            if (hasData) {
                upload.send();
            }
        }
    }

}