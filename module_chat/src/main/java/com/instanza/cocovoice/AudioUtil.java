package com.instanza.cocovoice;


import im.turbo.utils.log.S;

public class AudioUtil {

    static {
        try {
            System.loadLibrary("audio_util");
        } catch (UnsatisfiedLinkError e) {
            S.e(e);
        }
    }

    public native static long init(String dstFile);

    public native static int destroy(long handle);

    public native static int processAndEncode(long handle, byte[] src, int len);
}
