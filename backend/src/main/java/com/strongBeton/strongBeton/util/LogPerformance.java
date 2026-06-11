package com.strongBeton.strongBeton.util;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class LogPerformance {
    private File file;
    private Workbook workbook;
    private long startTime = 0;
    private long endTime;
    private Queue<PerformanceRecord> queue;

    public LogPerformance(){
        queue = new LinkedList<>();
    }

    public void startTimer(){
        startTime = System.nanoTime();
    }
    public void stopTimer(String nameOfMethod){
        endTime = System.nanoTime();
        int time = Math.toIntExact((endTime - startTime) / 1000000);
        queue.add(new PerformanceRecord(nameOfMethod, time));
        startTime = 0;
        endTime = 0;
        saveResult();
    }

    public void saveResult(){
        for (var result : queue){
            System.out.println(result.getMethodName() + " " + result.getTimeForSuccess());
        }
    }
}
