package com.example.discussion_app;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MesTest {
    public static void main(String[] args) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy : hh'h' mm'min' ss's' ");
        String formatDate = sdf.format(date);
        System.out.println("La date est: " + formatDate);
    }
}
