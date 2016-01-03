package com.tintin.tintinplayer.util;

import android.util.Base64;

/**
 * Created by alive on 2016/1/3.
 */
public class Base64Util
{

    public static String encodeBase64(String str)
    {
        // Base64 编码：
        byte[] encode = Base64.encode(str.getBytes(), Base64.DEFAULT);

        String enc = new String(encode);

        return enc;

    }

    public static String decodeBase64(String str)
    {
        // Base64 解码：
        byte[] result = Base64.decode("SGVsbG8sIFdvcmxk", Base64.DEFAULT);

        String res = new String(result);

        return res;
    }
}
