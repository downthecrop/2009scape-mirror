package org.runite.client;

public final class TimeUtils {

    private static long correction;
    private static long previous;

    public static synchronized long time() {
        long time = System.currentTimeMillis();
        if (time < previous) {
            correction += previous + -time;
        }

        previous = time;
        return correction + time;
    }

    public static void sleep(long millis) {
        if (0L < millis) {
            if (0L == millis % 10L) {
                sleepWrapped(millis + -1L);
                sleepWrapped(1L);
            } else {
                sleepWrapped(millis);
            }
        }
    }

    private static void sleepWrapped(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException var4) {
        }
    }
}
