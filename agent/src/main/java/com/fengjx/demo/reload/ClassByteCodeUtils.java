package com.fengjx.demo.reload;


import org.objectweb.asm.ClassReader;

public class ClassByteCodeUtils {

    public static String getClassNameFromByteCode(byte[] bytes) {
        ClassReader classReader = new ClassReader(bytes);
        String className = classReader.getClassName();
        return className.replaceAll("/", ".");
    }

}
