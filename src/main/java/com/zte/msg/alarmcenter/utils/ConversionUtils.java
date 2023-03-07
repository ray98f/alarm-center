package com.zte.msg.alarmcenter.utils;

public class ConversionUtils {

    /**
     * @Author 宇智波波奶茶
     * @param inHex
     * @return byte
     */
    public static byte hexToByte(String inHex){
        return (byte) Integer.parseInt(inHex,16);
    }

    /**
     * @Author 宇智波波奶茶
     * @param inHex
     * @return byte[]
     */
    public static byte[] hexToByteArr(String inHex){
        inHex = inHex.replaceAll(" ","").replaceAll("\n","");
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1){
            hexlen ++;
            inHex = "0" + inHex;
            result = new byte[(hexlen/2)];
        } else {
            result = new byte[(hexlen/2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i+=2) {
            result[j] = hexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }
}
