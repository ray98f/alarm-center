package com.zte.msg.alarmcenter.utils;

import java.io.UnsupportedEncodingException;

public class ConversionUtils {

    /**
     * @param inHex
     * @return byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * @param inHex
     * @return byte[]
     */
    public static byte[] hexToByteArr(String inHex) {
        inHex = inHex.replaceAll(" ", "").replaceAll("\n", "");
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            hexlen++;
            inHex = "0" + inHex;
            result = new byte[(hexlen / 2)];
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String hexStr = "ba:a3:b3:c7:d5:be".toUpperCase();
        hexStr = hexStr.replaceAll(":", " ");
        byte[] bytes = hexToByteArr(hexStr);
        String s = new String(bytes, "gbk");
        System.out.printf("16进制报文为：%s\n转为报文为：%s", hexStr, s);
    }
}
