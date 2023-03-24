package com.leo.bedwars.misc;

import java.util.Random;

public class Utils {

    // todo something more sofisitcated
    public static String generateId() {
        return "bw" + new Random().nextInt(100, 999);
    }

}
