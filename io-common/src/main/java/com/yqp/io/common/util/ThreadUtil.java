package com.yqp.io.common.util;

public class ThreadUtil {

    public static void hangCurrentThread() {
        try {
            while (true) {
                Thread.sleep(60000);
            }
        } catch (InterruptedException e) {
            System.err.println("error in hangCurrentThread");
        }
    }
}
