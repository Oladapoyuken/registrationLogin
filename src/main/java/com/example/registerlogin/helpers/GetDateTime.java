package com.example.registerlogin.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GetDateTime {
    public String getDateTime(int minutes){
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(minutes);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(formatter);
    }
    public String getDateTime(){
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(formatter);
    }
}
