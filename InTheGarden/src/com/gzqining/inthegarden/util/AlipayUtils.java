package com.gzqining.inthegarden.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.gzqining.inthegarden.act.ChatConversationActivity;
import com.gzqining.inthegarden.vo.CommonBack;
import com.gzqining.inthegarden.vo.CommonConstants;
import com.gzqining.inthegarden.vo.giftMessageBean;

public class AlipayUtils {
	Activity activity;
	Object obj;
	Handler handler;
	String encodesstr = "";
	CommonBack back;
	String giftid;
	String orderNo;
	int type;

	public AlipayUtils(Context context, int type) {
		super();
		this.activity = (Activity) context;
		this.type = type;
	}

	public AlipayUtils(Activity activity, int type, String oauth_token, String oauth_token_secret) {
		super();
		this.activity = activity;
		this.type = type;
	}

	// 商户PID
	public static final String PARTNER = "2088911913866787";
	// 商户收款账号
	public static final String SELLER = "2088911913866787";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOMgTprnZHFoCUV/EClICBenLIEncVv2oqkWfw5QNQ85cXDfqyoDF0wgZZ0VVsZbfO40ODs9Kv0sSudLQNo/EQHv6b/JqXE3Wi+7Wk8il9NhNfoipplyEWvHO0eQ6e0W549Ayim44qR2j6tWgId/h8eNq8SqF36fEUCphp8kThKTAgMBAAECgYBPivj8dx1OOklE9YMLchajcgC8GrwwoOfGoAJPWPe8JTn9ddfUFtEJ8C6mHNsPDOtr4Q3wcTOlBuYUqt5DkOK7SfbHzFTd8t5rXGQhnTvQ1nzcS+SKx97Lkn3f4Su+a4v74oEJ/42aScNUxlmI9zRwvjl/C6p9yB2jLT0by0W5QQJBAPjoTMrREHbVXF9gdYILAEHR6k4is/3Ms3vuucfWaFbhfUpEZuVkZ1LKTqZmQfnu1FC4ahLFRHnCdU1kPlD4PN0CQQDpmR66WqC7j//DqqLqA8oVsMSwuYnEHIc0FUtR6ggDz7xRv+N6RJNukm2fb35aWZj/RJVV/OhLA4l57w9D7R4vAkANfYmr9GjQCABZqwCza6U37Aim83qN5upTbTzkd9pv2wjPXaW2CRsQgaaBnkk1IpyfQ198ZPSxJLj22NgHy2yBAkA7bcIchOv5a8QU4nd586bY59TSZRGAfAWFoZk86L5LQfFWFuxnccTUT5pmAnZxw/OhlbAsZSAv3WF/TOtkS5HvAkEAsRXgPMTLGnnqe9HW5yDC3+Su2zJwVr6W5mUW+lJ3OFgyfcHT+JQRVXVqa6bk4oDQ0qgkZWVmee7b1JyTQz6QNA==";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDjIE6a52RxaAlFfxApSAgXpyyBJ3Fb9qKpFn8OUDUPOXFw36sqAxdMIGWdFVbGW3zuNDg7PSr9LErnS0DaPxEB7+m/yalxN1ovu1pPIpfTYTX6IqaZchFrxztHkOntFuePQMopuOKkdo+rVoCHf4fHjavEqhd+nxFAqYafJE4SkwIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	public void playOrderToService() {
		String url = Constans.payOrder;
		AjaxParams params = new AjaxParams();
		params.put("oauth_token", Util.getOauthToken(activity));
		params.put("oauth_token_secret", Util.getOauthTokenSecret(activity));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderNo);
		Log.e("anan", new Gson().toJson(map));
		try {
			encodesstr = AesUtils.Encrypt(new Gson().toJson(map), CommonConstants.AES_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		params.put("cont", encodesstr);
		FinalHttp http = new FinalHttp();
		http.post(url, params, new AjaxCallBack<Object>() {

			@Override
			public void onSuccess(Object t) {
				Log.e("anan", t.toString());
				if (type == 1) {
					if (t.toString().indexOf("0000") != -1) {
						SharedPreferences preferences = activity.getSharedPreferences("idcard", Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = preferences.edit();
						editor.putInt("is_vip", 1);
						editor.commit();
					}
				}
				super.onSuccess(t);
			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
					playOrderToService();
					if (type == 0) {
						giftMessaget();
						// 付款完之后跳转到聊天界面
						Intent intent = new Intent(activity, ChatConversationActivity.class);
						activity.startActivity(intent);
						activity.finish();
					} else {
						activity.finish();
					}
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(activity, "支付结果确认中", Toast.LENGTH_SHORT).show();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(activity, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	// fragment或dialog中调用
	// 调用方法
	// AlipayUtils ga = new AlipayUtils(context);
	// ga.pay(context,0.01);
	public void pay(final Context context, String orderNo, double price) {
		this.orderNo = orderNo;
		// 订单
		String orderInfo = getOrderInfo("礼物", "在花园礼物购买", price + "");

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				activity = (Activity) context;
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	// activity中调用
	// 调用方法
	// AlipayUtils ga = new AlipayUtils(giftListActivity.this);
	// ga.pay(giftListActivity.this, 0.01);
	public void pay(final Activity activity, String orderNo, int price) {
		this.orderNo = orderNo;
		// 订单
		String orderInfo = getOrderInfo("礼物", "在花园礼物购买", price + "");

		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(activity);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(activity);
		String version = payTask.getVersion();
		Toast.makeText(activity, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		if (orderNo == null) {
			SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
			Date date = new Date();
			String key = format.format(date);

			Random r = new Random();
			key = key + r.nextInt();
			key = key.substring(0, 15);
			return key;
		}
		return orderNo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	// 推消息接口
	@SuppressLint("ShowToast")
	public void giftMessaget() {

		giftMessageBean bean = new giftMessageBean();
		bean.setGiftUserId(giftid);
		bean.setPushData("0");
		String jsonstr = JsonHelper.toJsonString(bean);

		try {
			encodesstr = AesUtils.Encrypt(jsonstr, CommonConstants.AES_KEY);
			Logger.d("encodesstr", encodesstr);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = Constans.giftMessage;
				AjaxParams params = new AjaxParams();
				params.put("oauth_token", Util.getOauthToken(activity));
				params.put("oauth_token_secret", Util.getOauthTokenSecret(activity));
				params.put("cont", encodesstr);
				FinalHttp http = new FinalHttp();
				obj = http.postSync(url, params);
				handler.sendEmptyMessage(4);

			}
		}).start();

		handler = new Handler() {
			@SuppressLint("ShowToast")
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 4) {
					try {
						CommonBack back = (CommonBack) ReqJsonUtil.changeToObject(obj.toString(), CommonBack.class);
						System.out.print(back);
						if (back.getCode() == 00000) {
							//
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
}
