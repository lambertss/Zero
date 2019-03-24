package com.wuyue.Util;

import com.wuyue.common.Cons;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 功能描述:加密工具类
 */
public class EncrUtil {

    /**
     * 功能描述:对字符串脱敏处理 若字符串长度不满足begin和end则不脱敏返回
     */
    public static String desenString(String str, int begin, int end) {
        if (EmptyUtil.isEmpty(str)) {
            return str;
        }
        StringBuilder strSB = new StringBuilder(str);
        int length = strSB.length();

        for (int i = 0; i < length; i++) {
            if (i >= begin && i < end) {
                strSB.replace(i, i + 1, "*");
            }
        }
        return strSB.toString();
    }

    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"};

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 返回形式只为数字
    private static String byteToNum(byte bByte) {
        int iRet = bByte;
        System.out.println("iRet1=" + iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String GetMD5Code(String str) {
        if(str!=null&&str.length()!=0){
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                // md.digest() 该函数返回值为存放哈希值结果的byte数组
                return byteToString(md.digest(str.getBytes()));
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        }
       return null;
    }


    public static String gen16BitMD5String() {
        String sourceString = UUID.randomUUID().toString().replaceAll("-", ""); // 原字符串;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(sourceString.getBytes());
            byte b[] = messageDigest.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static String SHA1EncodeSHA1(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static final String sKey = "tjhstrhdrthdgaku";
    private static final String ivParameter = "emtrhYPUIoyjdgki";


    // 加密  
    public static String AESEncrypt(String sSrc) {
        if (EmptyUtil.isEmpty(sSrc)) {
            return null;
        }
        String result = "";
        try {
            Cipher cipher;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度  
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            result = new BASE64Encoder().encode(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            result = sSrc;
        }
        // 此处使用BASE64做转码。  
        return result;

    }

    // 解密  
    public static String AESDecrypt(String sSrc) {
        if (EmptyUtil.isEmpty(sSrc)) {
            return null;
        }
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密  
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "utf-8");

        } catch (Exception ex) {
            ex.printStackTrace();
            return sSrc;
        }
    }

    /**
     * 说明： 生成token 通过用户id + ip + ua + id 组合得到
     * @param id 用户id
     * @param timestamp 时间戳
     * @return token
     */
    public static String generateComplexToken(String id, Long timestamp){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		HttpServletRequest hsRequest = sra.getRequest();
		String ip = LogUtil.getIpAddr(hsRequest);
		String ua = hsRequest.getHeader(Cons.USERAGENT);
		LogUtil.DebugLog(EncrUtil.class, ua);
		return EncrUtil.GetMD5Code(timestamp+ip+ua+id);
    }

    /**
     * 说明： 生成token 时间戳+用户id组合得到
     * @param id 用户id
     * @return token
     */
    public static String generateTimeToken(String id){
		return EncrUtil.GetMD5Code(System.currentTimeMillis() + id);
    }
}
