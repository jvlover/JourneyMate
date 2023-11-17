package com.journeymate.userservice.util;

import java.nio.ByteBuffer;

public class BytesHexChanger {

    private final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    // String 을 byte[]로
    public byte[] hexToBytes(String id) {

        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(Long.parseUnsignedLong(id.substring(0, 16), 16));
        bb.putLong(Long.parseUnsignedLong(id.substring(16), 16));
        return bb.array();
    }

    //byte[]를 String으로
    public String bytesToHex(byte[] hexId) {

        char[] hexChars = new char[hexId.length * 2];
        for (int i = 0; i < hexId.length; i++) {
            int v = hexId[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }
}
