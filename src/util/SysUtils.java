package util;

import java.util.Map;

/**
 * @author 22591
 */
public class SysUtils {
    /**
     * 获取本机上当前的登录用户
     * @return
     */
    public static String getLoginUserName(){
        Map<String, String> map = System.getenv();
        if(map.containsKey("USERNAME")) {
            return  map.get("USERNAME");
        } else {
            return "";
        }
    }
    /**
     * 获取本机机器名
     * @return
     */
    public static String getComputerName(){
        Map<String, String> map = System.getenv();
        if(map.containsKey("COMPUTERNAME")) {
            return  map.get("COMPUTERNAME");
        } else {
            return "";
        }
    }
}
