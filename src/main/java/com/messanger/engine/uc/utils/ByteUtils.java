package com.messanger.engine.uc.utils;

public class ByteUtils {
    /**
     * 
     * @param in
     * @return
     */
    public static String byteToHexString(byte in) {
        byte ch = 0x00;
        String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
        StringBuffer out = new StringBuffer();
        ch = (byte) (in & 0xF0);
        ch = (byte) (ch >>> 4);
        ch = (byte) (ch & 0x0F);
        // convert the nibble to a String Character
        out.append(pseudo[(int) ch]);
        // Strip off low nibble
        ch = (byte) (in & 0x0F);
        // convert the nibble to a String Character
        out.append(pseudo[(int) ch]);
        String rslt = new String(out);
        return rslt;
    }
}
