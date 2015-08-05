package com.gzqining.inthegarden.app.util;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class StringUtils {
	
	
	private static SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm", Locale.getDefault());
	private static SimpleDateFormat MM_dd_HHmm = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
	private static SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private static SimpleDateFormat yyyy_MM_dd_cn = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
	private static SimpleDateFormat yyyy_MM_dd_HH_mm = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
	private static SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	
	public static String date2HHmm(Date date) {
		return HHmm.format(date);
	}
	
	public static String date2yyyy_MM_dd(Date date) {
		return yyyy_MM_dd.format(date);
	}
	
	public static String date2yyyy_MM_dd_cn(Date date) {
		return yyyy_MM_dd_cn.format(date);
	}
	
	public static String date2yyyy_MM_dd_HH_mm(Date date) {
		return yyyy_MM_dd_HH_mm.format(date);
	}
	
	public static String date2yyyy_MM_dd_HH_mm_ss(Date date) {
		return yyyy_MM_dd_HH_mm_ss.format(date);
	}

	public static String date2String(Date date) {
		if (date == null)
			return "";
		long nowTime = System.currentTimeMillis();
		long newstime_i = nowTime - date.getTime();
		String newstime_s = "";

		if (newstime_i < 60000L) {
			newstime_s = "刚刚";
		} else if (newstime_i >= 60000L && newstime_i < 3600000L) {
			newstime_s = (newstime_i / 60000L) + "分钟前";
		} else if (newstime_i >= 3600000L && newstime_i < 86400000L) {
			newstime_s = (newstime_i / 3600000L) + "小时前";
		} else if (newstime_i >= 86400000L && newstime_i < 2592000000L) {
			newstime_s = (newstime_i / 86400000L) + "天前";
		} else if (newstime_i >= 2592000000L && newstime_i < 31104000000L) {
			newstime_s = (newstime_i / 2592000000L) + "月前";
		} else {
			newstime_s = (newstime_i / 31104000000L) + "年前";
		}
		return newstime_s;
	}
	
	public static String date2DayTime(Date oldTime) {
		Date newTime = new Date();
		try {
			Calendar cal=Calendar.getInstance();
			cal.setTime(newTime);
			Calendar oldCal=Calendar.getInstance();
			oldCal.setTime(oldTime);
			
			int oldYear = oldCal.get(Calendar.YEAR);
			int year = cal.get(Calendar.YEAR);
			int oldDay = oldCal.get(Calendar.DAY_OF_YEAR);
			int day = cal.get(Calendar.DAY_OF_YEAR);
			
			if(oldYear == year){
				int value = oldDay - day;
				if(value == -1) {
					return "昨天 "+HHmm.format(oldTime);
				} else if(value == 0) {
					return "今天 "+HHmm.format(oldTime);
				} else if(value == 1) {
					return "明天 "+HHmm.format(oldTime);
				}else {
					return MM_dd_HHmm.format(oldTime);
				}
			}
		} catch (Exception e) {
			
		}
		return yyyy_MM_dd_HH_mm.format(oldTime);
	}
	
	
	/**
	 * 视频时间转换 
	 * @param t 毫秒
	 */
	public static String millisecondToDate(long t) {
		long i = t;
		i /= 1000;
		long minute = i / 60;
		long hour = minute / 60;
		long second = i % 60;
		minute %= 60;
		if (hour <= 0)
			return String.format(Locale.getDefault(), "%02d:%02d", minute, second);
		else
			return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
	}

	
	/**
	 * 格式化成时间格式比如120s --> 02:00
	 * @param milliseconds
	 * @return
	 */
	public static String generateTime(long milliseconds){
		StringBuilder sb = new StringBuilder();
		long h,m,s;
		h = milliseconds/(3600000);
		if(h>0){
			sb.append(h+":");
		}
		m = (milliseconds-h*3600000)/60000;
		if(m<10){
			sb.append("0"+m+":");
		}else{
			sb.append(m+":");
		}
		s = milliseconds % 60000/1000;
		if(s<10){
			sb.append("0"+s);
		}else{
			sb.append(""+s);
		}
		return sb.toString();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public static boolean isEmail(String email) {
		if ((email == null) || (email.trim().equals(""))) {
			return false;
		}
		String emailRegular = "^([a-z0-9A-Z]+[-|\\.]?+[_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(emailRegular);
		Matcher matcher = regex.matcher(email);
		boolean isMatched = matcher.matches();
		return isMatched;
	}
	
	/** 判断电话号码 **/
	public static boolean isPhoneNumberValid(String phoneNumber) {
		if ((phoneNumber == null) || (phoneNumber.trim().equals(""))) {
			return false;
		}
		/*boolean isValid = false;
		String expression_r_r = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
		Pattern pattern = Pattern.compile(expression_r_r);
		Matcher matcher = pattern.matcher(phoneNumber);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;*/
		
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,3-9]))\\d{8}$");  
		Matcher m = p.matcher(phoneNumber);  
		return m.matches();
	}
	
	public static String addParamURL(String url, String params) {
		if(!TextUtils.isEmpty(url) && !TextUtils.isEmpty(params)) {
			try {
				URI uri = URI.create(url.trim());
				if(uri.getFragment() != null) {
					String fragment = params;
					if(uri.getFragment().length() > 0)
						fragment += '&'+uri.getFragment();
					URI newURI = new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), null, fragment);
					return newURI.toString();
				} else {
					String query = params;
					if(!TextUtils.isEmpty(uri.getQuery()))
						query +=  '&'+uri.getQuery();
					URI newURI = new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), query, null);
					return newURI.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return url;
	}
	
	/**
	 * 判断IP是否在指定范围；
	 */
	public static boolean ipIsValid(String ipSection, String ip) {
		ipSection = ipSection.trim();
		ip = ip.trim();
		final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
		final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
		if (!ipSection.matches(REGX_IPB) || !ip.matches(REGX_IP))
			return false;
		int idx = ipSection.indexOf('-');
		String[] sips = ipSection.substring(0, idx).split("\\.");
		String[] sipe = ipSection.substring(idx + 1).split("\\.");
		String[] sipt = ip.split("\\.");
		long ips = 0L, ipe = 0L, ipt = 0L;
		for (int i = 0; i < 4; ++i) {
			ips = ips << 8 | Integer.parseInt(sips[i]);
			ipe = ipe << 8 | Integer.parseInt(sipe[i]);
			ipt = ipt << 8 | Integer.parseInt(sipt[i]);
		}
		if (ips > ipe) {
			long t = ips;
			ips = ipe;
			ipe = t;
		}
		return ips <= ipt && ipt <= ipe;
	}

	/**
	 * 半角转全角
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
	
	/**
	 * 字符串转string
	 * @param arrayOfString
	 * @return
	 */
	public static List<String> stringsToList(String[] arrayOfString) {
		if ((arrayOfString == null) || (arrayOfString.length == 0))
			return null;
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < arrayOfString.length; i++)
			list.add(arrayOfString[i]);
		return list;
	}
	
	/**
	 * 获取字符串长度（一个中文=2）
	 * @param string
	 * @return
	 */
	public static int textLength(String string){
		if(TextUtils.isEmpty(string))
			return 0;
		String anotherString = null;
		try {
			anotherString = new String(string.getBytes("GBK"), "ISO8859_1");
		} catch (Exception ex) {}
		
		return !TextUtils.isEmpty(anotherString) ? anotherString.length() : string.length();
	}
	
	public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	
	public static boolean isNum0_9(String str){
		return str.matches("[0-9]*");
	}
	
	public static boolean isSystemCalss(String name) {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.");
    }
}
