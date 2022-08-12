package com.luzhi.thread.job;

import cn.hutool.core.io.FileUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 获取其他语言程序或者脚本的地址.
 */

@Component
public class ScriptRun extends Thread {

    @Override
    public void run() {
        try {
            SharesScriptRun sharesScriptRun = new SharesScriptRun();
            sharesScriptRun.start();
            sharesScriptRun.join();
            ScriptReplaceRun scriptReplaceRun = new ScriptReplaceRun();
            scriptReplaceRun.start();
            scriptReplaceRun.join();
            SharesMoveRun sharesMoveRun = new SharesMoveRun();
            sharesMoveRun.start();
            sharesMoveRun.join();
        } catch (InterruptedException interruptedException) {
            System.out.println("异常原因:" + interruptedException.getMessage());
        }
    }
}

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 执行Python脚本获取最新的股票Json文件.
 */
class SharesScriptRun extends AbstractNeedParamenters {

    @Override
    public void run() {
        String path = "";
        if (SYSTEM_TYPE.contains(MAC_OS_X)) {
            path = runPath("python/shares_script.py");
        } else if (SYSTEM_TYPE.contains(LINUX)) {
            path = runPath("python/shares_script.py");
        } else if (SYSTEM_TYPE.contains(WINDOWS)) {
            path = runPath("python\\shares_script.py");
        } else {
            System.out.println("抱歉仅仅支持POSIX和NT谢谢");
        }
        try {
            String command = "python " + path;
            emptyAndWriteSignalFileAfterJudge();
            Runtime.getRuntime().exec(command);
            singalRun(PYTHON_GAIN_DATA_SUCCESS, 3);
            System.out.println("获取json成功");
        } catch (IOException exception) {
            System.out.println("异常原因:" + exception.getMessage());
        }
    }
}

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 执行Python脚本将获取转换完成的Json写入第三方数据文件夹中
 */
class SharesMoveRun extends AbstractNeedParamenters {

    @Override
    public void run() {
        String path = "";
        if (SYSTEM_TYPE.contains(MAC_OS_X)) {
            path = runPath("python/shares_move.py");
        } else if (SYSTEM_TYPE.contains(LINUX)) {
            path = runPath("python/shares_move.py");
        } else if (SYSTEM_TYPE.contains(WINDOWS)) {
            path = runPath("python\\shares_move.py");
        } else {
            System.out.println("抱歉仅仅支持POSIX和NT谢谢");
        }
        try {
            emptyAndWriteSignalFileAfterJudge();
            Runtime.getRuntime().exec("python " + path);
            singalRun(PYTHON_MOVE_DATA_SUCCESS, 4);
            System.out.println("移动json成功");
        } catch (Exception exception) {
            System.out.println("异常原因:" + exception.getMessage());
        }
    }
}

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/7/21
 * 使用c++中的STL和Json将Python获取的脚本格式化.
 */
class ScriptReplaceRun extends AbstractNeedParamenters {

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        String path = "";
        if (SYSTEM_TYPE.contains(MAC_OS_X)) {
            path = runPath("cplusplus/main");
        } else if (SYSTEM_TYPE.contains(LINUX)) {
            path = runPath("cplusplus/main");
        } else if (SYSTEM_TYPE.contains(WINDOWS)) {
            path = runPath("cplusplus\\main.exe");
        } else {
            System.out.println("抱歉仅仅支持POSIX和NT谢谢");
        }
        try {
            while (!FileUtil.exist(path)) {
                Thread.sleep(5000);
                if (FileUtil.exist(path)) {
                    break;
                }
            }
            emptyAndWriteSignalFileAfterJudge();
            Runtime.getRuntime().exec(path);
            singalRun(CPP_SCRIPT_SUCCESS, 5);
            System.out.println("json转换成功");
        } catch (Exception exception) {
            System.out.println("异常原因:" + exception.getMessage());
        }
    }
}
