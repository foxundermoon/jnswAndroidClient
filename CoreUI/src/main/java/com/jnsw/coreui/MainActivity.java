package com.jnsw.coreui;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.jnsw.core.CustomApplication;
import com.jnsw.core.config.ClientConfig;
import com.jnsw.core.data.*;
import com.jnsw.core.event.*;
import com.jnsw.core.xmpp.ServiceManager;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends Activity {
    private Button loginBtn;
    private Button sendTestBtn;
    private TextView textView;
    private ClickListener clickListener;
    private ServiceManager serverceManager;
    private Button exitBtn;
    private Button sendByEventBtn;
    private android.os.Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        sendTestBtn = (Button) findViewById(R.id.sendTestBtn);
        textView = (TextView) findViewById(R.id.textView);
        exitBtn = (Button) findViewById(R.id.exitBtn);
        sendByEventBtn = (Button) findViewById(R.id.sendByEventBus);
        clickListener = new ClickListener();
        sendByEventBtn.setOnClickListener(clickListener);
        loginBtn.setOnClickListener(clickListener);
        sendTestBtn.setOnClickListener(clickListener);
        exitBtn.setOnClickListener(clickListener);
        handler = new Handler();
        CustomApplication.getInstance().eventBus.register(this);
//        ((CustomApplication)getApplication()).eventBus.register(this);
//        CustomApplication.getInstance().eventBus.register(this);
    }

    class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == loginBtn) {
                login();
            }
            if (v == sendTestBtn) {

//                sendMessageTest();
//                Drawable d = getResources().getDrawable(R.drawable.ic_launcher);
//                textView.append( d.getBounds().toString());
                new Thread() {
                    @Override
                    public void run() {
                        try {
                          final  Bitmap pic = CustomApplication.getInstance().httpClient.downloadBitmap("http://10.80.5.222/img/girl_2.jpg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView) findViewById(R.id.imageView)).setImageBitmap(pic);
                                }
                            });
                        } catch (final Throwable throwable) {
                            throwable.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView.append(throwable.getMessage());
                                }
                            });
                        }
                    }
                }.start();
            }

            if (v == exitBtn) {
                if (serverceManager != null) {
                    serverceManager.stopService();
                }
                System.exit(0);
            }
            if (v == sendByEventBtn) {
                upAndDownLoad();
//                sendQueryMessage();
//                sendByEventBtn.setEnabled(false);
//                sendMessageByEventBus();
            }
        }
    }

    private void sendQueryMessage() {
        com.jnsw.core.data.Message message = new com.jnsw.core.data.Message();  //①:新建 Message
        message.creatCommand()
                .setName(Command.DataTable).setOperation(Operation.query)
                //        .setSql("SELECT * FROM `foxdata`.`nj_专题_专业_图层类型划分`");
                .setSql("SELECT * FROM `foxdata`.`线路信息1`")
        .setName("testname"); //②:设置message command
//        message.setCallback(new MessageCallback() {
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

    public void upAndDownLoad(){
        CustomApplication.getInstance() .eventBus.register(this);
        FileMessage f = new FileMessage();
        f.setId(1);
        CustomApplication.getInstance().eventBus.post(new DownloadEvent(f));
        p("download the file with id=1");
        p("downloading.... ");
    }

    @Subscribe
    public void onDownloaded(DownloadedEvent event){
        FileMessage file = event.getEventData();
        p("download success");
        p("name:" + file.getFileName());
        p("md5:" + file.getMd5());
        file.setFileName(file.getFileName() + ".new");
        CustomApplication.getInstance().eventBus.post(new UploadEvent(file));
        p("uploadding.....");
    }
    @Subscribe
    public void onUploaded(UploadedEvent event){
        FileMessage f = event.getEventData();
        p("upload success");
        p("id:" + f.getId());
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
    public void onReceive(final ReceivedMessageEvent event) {
        runAtUI(new Runnable() {
            @Override
            public void run() {
                com.jnsw.core.data.Message msg = event.getEventData();
                Command cmd= msg.getCommand();
                textView.append(msg.toJson());
                textView.append(msg.getJsonCommand());

            }
        });
    }

    private void sendInsertMessage() {
        com.jnsw.core.data.Message message = new com.jnsw.core.data.Message();
        message.creatCommand()
                .setName(Command.DataTable)
                .setOperation(Operation.insert);
        Table tb = message.createTable("用户", "日期", "档案号", "坐标串")
                .setDatabase("foxdata")
                .setName("nj_gps档案记录");
        message.addProperty("info", " the information of table");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 12; i++) {
            Row row = tb.createRow();
            try {
                row.put("用户", "随机生成的第" + i + "个用户")
                        .put("日期", simpleDateFormat.format(Calendar.getInstance(TimeZone.getDefault()).getTime()))
                        .put("档案号", i)
                        .put("坐标串", "117.053038719529,36.6579218009726,117.05307051668,36.6579156466854,117.053104365259,36.6578571809573,117.05305923382,36.6578089723744,117.053012050952,36.6578130752325,117.052979228087,36.657866412388");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        textView.setText(message.toJsonObject().toString());
        try {
            message.send();
        } catch (JSONException e) {
            e.printStackTrace();
            textView.setText(e.getMessage());
        }

//        try {
//            message.send();
//        } catch (JSONException e) {
//            e.printStackTrace();
//
//        }


    }

    @Deprecated
    private void sendMessageByEventBus() {
//        SendXmppPacketEvent<Packet> event = new SendXmppPacketEvent<Packet>();
//        Message message = new Message();
//        message.setBody(Strings.repeat("hello world!", 2));
//        message.setFrom(ClientConfig.getLocalJid());
//        message.setTo(ClientConfig.getServerJid());
//        event.setEventData(message);
//        CustomApplication.getInstance().eventBus.post(event);
//        CustomApplication.getInstance().eventBus.post(new SendStringEvent(message.getBody()));
    }

    private void sendMessageTest() {
//        Row row =
    }

    private void _sendMessageTest() {
        Message message = new Message();
        message.setBody(Strings.repeat("hello word!\n", 100));
        message.setTo(ClientConfig.getServerJid());
        message.setFrom(ClientConfig.getLocalJid());
//        CustomApplication.getInstance().sendPacketByXmppAsync(message);
//        CustomApplication.getInstance().sendStringByXmppAsync(Strings.repeat("hello world by String!\n",100));
    }

    private void login() {
        ClientConfig.Builder.getInstance()
                .setXmppPasword("222")
                .setXmppUser("user1")
                .setXmppServerPort(5222)
                .setXmppServerHost("10.80.5.222")
                .commit().startXmppService();
//                ((CustomApplication)getApplication()).eventBus.register(this);
    }
    @Deprecated
    @Subscribe
    public void ReceivedStringByEventBus(final ReceivedStringEvent event) {
        runAtUI(new Runnable() {
            @Override
            public void run() {
                textView.setText(event.getEventData());
            }
        });
    }

    private void runAtUI(Runnable runnable) {
        handler.post(runnable);
    }


    public void p(final String s){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(s +"\n");
            }
        });
    }
}
