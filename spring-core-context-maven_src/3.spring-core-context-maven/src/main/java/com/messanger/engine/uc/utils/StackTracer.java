package com.messanger.engine.uc.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class StackTracer {
    /**
     * throwable을 trace한다.
     *
     *@param java.lang.Throwable Throwable
     *@return java.lang.String
     */
    public static final String stackTrace(Throwable e) {
        String printStackTrace = null;

        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            e.printStackTrace(new PrintWriter(buf, true));
            printStackTrace = buf.toString();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return printStackTrace;
    }

    /**
     * 
     * @param e
     * @param addPre
     * @return
     */
    public static final String stackTrace(Throwable e, boolean addPre) {
        if (addPre)
            return "<pre>" + stackTrace(e) + "</pre>";
        else
            return stackTrace(e);
    }
}
