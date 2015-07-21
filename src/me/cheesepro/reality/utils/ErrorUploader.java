package me.cheesepro.reality.utils;

import org.bukkit.Bukkit;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Mark on 2015-05-27.
 */
public class ErrorUploader extends Handler{

    public void attachToBukkit(){
        Bukkit.getLogger().addHandler(this);
    }


    @Override
    public void close() throws SecurityException {

    }

    @Override
    public void flush() {

    }

    @Override
    public void publish(LogRecord arg0) {
        System.out.print("OLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOL");
        if(arg0.getMessage().contains("Exception")){
            //upload dis
        }
    }

}
