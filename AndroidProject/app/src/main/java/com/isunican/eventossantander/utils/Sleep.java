package com.isunican.eventossantander.utils;

public class Sleep {

    private Sleep(){}

    public static void sleep(int milis){
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
