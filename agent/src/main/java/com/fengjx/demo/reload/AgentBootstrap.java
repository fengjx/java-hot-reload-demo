package com.fengjx.demo.reload;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AgentBootstrap {

    /**
     * 启动时加载
     */
    public static void premain(String args, Instrumentation inst) {
        System.out.println("premain agent args: " + args);
    }

    /**
     * 运行时加载（attach）
     */
    public static void agentmain(String args, Instrumentation inst) {
        System.out.println("agentmain agent args：" + args);
        process(args, inst);
    }

    private static void process(String args, Instrumentation inst) {
        if (args == null) {
            throw new IllegalArgumentException("Agent args is null");
        }
        try {
            File file = Paths.get(args).toFile();
            byte[] newClazzByteCode = Files.readAllBytes(file.toPath());
            String className = ClassByteCodeUtils.getClassNameFromByteCode(newClazzByteCode);
            HotReloadWorker.doReloadClassFile(inst, className, newClazzByteCode);
        } catch (Exception e) {
            System.err.println("Reload failed: " + e.getMessage());
        }
    }


}
