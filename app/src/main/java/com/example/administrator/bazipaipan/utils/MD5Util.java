package com.example.administrator.bazipaipan.utils;

import java.security.MessageDigest;


public class MD5Util {
    // 与IOS想同,,没有测试中文
    public static String MD5(byte[] source) {
        String s = null;
        char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
        try {
            MessageDigest md = MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
            int j = tmp.length;                            // 用字节表示就是 16 个字节
            char str[] = new char[j * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
            // 所以表示成 16 进制需要 32 个字符
            int k = 0; // 表示转换结果中对应的字符位置
            for (int i = 0; i < j; i++) { // 从第一个字节开始，对 MD5 的每一个字节
                // 转换成 16 进制字符的转换
                byte byte0 = tmp[i]; // 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
                // >>>
                // 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
            }
            s = new String(str); // 换后的结果转换为字符串
            s = s.toUpperCase();// 转换成大写
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * 微信支付MD5
     *
     * @param buffer
     * @return
     */
    public final static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

//	// MD5加密，32位
//		public static String MD5(String str) {
//			MessageDigest md5 = null;
//			try {
//				md5 = MessageDigest.getInstance("MD5");
//				md5.reset();
//				md5.update(str.getBytes("UTF-8"));
//			} catch (Exception e) {
//				e.printStackTrace();
//				return "";
//			}
//			char[] charArray = str.toCharArray();
//			byte[] byteArray = new byte[charArray.length];
//
//			for (int i = 0; i < charArray.length; i++) {
//				byteArray[i] = (byte) charArray[i];
//			}
//			byte[] md5Bytes = md5.digest(byteArray);
//
//			StringBuffer hexValue = new StringBuffer();
//			for (int i = 0; i < md5Bytes.length; i++) {
//				int val = ((int) md5Bytes[i]) & 0xff;
//				if (val < 16) {
//					hexValue.append("0");
//				}
//				hexValue.append(Integer.toHexString(val));
//			}
//			return hexValue.toString();
//		}
    /*
    * MD5加密
	*/
//	public static String MD5(String str) {       
//	      MessageDigest messageDigest = null;       
//	     
//	      try {       
//	          messageDigest = MessageDigest.getInstance("MD5");       
//	     
//	          messageDigest.reset();       
//	     
//	          messageDigest.update(str.getBytes("UTF-8"));       
//	      } catch (NoSuchAlgorithmException e) {       
//	          System.out.println("NoSuchAlgorithmException caught!");       
//	          System.exit(-1);       
//	      } catch (UnsupportedEncodingException e) {       
//	          e.printStackTrace();       
//	      }       
//	     
//	      byte[] byteArray = messageDigest.digest();       
//	     
//	      StringBuffer md5StrBuff = new StringBuffer();       
//	        
//	      for (int i = 0; i < byteArray.length; i++) {                   
//	          if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)       
//	              md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));       
//	          else       
//	              md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));       
//	      }       
//	    //16位加密，从第9位到25位  
//	      return md5StrBuff.substring(8, 24).toString().toUpperCase();      
//	  }   


}
