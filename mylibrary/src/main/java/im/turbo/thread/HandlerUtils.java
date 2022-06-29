package im.turbo.thread;

import android.os.Handler;
import android.os.Looper;
import android.util.Printer;
import android.util.StringBuilderPrinter;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerUtils {
    public static void dump(@NonNull Handler handler, @NonNull Printer printer, @NonNull String prefix) {
        Method dumpMtd = ReflectionUtils.findMethodNoThrow(Looper.class, "dump",
                Printer.class, String.class, Handler.class);
        if (dumpMtd != null) {
            try {
                dumpMtd.invoke(handler.getLooper(), printer, prefix, handler);
            } catch (IllegalAccessException | InvocationTargetException e) {
            }
        } else {
        }
    }

    public static void dumpHandler(@NonNull Handler handler, @NonNull String name) {
        StringBuilder sb = new StringBuilder(4096);
        StringBuilderPrinter printer = new StringBuilderPrinter(sb);
        HandlerUtils.dump(handler, printer, "");
    }
}
