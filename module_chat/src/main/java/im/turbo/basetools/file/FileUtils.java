package im.turbo.basetools.file;

import java.util.Locale;

/**
 * created by zhaoyuntao
 * on 09/06/2022
 * description:
 */
public class FileUtils {

    private static final double SIZE_KB = 1000;
    private static final double SIZE_MB = SIZE_KB * 1000;
    private static final double SIZE_G = 1000 * SIZE_MB;
    private static final double SIZE_10M = 10 * SIZE_MB;
    private static final double SIZE_5M = 5 * SIZE_MB;

    public static String sizeString(long byteSize) {
        if (byteSize < SIZE_KB) { // B
            return byteSize + " B";
        }
        if (byteSize < SIZE_MB) { // KB
            return (int) (byteSize / SIZE_KB) + " K";
        }
        if (byteSize < SIZE_G) { // MB
            double f = (byteSize / SIZE_MB);
            return String.format(Locale.ENGLISH, "%.1f MB", f);
        }
        double f = byteSize / SIZE_G;
        return String.format(Locale.ENGLISH, "%.01f GB", f);
    }
}
