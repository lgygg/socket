package com.lgy.socket.util;

public class ByteUtil {
    public static byte[] byteMergerAll(byte[]... args) {
        int length_byte = 0;
        for (byte[] b : args) {
            length_byte += b.length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (byte[] b : args) {
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }
}
