package com.gzqining.inthegarden.util;

public class Constans {

	/**
	 * 服务器IP
	 * **/
	public static final String HOST = "http://61.152.93.162:8380/";

	/**
	 * 登录接口
	 * **/
	public static final String authorize = HOST + "index.php?app=api&mod=Oauth&act=authorize";
	/**
	 * 郵箱注册接口
	 * **/
	public static final String register = HOST + "index.php?app=api&mod=Oauth&act=regist";
	/**
	 * 世界接口
	 * **/
	public static final String world = HOST + "index.php?app=api&mod=Users&act=news_list";
	/**
	 * 粉丝接口
	 * **/
	public static final String fans = HOST + "index.php?app=api&mod=Users&act=user_followers";
	/**
	 * 关注接口
	 * **/
	public static final String attention = HOST + "index.php?app=api&mod=Users&act=user_following";
	/**
	 * 相册接口
	 * **/
	public static final String album = HOST + "index.php?app=api&mod=Users&act=profileContent";
	/**
	 * 地区数据接口
	 * **/
	public static final String area_list = HOST + "index.php?app=api&mod=Users&act=area_list";
	/**
	 * 用户资料接口
	 * **/
	public static final String userinfo = HOST + "index.php?app=api&mod=Users&act=userinfo";
	/**
	 * 上传相片接口
	 * **/
	public static final String upload_picture = HOST + "index.php?app=api&mod=Users&act=upload_picture";
	/**
	 * 关注，取消关注接口
	 * **/
	public static final String unAndoFollow = HOST + "index.php?app=api&mod=Users&act=unAndoFollow";
	/**
	 * 修改个人资料接口
	 * **/
	public static final String editUserData = HOST + "index.php?app=api&mod=Users&act=editUserData";
	/**
	 * 寻觅附近的人接口
	 * **/
	public static final String looking_user = HOST + "index.php?app=api&mod=Users&act=looking_user";
	/**
	 * 手機註冊接口
	 * **/
	public static final String registPhone = HOST + "index.php?app=api&mod=Oauth&act=registPhone";
	/**
	 * 手机驗證碼接口
	 * **/
	public static final String get_PhoneCode = HOST + "index.php?app=api&mod=Oauth&act=get_PhoneCode";
	/**
	 * 圖片驗證碼接口
	 * **/
	public static final String get_PictureCode = HOST + "/index.php?app=api&mod=Oauth&act=get_captcha";
	/**
	 * 邮箱找回密码接口
	 * **/
	public static final String Email_PwRetrieve = HOST + "index.php?app=api&mod=Oauth&act=Email_PwRetrieve";

	/**
	 * 手机找回密码接口
	 * **/
	public static final String Phone_PwRetrieve = HOST + "index.php?app=api&mod=Oauth&act=Phone_PwRetrieve";

	/**
	 * 获取融云token
	 */
	public static final String getToken = HOST + "index.php?app=api&mod=Chat&act=getToken";
	/**
	 * 意见反馈
	 */
	public static final String submitFeedback = HOST + "index.php?app=api&mod=Users&act=submitFeedback";

	/**
	 * 礼物列表
	 */
	public static final String getGift = HOST + "index.php?app=api&mod=Users&act=getGift";
	/**
	 * 送礼物
	 */
	public static final String sendGift = HOST + "index.php?app=api&mod=Users&act=sendGift";
	/**
	 * 用户送出礼物列表
	 */
	public static final String toUserGift = HOST + "index.php?app=api&mod=Users&act=toUserGift";
	/**
	 * 查看送礼/买断详情
	 */
	public static final String giftDetails = HOST + "index.php?app=api&mod=Users&act=giftDetails";
	/**
	 * 收支明细
	 */
	public static final String creditDetail = HOST + "index.php?app=api&mod=Users&act=creditDetail";
	/**
	 * 设置默认提现方式
	 */
	public static final String setBinkDefault = HOST + "index.php?app=api&mod=Users&act=setBinkDefault";
	/**
	 * 提现方式列表
	 */

	public static final String binkList = HOST + "index.php?app=api&mod=Users&act=binkList";
	/**
	 * 编辑提现方式
	 */
	public static final String editBinkInfo = HOST + "index.php?app=api&mod=Users&act=editBinkInfo";
	/**
	 * 生成订单
	 */
	public static final String createOrder = HOST + "index.php?app=api&mod=Users&act=createOrder";
	/**
	 * 修改订单
	 */
	public static final String payOrder = HOST + "index.php?app=api&mod=Users&act=payOrder";
	/**
	 * 我的财富
	 */
	public static final String myScore = HOST + "index.php?app=api&mod=Users&act=myScore";
	/**
	 * 买断列表
	 */
	public static final String buyoutList = HOST + "index.php?app=api&mod=Users&act=buyoutList";
	/**
	 * 礼物推送消息
	 */
	public static final String giftMessage = HOST + "index.php?app=api&mod=Chat&act=giftMessage";

	/**
	 * 提现
	 */
	public static final String getCash = HOST + "index.php?app=api&mod=Users&act=getCash";
	/**
	 * 身份认证
	 */
	public static final String approve = HOST + "index.php?app=api&mod=Users&act=approve";
	/**
	 * 认证状态
	 */
	public static final String myState = HOST + "index.php?app=api&mod=Users&act=myState";
	/**
	 * 图片删除
	 */
	public static final String delete_picture = HOST + "index.php?app=api&mod=Users&act=delete_picture";
	/**
	 * 申请礼物撤回
	 */
	public static final String recallGift = HOST + "index.php?app=api&mod=Users&act=recallGift";
	/**
	 * 买断状态确认
	 */
	public static final String checkGift = HOST + "index.php?app=api&mod=Users&act=checkGift";
	/**
	 * 买断状态确认
	 */
	public static final String chenkBuyout = HOST + "index.php?app=api&mod=Users&act=chenkBuyout";
	/**
	 * 电话/邮箱验证
	 */
	public static final String dataVerify = HOST + "index.php?app=api&mod=Oauth&act=dataVerify";

	// 礼物推送消息
	public static final int REQ_giftMessage = 30;
	// 买断列表
	public static final int REQ_buyoutList = 29;
	// 我的财富
	public static final int REQ_myScore = 28;
	// 生成订单
	public static final int REQ_createOrder = 27;
	// 编辑提现方式
	public static final int REQ_editBinkInfo = 26;
	// 提现方式列表
	public static final int REQ_binkList = 25;
	// 设置默认提现方式
	public static final int REQ_setBinkDefault = 24;
	// 收支明细
	public static final int REQ_creditDetail = 23;
	// 查看送礼/买断详情
	public static final int REQ_giftDetails = 22;
	// 已送出礼物列表
	public static final int REQ_toUserGift = 21;
	// 送礼物
	public static final int REQ_sendGift = 20;
	// 获取礼物列表
	public static final int REQ_getGift = 19;
	// 获取融云token标识
	public static final int REQ_submitFeedback = 18;
	// 获取融云token标识
	public static final int REQ_getToken = 17;
	// 手机找回密码
	public static final int REQ_Phone_PwRetrieve = 16;
	// 邮箱找回密码
	public static final int REQ_Email_PwRetrieve = 15;
	// 驗證碼
	public static final int REQ_Get_PhoneCode = 14;
	// 手機註冊
	public static final int REQ_RegistPhone = 13;
	// 登录
	public static final int REQ_Authorize = 1;
	// 注册
	public static final int REQ_Register = 2;
	// 世界
	public static final int REQ_World = 3;
	// 粉丝
	public static final int REQ_Fans = 4;
	// 关注
	public static final int REQ_Attention = 5;
	// 相册
	public static final int REQ_Album = 6;
	// 地区数据
	public static final int REQ_Area_List = 7;
	// 用户信息
	public static final int REQ_UserInfo = 8;
	// 上传相片
	public static final int REQ_Upload_Picture = 9;
	// 关注，取消关注
	public static final int REQ_unAndoFollow = 10;
	// 修改个人资料
	public static final int REQ_editUserData = 11;
	// 寻觅附近的人
	public static final int REQ_Looking_User = 12;

}
