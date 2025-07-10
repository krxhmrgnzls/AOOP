package com.motorph.dao;

import com.motorph.model.AttendanceRecord;

public class AttendanceResult {
    private boolean success;
    private String message;
    private AttendanceRecord attendanceRecord;
    
    public AttendanceResult(boolean success, String message, AttendanceRecord record) {
        this.success = success;
        this.message = message;
        this.attendanceRecord = record;
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public AttendanceRecord getAttendanceRecord() { return attendanceRecord; }
}

