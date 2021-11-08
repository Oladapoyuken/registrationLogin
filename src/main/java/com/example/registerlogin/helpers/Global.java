package com.example.registerlogin.helpers;

import java.io.File;

public class Global {
    public static char separator = File.separatorChar;
    public static final String PROFILE_DIR = System.getProperty("user.dir") +
            separator + "uploads" + separator;
    public static  String JWT_SECRET_KEY = "automation";
}
