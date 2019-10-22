package ru.otus;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.MBeanServer;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
О формате логов
http://openjdk.java.net/jeps/158


-Xms512m
-Xmx512m
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/dump
-XX:+UseG1GC
*/

/*
1)
    default, time: 83 sec (82 without Label_1)
2)
    -XX:MaxGCPauseMillis=100000, time: 82 sec //Sets a target for the maximum GC pause time.
    -XX:GCPauseIntervalMills=
3)
    -XX:MaxGCPauseMillis=10, time: 91 sec

4)
-Xms2048m
-Xmx2048m
    time: 81 sec

5)
-Xms5120m
-Xmx5120m
    time: 80 sec

5)
-Xms20480m
-Xmx20480m
    time: 81 sec (72 without Label_1)

*/

public class GcDemo {
    private static final Set<String> YOUNG_GC = new HashSet<>(3);
    private static final Set<String> OLD_GC = new HashSet<>(3);

    static {
        YOUNG_GC.add("PS Scavenge");
        YOUNG_GC.add("ParNew");
        YOUNG_GC.add("G1 Young Generation");
        YOUNG_GC.add("Copy");
        OLD_GC.add("PS MarkSweep");
        OLD_GC.add("ConcurrentMarkSweep");
        OLD_GC.add("G1 Old Generation");
        OLD_GC.add("MarkSweepCompact");
    }

    public static void main(String... args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnMonitoring();
        long beginTime = System.currentTimeMillis();

        int size = 5 * 1000 * 1500;
        int loopCounter = 1000;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName name = new ObjectName("ru.otus:type=Benchmark");

        Benchmark mbean = new Benchmark(loopCounter);
        mbs.registerMBean(mbean, name);
        mbean.setSize(size);
        mbean.run();

        System.out.println("time:" + (System.currentTimeMillis() - beginTime) / 1000);
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;

            int[] timeCounter = {1};

            long[] data = new long[4];
            NotificationListener listener = (notification, handback) -> {

                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    if (timeCounter[0] * 60000 >= startTime) {
                        if (YOUNG_GC.contains(gcName)) {
                            data[0] += 1;
                            data[1] += duration;
                        }
                        if (OLD_GC.contains(gcName)) {
                            data[2] += 1;
                            data[3] += duration;
                        }
                    } else {
                        timeCounter[0] += 1;
                        if (data[0] != 0 && data[1] != 0)
                            System.out.println(String.format("Young, Times: %d, GC time: %d ms",
                                    data[0], data[1]));

                        if (data[2] != 0 && data[3] != 0)
                            System.out.println(String.format("Old, Times: %d, GC time: %d ms",
                                    data[2], data[3]));

                        for (int i = 0; i < 4; i++) {
                            data[i] = 0;
                        }
                    }
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}
