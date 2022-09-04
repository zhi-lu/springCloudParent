package com.luzhi.thread.job;

import cn.hutool.core.io.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhilu
 * @version jdk1.8
 * @date 2022/5/21
 * <p>
 * </br> 由于部分需要下列参数的类需要继承{@link Thread}.所以使用抽象类型来定义所需的参数(自下而上).
 */
public abstract class AbstractNeedParamenters extends Thread {

    /**
     * 初始信号量文件的内容为1个字符.
     */
    private static final int INIT_SIGNAL_CHAR_NUMBER = 1;

    /**
     * 初始化自旋程序自旋的个数为15
     */
    private static final int INIT_SPINING_LOCK_NUMBER = 15;

    /**
     * 兼容Mac系统的字符串
     */
    protected static final String MAC_OS_X = "Mac";

    /**
     * 兼容Windows系统字符串
     */
    protected static final String WINDOWS = "Win";

    /**
     * 兼容LINUX系统的字符串
     */
    protected static final String LINUX = "Lin";

    /**
     * 存放信号量的文件名.<p>{使用信号量机制来解决多语言脚本运行的串行问题}</p>
     */
    private static final String SINGAL_FILE_NAME = "signal.txt";

    /**
     * python 脚本获取数据成功的信号量
     */
    protected static final String PYTHON_GAIN_DATA_SUCCESS = "gainSuccess";

    /**
     * cpp 脚本对原始“json”数据格式化成功的信号量
     */
    protected static final String CPP_SCRIPT_SUCCESS = "replaceSuccess";

    /**
     * python 脚本移动数据成功
     */
    protected static final String PYTHON_MOVE_DATA_SUCCESS = "moveSuccess";

    /**
     * 获取当前操作系统的类型
     * <p>
     * 使用的方法为{@link System#getProperty(String)}.
     */
    protected static final String SYSTEM_TYPE = System.getProperty("os.name");


    /**
     * 通过自旋锁来保证多语言之间的线程能够依次执行.
     *
     * @param singalSuccess 脚本执行的信号量.例如{@link AbstractNeedParamenters#CPP_SCRIPT_SUCCESS}.
     * @param seconds       自旋挂起未能执行进入阻塞队列的再处理的秒数.
     */
    @SuppressWarnings({"BusyWait", "AlibabaCountDownShouldInFinally"})
    public static void singalRun(String singalSuccess, int seconds) {
        String filePath = returnPath() + SINGAL_FILE_NAME;
        CountDownLatch latch = new CountDownLatch(INIT_SPINING_LOCK_NUMBER);
        for (; ; ) {
            try {
                File file = new File(filePath);
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String message = reader.readLine().toLowerCase(Locale.ROOT);
                if (message.length() > INIT_SIGNAL_CHAR_NUMBER) {
                    if (singalSuccess.toLowerCase(Locale.ROOT).contains(message)) {
                        System.out.format("script %s runing ----- ", singalSuccess);
                        reader.close();
                        return;
                    } else {
                        System.err.println("脚本执行失败");
                        System.exit(1);
                    }
                }
                // 每次自旋后倒计时计数器每次减一
                latch.countDown();
                reader.close();
                // 自旋锁,每隔指定秒数进行处理.
                if (latch.getCount() == 0){
                    Thread.sleep(seconds * 1000L);
                    latch = new CountDownLatch(INIT_SPINING_LOCK_NUMBER);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                System.err.println("异常信息:" + exception.getMessage());
                System.exit(1);
            }
        }
    }

    /**
     * 判断信号文件是否存在,如果不存在则创建。将上一次写入信号量文件的内容覆盖写入字符'a'.
     */
    public static void emptyAndWriteSignalFileAfterJudge() {
        String filePath = returnPath() + SINGAL_FILE_NAME;
        if (!FileUtil.exist(filePath)) {
            try {
                File file = new File(filePath);
                if (file.createNewFile()) {
                    System.out.println("信号量文件signal.txt创建成功!");
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                System.exit(1);
            }
        }
        try {
            // 将原先的内容覆盖
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(97);
            fileOutputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 通过当前的操作系统来执行具体的脚本的文件{仅仅支持NT和UNIX}.
     *
     * @param path 执行的脚本地址
     * @return operatorPath 返回需要执行的脚本地址{绝对地址}.
     */
    public static @NotNull String runPath(String path) {
        String operatorPath;
        if (SYSTEM_TYPE.contains(MAC_OS_X) || SYSTEM_TYPE.contains(LINUX) || SYSTEM_TYPE.contains(WINDOWS)) {
            operatorPath = returnPath() + path;
        } else {
            return "";
        }
        return operatorPath;
    }

    /**
     * 获取当前该微服务的运行环境地址.
     *
     * @return path 返回环境地址
     */
    private static @NotNull String returnPath() {
        String path = System.getProperty("user.dir");
        if (SYSTEM_TYPE.contains(MAC_OS_X)) {
            path = path + "/third-part-index-data-update-service/src/main/";
        } else if (SYSTEM_TYPE.contains(LINUX)) {
            path = path + "/third-part-index-data-update-service/src/main/";
        } else if (SYSTEM_TYPE.contains(WINDOWS)) {
            path = path + "\\third-part-index-data-update-service\\src\\main\\";
        } else {
            System.out.println("抱歉仅仅支持POSIX和NT谢谢");
            return "";
        }
        return path;
    }
}
