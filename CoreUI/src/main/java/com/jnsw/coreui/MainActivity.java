package com.jnsw.coreui;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.data.*;
import com.jnsw.core.event.*;
import com.jnsw.core.util.L;
import com.jnsw.core.xmpp.ServiceManager;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;

import java.text.SimpleDateFormat;


@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
    @ViewById
     Button loginBtn;
    @ViewById(R.id.sendTest)
     Button sendTestBtn;
    @ViewById
     TextView textView;
     ClickListener clickListener;
     ServiceManager serverceManager;
    @ViewById
     Button exitBtn;
    @ViewById(R.id.upAndDown)
     Button sendByEventBtn;
     android.os.Handler handler;

    @Click(R.id.loginBtn)
    void _login(){
        login();
    }
    @Click
  void exitBtn(){
        CustomApplication.getInstance().eventBus.post(new ShutdownEvent());
        textView.setText("shut down service");
    }
    @Click(R.id.upAndDown)
    void sendByEventBtn(){
            p("clicked sendQueryMessage");
        sendQueryMessage();
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
//    }

    @AfterViews
    void init(){
        CustomApplication.getInstance().eventBus.register(this);
        handler = new Handler();
    }
    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == loginBtn) {
                p("login....");
                login();
            }
            if (v == sendTestBtn) {
                p("sqlDataTable");
                sqlDataTable();
//                sendQueryMessage();
            }

            if (v == exitBtn) {
                CustomApplication.getInstance().eventBus.post(new ShutdownEvent());
                textView.setText("shut down service");
            }
            if (v == sendByEventBtn) {
                upAndDownLoad();
//                sendQueryMessage();
//                sendByEventBtn.setEnabled(false);
//                sendMessageByEventBus();
            }
        }
    }

    private void sendMutiQueryMessage() {
        com.jnsw.core.data.Message message = new com.jnsw.core.data.Message();
        message.creatCommand().setName(Command.DataTable)
                .setOperation(Operation.mutiQuery)
                .setSql("SELECT * FROM `foxdata`.`nj_专题_抢险_任务表单` WHERE ID=@ID AND 版本>@版本");
        message.setCallback(new MessageCallback() {
            @Override
            public void onCallback(com.jnsw.core.data.Message message) {
                p(message.toJson());
            }
        });
        Table tb = message.createTable("ID", "版本");
        for (int i = 0; i < 20; i++) {
            try {
                tb.createRow().put("ID", i).put("版本", 1);
            } catch (Exception e) {
                e.printStackTrace();
                p(e.getMessage());
            }
        }
        try {
            message.send();
        } catch (JSONException e) {
            e.printStackTrace();
            p(e.getMessage());
        }
    }


    private void taskTest(){
        Task  task = new Task();
        task.setTaskBody(new Runnable() {
            @Override
            public void run() {
                L.d(MainActivity.this.getClass(),"task run : 8 * 8="+(8*8));
            }
        });
        task.setOnFailed(new Runnable() {
            @Override
            public void run() {
                L.d(" task failed----------");
            }
        });
        task.setOnSuccess(new Runnable() {
            @Override
            public void run() {
                L.d("task run success");
            }
        });
        task.post();
    }

    private void sqlDataTable(){
        com.jnsw.core.data.Message message = new Message();
        message.creatCommand().setName(Command.SqlDataTable).setOperation(Operation.runsql)
                .setSql("insert CityDustbinCollectDatabase.dbo.SW_轨迹表(车辆GUID,经度,时间,速度,纬度,状态) values ('测0000',117.112572,'2014/12/29 11:29:54',0.51,36.672970,'android测试状态')");
        try {
            message.send();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void sendQueryMessage() {
        com.jnsw.core.data.Message message = new com.jnsw.core.data.Message();  //①:新建 Message
        message.creatCommand()
                .setName(Command.DataTable).setOperation(Operation.query)
                //        .setSql("SELECT * FROM `foxdata`.`nj_专题_专业_图层类型划分`");
                .setSql("SELECT * FROM `foxdata`.`nj_专题_抢险_任务表单` WHERE ID>5");
        message.setCallback(new MessageCallback() {
            @Override
            public void onCallback(com.jnsw.core.data.Message message) {
                p(message.toJson());
            }
        });


//        message.set_callback(new MessageCallback() {
//            @Override
//            public void onCallback(com.jnsw.core.data.Message message) {
//                final String callBackMsg = message.toJson();
//                runAtUI(new Runnable() {
//                    @Override
//                    public void run() {
//                        textView.setText(callBackMsg);
//                    }
//                });
//            }
//        });

        message.setCallback(new MessageCallback() {
            @Override
            public void onCallback(com.jnsw.core.data.Message message) {
                Table tb = message.getDataTable();
                String p = (String) message.getProperty("key");

            }
        });  //③:设置回调

        try {
            message.send();  //④发送
        } catch (JSONException e) {
        }
    }

    public void upAndDownLoad() {
        CustomApplication.getInstance().eventBus.register(this);
        FileMessage f = new FileMessage();
        f.setId(1);
        CustomApplication.getInstance().eventBus.post(new DownloadEvent(f));
        p("download the file with id=1");
        p("downloading.... ");
    }

    @Subscribe
    public void onDownloaded(DownloadedEvent event) {
        FileMessage file = event.getEventData();
        p("download success");
        p("name:" + file.getFileName());
        p("md5:" + file.getMd5());
        p("isDownload:" + file.isDownloaded());
        file.setFileName(null);
        CustomApplication.getInstance().eventBus.post(new UploadEvent(file));
        p("uploadding.....");
    }

    @Subscribe
    public void onUploaded(UploadedEvent event) {
        FileMessage f = event.getEventData();
        p("upload success");
        p("id:" + f.getId());
    }

    @Subscribe
    public void onLogined(LoginedEvent event) {
        if (event.getEventData().isSuccess()) {
            p(Strings.repeat("-",200));
            p("-----------------恭喜你登录成功------------------");
            p(Strings.repeat("-",200));
        }
        else {
            p(Strings.repeat("-",200));
            p("----------------登录失败  "+event.getEventData().getCause() +"--------------");
            p(Strings.repeat("-",200));
        }
        p(event.getEventData().toString());
    }

    @Subscribe
    public void onUserOnline(HasUserOnLineEvent event) {
        p(event.getEventData() + " ：上线啦");
    }

    @Subscribe
    public void onUserOffLine(HasUserOffLineEvent event) {
        p(event.getEventData() + "：下线啦");
    }

    private void sendCreateMessage() {
        com.jnsw.core.data.Message message = new com.jnsw.core.data.Message();
        message.creatCommand().setName(Command.DataTable).setOperation(Operation.runsql)
                .setSql("CREATE TABLE IF NOT EXISTS `foxdata`.`test` (" +
                        "  `id` int(11) NOT NULL AUTO_INCREMENT," +
                        "  `c` varchar(20) NOT NULL," +
                        "  PRIMARY KEY (`id`)" +
                        ") ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;");
        try {
            message.send();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendUpdateMessage() {
        com.jnsw.core.data.Message message = new com.jnsw.core.data.Message();
        message.creatCommand().setName(Command.DataTable).setOperation(Operation.update)
                .setCondition("ID=@ID AND 档案号=@档案号");
        Table tb = message.createTable("ID", "用户", "档案号")
                .setDatabase("foxdata")
                .setName("nj_gps档案记录");
        for (int i = 10; i < 20; i++) {
            try {
                tb.createRow().put("ID", i).put("用户", "修改后的用户名" + i)
                        .put("档案号", 11);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            message.send();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void sendDeleteMessage() {
        com.jnsw.core.data.Message message = new com.jnsw.core.data.Message();
        message.creatCommand().setName(Command.DataTable).setOperation(Operation.delete)
                .setCondition("ID=@ID AND `档案号`=@档案号");
        Table tb = message.createTable("ID", "档案号")
                .setDatabase("foxdata")
                .setName("nj_gps档案记录");
        for (int i = 0; i < 15; i++) {
            try {
                tb.createRow().put("ID", i).put("档案号", 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            message.send();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void onSendTaskMessage(SendTaskEvent event) {
        p("收到新任务啦");
        com.jnsw.core.data.Message queryTask = new com.jnsw.core.data.Message();
        queryTask.creatCommand()
                .setName(Command.DataTable)
                .setOperation(Operation.query)
                .setSql("SELECT a.* FROM `nj_专题_抢险_任务表单` a , `nj_用户表` b WHERE b.登录名 = '" + ClientConfig.getUsrName() + "' and b.名称 = a.接警人");
        queryTask.setCallback(new MessageCallback() {
            @Override
            public void onCallback(com.jnsw.core.data.Message message) {
                p("receive 任务： " + message.toJson());
            }
        });
        try {
            queryTask.send();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    private void deserilizerJsonMessage() {
        String jmsg = "{\n" +
                "  \"id\": \"b510f5e4-446c-4ab6-b54a-8b666379c333\",\n" +
                "  \"dataTable\": {\n" +
                "    \"columns\": [\n" +
                "      {\n" +
                "        \"name\": \"ID\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"所属编码\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"图层类型\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"rows\": [\n" +
                "      [\n" +
                "        1,\n" +
                "        \"010\",\n" +
                "        \"管点\"\n" +
                "      ],\n" +
                "      [\n" +
                "        2,\n" +
                "        \"010\",\n" +
                "        \"管线\"\n" +
                "      ],\n" +
                "      [\n" +
                "        3,\n" +
                "        \"010\",\n" +
                "        \"管点注记\"\n" +
                "      ],\n" +
                "      [\n" +
                "        4,\n" +
                "        \"010\",\n" +
                "        \"管线注记\"\n" +
                "      ],\n" +
                "      [\n" +
                "        5,\n" +
                "        \"020\",\n" +
                "        \"地形\"\n" +
                "      ],\n" +
                "      [\n" +
                "        6,\n" +
                "        \"030\",\n" +
                "        \"索引\"\n" +
                "      ],\n" +
                "      [\n" +
                "        7,\n" +
                "        \"011\",\n" +
                "        \"设备\"\n" +
                "      ],\n" +
                "      [\n" +
                "        9,\n" +
                "        \"002\",\n" +
                "        \"线路\"\n" +
                "      ],\n" +
                "      [\n" +
                "        10,\n" +
                "        \"002\",\n" +
                "        \"站\"\n" +
                "      ],\n" +
                "      [\n" +
                "        11,\n" +
                "        \"002\",\n" +
                "        \"设施\"\n" +
                "      ],\n" +
                "      [\n" +
                "        12,\n" +
                "        \"002\",\n" +
                "        \"重点场所\"\n" +
                "      ],\n" +
                "      [\n" +
                "        13,\n" +
                "        \"002\",\n" +
                "        \"用户\"\n" +
                "      ],\n" +
                "      [\n" +
                "        14,\n" +
                "        \"005\",\n" +
                "        \"高压电缆\"\n" +
                "      ],\n" +
                "      [\n" +
                "        15,\n" +
                "        \"040\",\n" +
                "        \"流程图\"\n" +
                "      ],\n" +
                "      [\n" +
                "        16,\n" +
                "        \"060\",\n" +
                "        \"工艺流程图\"\n" +
                "      ],\n" +
                "      [\n" +
                "        17,\n" +
                "        \"011\",\n" +
                "        \"节点\"\n" +
                "      ],\n" +
                "      [\n" +
                "        18,\n" +
                "        \"015\",\n" +
                "        \"辅助数据\"\n" +
                "      ],\n" +
                "      [\n" +
                "        19,\n" +
                "        \"011\",\n" +
                "        \"线路\"\n" +
                "      ],\n" +
                "      [\n" +
                "        20,\n" +
                "        \"011\",\n" +
                "        \"注记\"\n" +
                "      ],\n" +
                "      [\n" +
                "        21,\n" +
                "        \"022\",\n" +
                "        \"巡检信息\"\n" +
                "      ],\n" +
                "      [\n" +
                "        22,\n" +
                "        \"023\",\n" +
                "        \"巡检信息\"\n" +
                "      ]\n" +
                "    ],\n" +
                "    \"name\": \"\",\n" +
                "    \"dataBase\": null\n" +
                "  }\n" +
                "}";
        try {
            com.jnsw.core.data.Message message = com.jnsw.core.data.Message.fromJson(jmsg);
            ReceivedMessageEvent receivedMessageEvent = new ReceivedMessageEvent(message);
            CustomApplication.getInstance().eventBus.register(this);
//            CustomApplication.getInstance().eventBus.post(receivedMessageEvent);
            receivedMessageEvent.post();
//            textView.setText(message.toJson());
        } catch (final Exception e) {
            runAtUI(new Runnable() {
                @Override
                public void run() {
                    textView.setText(e.getMessage());
                }
            });

        }
    }

    @Subscribe
    public void onReceive(ReceivedMessageEvent event) {
        final com.jnsw.core.data.Message msg = event.getEventData();
        p("receive a message  id:" + msg.getId());
//        runAtUI(new Runnable() {
//            @Override
//            public void run() {
//                textView.append(msg.toJson());
//                textView.append(msg.getJsonCommand());
//
//            }
//        });
    }

    private void sendInsertMessage() {
        com.jnsw.core.data.Message message = new com.jnsw.core.data.Message();
        message.creatCommand()
                .setName(Command.DataTable)
                .setOperation(Operation.insert);
        Table tb = message.createTable(new Column("停气时间"), new NowColumn("上报时间"), new Column("LID"));

        tb.setDatabase("foxdata")
                .setName("nj_now_test");
        message.addProperty("info", " the information of table");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 5; i++) {
            Row row = tb.createRow();
            try {
                row.put("停气时间", "2018-12-21 12:21:12")
                        .put("LID", i + 200);
//                row.put("now","--");
//                        .put("日期", simpleDateFormat.format(Calendar.getInstance(TimeZone.getDefault()).getTime()))
//                        .put("档案号", i)
//                        .put("坐标串", "117.053038719529,36.6579218009726,117.05307051668,36.6579156466854,117.053104365259,36.6578571809573,117.05305923382,36.6578089723744,117.053012050952,36.6578130752325,117.052979228087,36.657866412388");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        p(message.toJsonObject().toString());
        p(Strings.repeat("-", 100));
        message.setCallback(new MessageCallback() {
            @Override
            public void onCallback(com.jnsw.core.data.Message message) {
                p(message.toJson());
            }
        });

        try {
            message.send();
        } catch (JSONException e) {
            e.printStackTrace();
            p(e.getMessage());
        }
    }

    public void castTable(){
        Message msg = MessageFactory
                .creatQueryMessage("SELECT * FROM `foxdata`.`nj_专题_巡检_old任务分配表` WHERE 1");
        msg.setCallback(new Callback<Message>() {

            public void onCallback(Message msg) {
                L.d(this.getClass(), "receive task msg:" + msg.toJson());
             /*   swTable swtb = null;
                try {
                    swtb = com.jnsw.gas.services.TableCast
                            .Table2swTable(msg.getDataTable());
                } catch (Exception e3) {
                    L.e(this.getClass(), "Table2swTable exception:" + e3.getMessage());
                }

                L.d(this.getClass(), "transfomat to swTable:");*/
            }
        });
    }
    public void mutiUpload() {
        MutiFileMessage mutiFileMessage = new MutiFileMessage();
        for (int i = 0; i < 10; i++) {
            FileMessage fileMessage = new FileMessage();
            fileMessage.setFileName("testfile." + i);
            fileMessage.setData(Strings.repeat("hello world " + i, 100).getBytes());
            mutiFileMessage.addFile(fileMessage);
        }
        mutiFileMessage.setCallback(new Callback<MutiFileMessage>() {
            @Override
            public void onCallback(MutiFileMessage mutiFileMessage) {
                if (mutiFileMessage.isUploadSuccess()) {
                    p("all uploaded success");
                    for (FileMessage f : mutiFileMessage.getFiles()) {
                        p("name:" + f.getFileName() + "   id:" + f.getId());
                    }
                } else {
                    p("upload with error");
                    for (String err : mutiFileMessage.getErrorMessage()) {
                        p(err + "\n");
                    }
                }
            }
        }).sendToUpload();
    }

    private void login() {
        ClientConfig.Builder.getInstance()
                .setXmppPasword("222")
                .setXmppUser("user2")
                .setXmppServerPort(7777)
                .setXmppServerHost("jnsw.vvfox.com")
                .setFileServerUrl("http://10.80.5.199:8080/")
                .setXmppResource("XJAPP")
                .commit();//.startXmppService();
        new LoginMessage().post();
    }

    private void runAtUI(Runnable runnable) {
        handler.post(runnable);
    }


    public void p(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(s + "\n");
            }
        });
    }
}
