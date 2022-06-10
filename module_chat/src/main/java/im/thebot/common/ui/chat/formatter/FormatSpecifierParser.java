package im.thebot.common.ui.chat.formatter;

import java.util.IllegalFormatPrecisionException;
import java.util.UnknownFormatConversionException;

/**
 * created by zhaoyuntao
 * on 14/09/2021
 * description:
 */
 class FormatSpecifierParser {
    private final String format;
    private int cursor;

    private String index;
    private String flags;
    private String width;
    private String precision;
    private String tT;
    private String conv;

    private static final String FLAGS = ",-(+# 0<";

    public FormatSpecifierParser(String format, int startIdx) {
        this.format = format;
        cursor = startIdx;
        // Index
        if (nextIsInt()) {
            String nint = nextInt();
            if (peek() == '$') {
                index = nint;
                advance();
            } else if (nint.charAt(0) == '0') {
                // This is a flag, skip to parsing flags.
                back(nint.length());
            } else {
                // This is the width, skip to parsing precision.
                width = nint;
            }
        }
        // Flags
        flags = "";
        while (width == null && FLAGS.indexOf(peek()) >= 0) {
            flags += advance();
        }
        // Width
        if (width == null && nextIsInt()) {
            width = nextInt();
        }
        // Precision
        if (peek() == '.') {
            advance();
            if (!nextIsInt()) {
                throw new IllegalFormatPrecisionException(peek());
            }
            precision = nextInt();
        }
        // tT
        if (peek() == 't' || peek() == 'T') {
            tT = String.valueOf(advance());
        }
        // Conversion
        conv = String.valueOf(advance());
    }

    private String nextInt() {
        int strBegin = cursor;
        while (nextIsInt()) {
            advance();
        }
        return format.substring(strBegin, cursor);
    }

    private boolean nextIsInt() {
        return !isEnd() && Character.isDigit(peek());
    }

    private char peek() {
        if (isEnd()) {
            throw new UnknownFormatConversionException("End of String");
        }
        return format.charAt(cursor);
    }

    private char advance() {
        if (isEnd()) {
            throw new UnknownFormatConversionException("End of String");
        }
        return format.charAt(cursor++);
    }

    private void back(int len) {
        cursor -= len;
    }

    private boolean isEnd() {
        return cursor == format.length();
    }

    public int getEndIdx() {
        return cursor;
    }
}
