package com.example.administrator.bazipaipan.utils.PinYin;

/**
 * Created by 王中阳 on 2015/5/30.
 */
public class Transliterator {
    private long peer;
    /**
     * Creates a new Transliterator for the given id.
     */
    public Transliterator(String id) {
        peer = create(id);
    }
    @Override
    protected synchronized void finalize() throws Throwable {
        try {
            destroy(peer);
            peer = 0;
        } finally {
            super.finalize();
        }
    }
    /**
     * Returns the ids of all known transliterators.
     */
    public static native String[] getAvailableIDs();
    /**
     * Transliterates the specified string.
     */
    public String transliterate(String s) {
        return transliterate(peer, s);
    }
    private static native long create(String id);
    private static native void destroy(long peer);
    private static native String transliterate(long peer, String s);
}
