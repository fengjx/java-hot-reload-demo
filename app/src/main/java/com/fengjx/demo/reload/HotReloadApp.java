package com.fengjx.demo.reload;

/**
 * @author FengJianxin
 */
public class HotReloadApp {


    public static void main(String[] args) {
        Hello hello = new Hello();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                hello.say();
                try {
                    Thread.sleep(3 * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
