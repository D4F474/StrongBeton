package com.strongBeton.strongBeton.util;

public class PerformanceRecord {

    private String methodName;
    private int timeForSuccess;

    public PerformanceRecord(String methodName, int timeForSuccess) {
        this.methodName = methodName;
        this.timeForSuccess = timeForSuccess;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getTimeForSuccess() {
        return timeForSuccess;
    }
}
