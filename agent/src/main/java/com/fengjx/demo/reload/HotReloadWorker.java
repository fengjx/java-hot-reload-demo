package com.fengjx.demo.reload;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HotReloadWorker {

    private static final Map<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();

    public static void doReloadClassFile(Instrumentation instrumentation, String className,
                                         byte[] newClazzByteCode) throws UnmodifiableClassException, ClassNotFoundException {
        Class<?> clazz = getToReloadClass(instrumentation, className, newClazzByteCode);
        if (clazz == null) {
            System.out.println("Class " + className + " not found");
        } else {
            instrumentation.redefineClasses(new ClassDefinition(clazz, newClazzByteCode));
            System.out.println("Class: " + clazz + " reload success!");
        }
    }

    private static Class<?> getToReloadClass(Instrumentation instrumentation, String className,
                                             byte[] newClazzByteCode) {
        Class<?> clazz = findTargetClass(className, instrumentation);
        if (clazz == null) {
            clazz = defineNewClass(className, newClazzByteCode);
        }
        return clazz;
    }

    private static Class<?> defineNewClass(String className, byte[] newClazzByteCode) {
        System.out.println("Class " + className + " not found, try to define a new class");
        ClassLoader classLoader = HotReloadWorker.class.getClassLoader();
        Class<?> clazz = null;
        try {
            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class,
                    byte[].class, int.class, int.class);
            defineClass.setAccessible(true);
            clazz = (Class<?>) defineClass.invoke(classLoader, className, newClazzByteCode
                    , 0, newClazzByteCode.length);
            System.out.println("Class " + className + " define success " + clazz);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("defineNewClass " + className + " failed ");
        }
        return clazz;
    }

    @SuppressWarnings("rawtypes")
    private static Class<?> findTargetClass(String className, Instrumentation instrumentation) {
        return CLASS_CACHE.computeIfAbsent(className, clazzName -> {
            Class[] allLoadedClasses = instrumentation.getAllLoadedClasses();
            return Arrays.stream(allLoadedClasses)
                    .parallel()
                    .filter(clazz -> clazzName.equals(clazz.getName()))
                    .findFirst()
                    .orElse(null);
        });
    }
}
