package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class BasicUtils {
    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     *
     * @param date 要格式化的日期
     * @param timeOnly 是则只保留时间部分舍弃日期部分，否则为日期+时间
     * @return
     */
    public static String formatDate(Date date,boolean timeOnly){
        if(timeOnly) {
            return timeFormat.format(date);
        } else {
            return dateTimeFormat.format(date);
        }
    }

    public static String formatDate(Date date){
        return formatDate(date,true);
    }

    public static String convertByte(long byteData){
        String result = byteData + "B";
        double temp;
        if(byteData >= 1024){
            temp = byteData / 1024;
            result = String.format("%.1f", temp) + "KB";
            if(temp >=1024){
                temp = temp / 1024;
                result = String.format("%.1f", temp) + "MB";
                if(temp >=1024){
                    temp = temp/1024;
                    result = String.format("%.2f", temp)+"GB";
                }
            }
        }
        return result;
    }

    public static String convertTime(long second){
        String result = second + "秒";
        long m;
        long h;
        long s;
        if(second >= 60){
            m = second / 60;
            s = second % 60;
            result = m+"分"+s+"秒";
            if(m >= 60){
                h = m / 60;
                m = m % 60;
                result = h+"小时"+m+"分"+s+"秒";
            }
        }
        return result;
    }

    /**
     * 将长整型数转换为byte数组（以便在网络上传输）
     * @param lNum 长整型数（64位）
     * @return 转换后的字节数组 （长度为8）
     * @throws DecoderException
     */
    public static byte[] longToByteArray(Long lNum) throws DecoderException{
        String hexStr = Long.toHexString(lNum);
        StringBuilder sb = new StringBuilder(hexStr);
        int len = sb.length();
        for(int i=0;i<(16-len);i++){
            sb.insert(0, '0');
        }
        hexStr = sb.toString();
        return Hex.decodeHex(hexStr.toCharArray());

    }

    /**
     * 将byte数组（如通过网络传输过来的数据）转换为长整型数
     * @param byteArray 字节数组（长度为8）
     * @return 转换后的长整型数（64位）
     * @throws Exception
     */
    public static Long byteArrayToLong(byte[] byteArray) throws Exception{
        if(byteArray.length != 8) {
            throw new Exception("字节数组不正确，长度必须为8");
        }
        char[] charArray = Hex.encodeHex(byteArray);
        String strValue = String.valueOf(charArray);
        return Long.decode("#"+strValue);
    }
}
