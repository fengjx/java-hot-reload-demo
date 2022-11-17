package com.fengjx.demo.reload;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author FengJianxin
 */
public class JvmAttach {

    public static void main(String[] args) throws Exception {
        File file = Paths.get("agent.jar").toFile();
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            if (vmd.displayName().endsWith("app.jar")) {
                String pid = vmd.id();
                System.out.println("app pid: " + pid);
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                virtualMachine.loadAgent(file.getAbsolutePath(), args[0]);
                virtualMachine.detach();
            }
        }
    }

}
