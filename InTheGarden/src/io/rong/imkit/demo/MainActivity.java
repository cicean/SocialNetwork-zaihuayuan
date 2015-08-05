package io.rong.imkit.demo;

import com.gzqining.inthegarden.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import io.rong.imkit.RongIM;
import io.rong.imkit.demo.message.GroupInvitationNotification;
import io.rong.imkit.view.ActionBar;
import io.rong.imkit.view.AlterDialog;
import io.rong.imlib.RongIMClient;
import io.rong.message.RichContentMessage;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String ACTION_DMEO_RECEIVE_MESSAGE = "action_demo_receive_message";
    private static final String TAG = "FunctionListActivity";
    private ListView mListView;
    private FunctionListAdapter mFunctionListAdapter;
    private Button mLogout;
    private ActionBar mAction;
    private int numbermessage = 0;
    private ImageView mImageView;
    private String TOKEN="6TFD0OUBUvI2LCD68J77icvm/o4XK5QTKgksvmQJQNaM+V+bYwBTgldvROK68J1P8z9Kj2yVRGSIc+V4IbZWKA==";
    private ReceiveMessageBroadcastReciver mBroadcastReciver;
    @Override
    protected int setContentViewResId() {
        return R.layout.activity_functioan_list;
    }

    @Override
    protected void initView() {

        mListView = getViewById(android.R.id.list);
        View headerView = LayoutInflater.from(this).inflate(
                R.layout.view_list_header, null);
        mListView.addHeaderView(headerView);
        mLogout = getViewById(android.R.id.button1);
        mLogout.setOnClickListener(this);
        mAction = getViewById(R.id.action_bar);
        mImageView = mAction.getBackView();
        mImageView.setVisibility(View.GONE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DMEO_RECEIVE_MESSAGE);
        if (mBroadcastReciver == null) {
            mBroadcastReciver = new ReceiveMessageBroadcastReciver();
        }
        this.registerReceiver(mBroadcastReciver, intentFilter);

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private class ReceiveMessageBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_DMEO_RECEIVE_MESSAGE)) {
                numbermessage = intent.getIntExtra("rongCloud", -1);
                initData();
            }
        }

    }

    @Override
    protected void initData() {
        if (RongIM.getInstance() != null) {
            try {
                numbermessage = RongIM.getInstance().getTotalUnreadCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] titleNameArray = this.getResources().getStringArray(
                R.array.function_list);
        mFunctionListAdapter = new FunctionListAdapter(this, titleNameArray,
                numbermessage);
        mListView.setAdapter(mFunctionListAdapter);
        mFunctionListAdapter.notifyDataSetChanged();

        mListView.setOnItemClickListener(this);
        mAction.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position == 1) {

            /**
             * 打开会话列表
             *
             * 如果您需要会话列表界面，可以启动聊天会话列表，在聊天会话列表中，用户可以通过点击右上角的加号按钮创建聊天会话
             *
             * API详见 http://docs.rongcloud.cn/android.html
             */
            if (RongIM.getInstance() != null)
                RongIM.getInstance().startConversationList(this);


        } else if (position == 2) {
            if (RongIM.getInstance() != null)
                RongIM.getInstance().startCustomerServiceChat(this, "kefu114", "客服");


        } else if (position == 3) {
            if(RongIM.getInstance() != null) {

                String content = "新华网莫斯科10月14日电 国务院总理李克强14日出席第三届莫斯科国际创新发展论坛并发表题为《以创新实现共同发展包容发展》的演讲。演讲全文如下：创新是人类发展进步的不熄引擎。当今世界正处于大变革、大调整之中，迫切要求更大范围、更深层次的创新。实现这样的创新，墨守成规不行，单打独斗也不行，需要开放、合作与分享。6年前，面对国际金融危机，国际社会同舟共济，避免了危机向纵深蔓延。随着经济全球化、社会信息化的深入推进，更需要各国携起手来，在合作创新中实现知识的倍增、价值的倍增，解决发展的难题，促进共同繁荣。这正是开放式创新的意义所在。";
                String title = "新华网莫斯科10月14日电,李克强在第三届莫斯科国际创新发展论坛上的演讲";
                String url = "http://img2.cache.netease.com/photo/0003/2014-10-15/900x600_A8J6CVA400AJ0003.jpg";
                RichContentMessage imageTextMessage = new RichContentMessage(title, content, url);
                imageTextMessage.setExtra("可以存放的网址，商品编号或URI,在点击消息时你可以取到进入你的商品页面");


                RongIM.getInstance().sendMessage(RongIMClient.ConversationType.PRIVATE, "11", imageTextMessage, new RongIMClient.SendMessageCallback() {

                            @Override
                            public void onSuccess(int messageId) {

                            }

                            @Override
                            public void onError(int messageId, ErrorCode errorCode) {

                            }

                            @Override
                            public void onProgress(int messageId, int percent) {

                            }
                        }
                );

                RongIM.getInstance().sendMessage(RongIMClient.ConversationType.CUSTOMER_SERVICE, "kefu114", imageTextMessage, new RongIMClient.SendMessageCallback() {

                            @Override
                            public void onSuccess(int messageId) {

                            }

                            @Override
                            public void onError(int messageId, ErrorCode errorCode) {

                            }

                            @Override
                            public void onProgress(int messageId, int percent) {

                            }
                        }
                );
                RongIM.getInstance().startCustomerServiceChat(this, "kefu114", "客服");
            }


        } else {
            if (position == 4) {
                /**
                 * 打开二人会话页面
                 *
                 * API详见 http://docs.rongcloud.cn/android.html
                 */
                if (RongIM.getInstance() != null  && DemoContext.getInstance().getCurrentUser() != null && !"".equals(DemoContext.getInstance().getCurrentUser()))
                    RongIM.getInstance().startPrivateChat(this, DemoContext.getInstance().getCurrentUser().getUserId(), "光头强");
                else{
                    RongIM.getInstance().startPrivateChat(this, "11", "光头强");
                }

            } else if (position == 5) {
                startActivity(new Intent(this, GroupListActivity.class));
            } else if (position == 6) {
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startConversation(this, RongIMClient.ConversationType.CHATROOM, "chatroom002", "聊天室");

            } else if (position == 7) {
                startActivity(new Intent(this, TestFragmentActivity.class));
            } else if (position == 8) {
                startActivity(new Intent(this, TestFragment2Activity.class));
            } else if (position == 9) {
                final  GroupInvitationNotification mGroupInvitationNotification = new GroupInvitationNotification("6878","I'm Bob");
                if(RongIM.getInstance() != null){
                    RongIM.getInstance().sendMessage(RongIMClient.ConversationType.PRIVATE,"6878",mGroupInvitationNotification, new RongIMClient.SendMessageCallback()
                            {
                                @Override
                                public void onSuccess(int i) {
                                    Log.e(TAG,"---自定义消息 onSuccess---");
                                    GroupInvitationNotification groupInvitationNotification =  mGroupInvitationNotification;
                                    Log.e("sendMessage", "VoiceMessage--发发发发发--发送了一条【群组邀请消息】---message--" + groupInvitationNotification.getMessage());
                                }

                                @Override
                                public void onError(int i, ErrorCode errorCode) {

                                }

                                @Override
                                public void onProgress(int i, int i2) {

                                }
                            }
                    );
                }


            }else if (position == 10){
                startActivity(new Intent(this, BlackListActivity.class));
            }else if(position == 11){
                startActivity(new Intent(this,DemoNotificationCycleSettingActivity.class));

            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v == mLogout) {

            /**
             * 断开与服务器的连接
             *
             * API详见 http://docs.rongcloud.cn/android.html
             */

            if (RongIM.getInstance() != null)
                RongIM.getInstance().disconnect(false);

            SharedPreferences.Editor editor = DemoContext.getInstance().getSharedPreferences().edit();
            editor.remove("LOGIN_TOKEN");
            editor.commit();


            finish();
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {

            final AlterDialog alterDialog = new AlterDialog(this);
            alterDialog.setTitle("确定退出应用？", true);

            alterDialog.setButton1("确定", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (RongIM.getInstance() != null) RongIM.getInstance().disconnect(true);
                    Process.killProcess(Process.myPid());
                }
            });

            alterDialog.setButton2("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alterDialog.dismiss();
                }
            });

            alterDialog.show();
        }

        return false;
    }


    @Override
    protected void onDestroy() {
        if (mBroadcastReciver != null) {
            this.unregisterReceiver(mBroadcastReciver);
        }
        super.onDestroy();
    }

}