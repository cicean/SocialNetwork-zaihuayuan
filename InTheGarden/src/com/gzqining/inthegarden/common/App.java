package com.gzqining.inthegarden.common;
import android.app.Application;
import io.rong.imkit.RongIM;
import io.rong.imkit.demo.DefaultExceptionHandler;
import io.rong.imkit.demo.DemoContext;
import io.rong.imkit.demo.RongCloudEvent;
import io.rong.imkit.demo.message.GroupInvitationNotification;
import io.rong.imlib.AnnotationNotFoundException;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化。
        RongIM.init(this);

        /**
		 * 融云SDK事件监听处理
		 */
		RongCloudEvent.init(this);

		DemoContext.init(this);
		try {
			// 注册自定义消息类型
			RongIM.registerMessageType(GroupInvitationNotification.class);

		} catch (AnnotationNotFoundException e) {
			e.printStackTrace();
		}

		Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(
				this));
    }
}
