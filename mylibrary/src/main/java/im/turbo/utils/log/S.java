package im.turbo.utils.log;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * created by zhaoyuntao
 * on 06/06/2022
 * description:
 */
public class S {

    protected static final String tagDebug = "    [Botim][abcde]  |";

    public static final int L = -1;
    public static final int I = 0;
    public static final int E = 1;
    public static final int D = 2;
    public static final int W = 3;

    private static void log(String tag, Object o, int offsetPosition, int depth, int type, Object... args) {
        String logContent;
        if (o == null) {
            logContent = "null";
        } else if (o instanceof Exception) {
            logContent = ((Exception) (o)).getMessage();
        } else {
            logContent = o.toString();
        }
        if (args != null && args.length > 0 && logContent != null) {
            logContent = String.format(logContent, args);
        }

        final int depthDefault = 3 + offsetPosition;

        final Throwable t = new Throwable();
        final StackTraceElement[] elements = t.getStackTrace();

        if (depth > 20) {
            depth = 20;
        }
        int depthNow = depthDefault + depth + 1;
        String usingSourceL = "";
        int offsetSpaceCount = 0;
        StringBuilder stackInfo = new StringBuilder();
        while (depthNow-- > depthDefault) {
            if (elements.length <= depthNow) {
                continue;
            }
            if (stackInfo.length() > 0) {
                stackInfo.append("\n");
            }
            StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
            StringBuilder taskName = new StringBuilder();
            if (traceElements.length > 6) {
                StackTraceElement traceElement = traceElements[depthNow + 1];
                taskName.append("(").append(traceElement.getFileName()).append(":").append(traceElement.getLineNumber()).append(")  ");
                taskName.append(traceElement.getMethodName());
                if (depthNow == depthDefault) {
                    usingSourceL = taskName.toString();
                }
            } else {
                String callerClassName = elements[depthNow].getClassName();
                String callerMethodName = elements[depthNow].getMethodName();
                String callerLineNumber = String.valueOf(elements[depthNow].getLineNumber());

                int pos = callerClassName.lastIndexOf('.');
                if (pos >= 0) {
                    callerClassName = callerClassName.substring(pos + 1);
                }

                taskName.append("<").append(callerClassName).append(".").append(callerMethodName).append(" ").append(callerLineNumber).append("> ");
                if (depthNow == depthDefault) {
                    usingSourceL = "(" + callerClassName + ".java:" + callerLineNumber + ")";
                }
            }
            if (depth > 0) {
                if (offsetSpaceCount > 0) {
                    taskName.insert(0, "∟");
                } else {
                    taskName.insert(0, " ");
                }
            }
            for (int i = 0; i < offsetSpaceCount; i++) {
                taskName.insert(0, "  ");
            }
            if (offsetSpaceCount > 0) {
                for (int i = 0; i < 82; i++) {
                    taskName.insert(0, " ");
                }
            }
            stackInfo.append(taskName);
            offsetSpaceCount++;
        }
        if (offsetSpaceCount == 1) {
            offsetSpaceCount = 0;
        }
        StringBuilder offset = new StringBuilder();
        for (int i = 0; i < offsetSpaceCount; i++) {
            offset.insert(0, "  ");
        }
        switch (type) {
            case E:
                Log.d(tag, stackInfo.toString());
                Log.e(tag, offset + logContent);
                break;
            case D:
                Log.d(tag, stackInfo.toString());
                Log.i(tag, offset + logContent);
                break;
            case W:
                Log.d(tag, stackInfo.toString());
                Log.d(tag, offset + logContent);
                break;
            case I:
                Log.i(tag, offset + logContent);
                break;
            case L:
                Log.i(tag, logContent + "    " + usingSourceL);
                break;
        }
    }

    //------------ Debug -----------------
    public static void s(boolean open, Object o) {
        if (!open) {
            return;
        }
        log(tagDebug, o, 0, 0, D);
    }

    public static void s(Object o) {
        log(tagDebug, o, 0, 0, D);
    }

    public static void s(String tag, Object o) {
        log(tag, o, 0, 0, D);
    }

    public static void s(Object o, Object... args) {
        log(tagDebug, o, 0, 0, D, args);
    }

    public static void s(Throwable e, Object o, Object... args) {
        log(tagDebug, o + Log.getStackTraceString(e), 0, 0, D, args);
    }

    public static void sd(Object o, int depth) {
        log(tagDebug, o, 0, depth, D);
    }

    public static void sd(boolean open, Object o, int depth) {
        if (!open) {
            return;
        }
        log(tagDebug, o, 0, depth, D);
    }

    public static void sd(Object o, int offset, int depth) {
        log(tagDebug, o, offset, depth, D);
    }

    public static void sd(Object o) {
        log(tagDebug, o, 0, 20, D);
    }

    //------------ Weak -----------------
    public static void w(Object o) {
        log(tagDebug, o, 0, 0, W);
    }

    public static void w(String tag, Object o) {
        log(tag, o, 0, 0, W);
    }

    public static void w(Object o, Object... args) {
        log(tagDebug, o, 0, 0, W, args);
    }

    public static void w(Throwable e, Object o, Object... args) {
        log(tagDebug, o + Log.getStackTraceString(e), 0, 0, W, args);
    }

    public static void wd(Object o, int depth) {
        log(tagDebug, o, 0, depth, W);
    }

    public static void wd(Object o) {
        log(tagDebug, o, 0, 20, W);
    }

    //------------ Info -----------------
    public static void i(Object o) {
        log(tagDebug, o, 0, 0, I);
    }

    public static void i(String tag, Object o) {
        log(tag, o, 0, 0, I);
    }

    public static void i(Object o, Object... args) {
        log(tagDebug, o, 0, 0, I, args);
    }

    public static void i(Throwable e, Object o, Object... args) {
        log(tagDebug, o + Log.getStackTraceString(e), 0, 0, I, args);
    }

    public static void i_stack(Object o, int depth) {
        log(tagDebug, o, 0, depth, I);
    }

    public static void i_stack(Object o) {
        log(tagDebug, o, 0, 20, I);
    }

    //------------ Error -----------------

    public static void e(boolean open, Object o) {
        if (!open) {
            return;
        }
        log(tagDebug, o, 0, 0, E);
    }

    public static void e(Object o) {
        log(tagDebug, o, 0, 0, E);
    }

    public static String e(Throwable e) {
        String error = Log.getStackTraceString(e);
        log(tagDebug, error, 0, 0, E);
        return error;
    }

    public static void e(String tag, Object o) {
        log(tag, o, 0, 0, E);
    }

    public static void e(Object o, Object... args) {
        log(tagDebug, o, 0, 0, E, args);
    }

    public static void e(Throwable e, Object o, Object... args) {
        log(tagDebug, o + Log.getStackTraceString(e), 0, 0, E, args);
    }

    public static void ed(Object o, int depth) {
        log(tagDebug, o, 0, depth, E);
    }

    public static void ed(boolean open, Object o, int depth) {
        if (!open) {
            return;
        }
        log(tagDebug, o, 0, depth, E);
    }

    public static void ed(Object o, int offset, int depth) {
        log(tagDebug, o, offset, depth, E);
    }

    public static void ed(Object o) {
        log(tagDebug, o, 0, 20, E);
    }

    //------------ Line -----------------
    public static void l() {
        log(tagDebug, "------------------", 0, 0, L);
    }

    public static void ll() {
        log(tagDebug, "-----------------------------------------", 0, 0, L);
    }

    public static void lll() {
        log(tagDebug, "--------------------------------------------------", 0, 0, L);
    }

    public static void llll() {
        log(tagDebug, "-----------------------------------------------------------------------", 0, 0, L);
    }

    public static void ls(Object o) {
        log(tagDebug, "------------------" + (o == null ? "" : o), 0, 0, L);
    }

    public static void lls(Object o) {
        log(tagDebug, "-----------------------------------------" + (o == null ? "" : o), 0, 0, L);
    }

    public static void llls(Object o) {
        log(tagDebug, "-----------------------------------------------------------------------" + (o == null ? "" : o), 0, 0, L);
    }

    public static void lllls(Object o) {
        log(tagDebug, "-------------------------------------------------------------------------------------------------------------" + (o == null ? "" : o), 0, 0, L);
    }

    public static void l(String tag) {
        log(tag, "------------------", 0, 0, L);
    }

    public static void ll(String tag) {
        log(tag, "-----------------------------------------", 0, 0, L);
    }

    public static void lll(String tag) {
        log(tag, "-----------------------------------------------------------------------", 0, 0, L);
    }

    public static void llll(String tag) {
        log(tag, "-------------------------------------------------------------------------------------------------------------", 0, 0, L);
    }

    public static void llll(boolean open) {
        if (!open) {
            return;
        }
        log(tagDebug, "-------------------------------------------------------------------------------------------------------------", 0, 0, L);
    }

    public static String getMode(int spec) {
        int mode = View.MeasureSpec.getMode(spec);
        switch (mode) {
            case View.MeasureSpec.AT_MOST:
                return "AT_MOST";
            case View.MeasureSpec.EXACTLY:
                return "EXACTLY";
            case View.MeasureSpec.UNSPECIFIED:
                return "UNSPECIFIED";
            default:
                return "UNKNOWN";
        }
    }

    public static void v(boolean open, String text, int widthMeasureSpec, int heightMeasureSpec, View view) {
        if (!open) {
            return;
        }
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        String marginString = "";
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams1 = (ViewGroup.MarginLayoutParams) layoutParams;
            if (layoutParams1 instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams1;
                marginString = "[" + layoutParams2.getClass().getSimpleName() + "] width:" + layoutParams2.width + " height:"+layoutParams2.height+" weight:"+layoutParams2.weight+" marginLeft:" + layoutParams2.leftMargin + "  rightMargin:" + layoutParams2.rightMargin;
            } else {
                marginString = "[" + layoutParams1.getClass().getSimpleName() + "] marginLeft:" + layoutParams1.leftMargin + "  rightMargin:" + layoutParams1.rightMargin;
            }
        }
        log(tagDebug, text + "  widthSpec:" + width + " [" + getMode(widthMeasureSpec) + "]   heightSpec:" + height + " [" + getMode(heightMeasureSpec) + "]   paddingStart:" + view.getPaddingStart() + "  paddingEnd:" + view.getPaddingEnd() + " " + marginString, 0, 0, D);
    }

    public static void ve(boolean open, String text, int widthMeasureSpec, int heightMeasureSpec, View view) {
        if (!open) {
            return;
        }
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        String marginString = "";
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams1 = (ViewGroup.MarginLayoutParams) layoutParams;
            marginString = " marginLeft:" + layoutParams1.leftMargin + "  rightMargin:" + layoutParams1.rightMargin;
        }
        log(tagDebug, text + "  width:" + width + "[" + getMode(widthMeasureSpec) + "]   height:" + height + "[" + getMode(heightMeasureSpec) + "]   paddingStart:" + view.getPaddingStart() + "  paddingEnd:" + view.getPaddingEnd() + " " + marginString, 0, 0, E);
    }
}
