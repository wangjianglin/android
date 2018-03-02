package io.cess.core.gallery;

import android.os.Build;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by lin on 28/02/2017.
 */

public class StringCodingUtils {
    public static byte[] getBytes(String src, Charset charSet) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            try {
                return src.getBytes(charSet.name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return src.getBytes(charSet);
        }
    }
}
