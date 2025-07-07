package payrollsystem;

public class AttendanceStatus {
    private boolean canTimeIn;
    private boolean canTimeOut;
    private String statusMessage;
    
    public AttendanceStatus(boolean canTimeIn, boolean canTimeOut, String message) {
        this.canTimeIn = canTimeIn;
        this.canTimeOut = canTimeOut;
        this.statusMessage = message;
    }
    
    public boolean canTimeIn() { return canTimeIn; }
    public boolean canTimeOut() { return canTimeOut; }
    public String getStatusMessage() { return statusMessage; }
}