package me.cheesepro.reality.utils;

import me.cheesepro.reality.Reality;

/**
 * Created by Mark on 2015-04-02.
 */
public class Logger {

    public void info(String in){
        print("[INFO] " + in);
    }

    public void warn(String in){
        print("[WARN] " + in);
    }

    public void error(Exception e){
        print("[ERROR] ====[>>Printing error stack trace<<]====");
        e.printStackTrace();
        print("[ERROR] ===[>End printing error stack trace<]===");
    }

    private void print(String in){
        System.out.println(Reality.pName + in);
    }

}
