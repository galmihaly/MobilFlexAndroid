package hu.logcontrol.mobilflexandroid.logger;

import android.annotation.SuppressLint;

public class LogObject {

    private final LogLevel loggingLevel;
    private final StackTraceElement stackTraceElement;
    private final String zonedDateTime;
    private final String message;

    public LogObject(LogLevel loggingLevel, StackTraceElement stackTraceElement, String zonedDateTime, String message) {
        this.loggingLevel = loggingLevel;
        this.stackTraceElement = stackTraceElement;
        this.zonedDateTime = zonedDateTime;
        this.message = message;
    }

    public LogLevel getLoggingLevel() {
        return loggingLevel;
    }

    public String getZonedDateTime() {
        return zonedDateTime;
    }

    public String getMessage() {
        return message;
    }

    public String getStackTraceClassName(){
        return this.stackTraceElement.getClassName();
    }

    public String getStackTraceFileName(){
        return this.stackTraceElement.getFileName();
    }

    public int getStackTraceLineNumber(){
        return this.stackTraceElement.getLineNumber();
    }

    public String getStackTraceMethodName(){ return this.stackTraceElement.getMethodName(); }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        if(this.loggingLevel.equals(LogLevel.ERROR) || this.loggingLevel.equals(LogLevel.FATAL)){
            return String.format("[%s] -> (%s) - Üzenet: %s Hiba helye: %s (%d. sor) | Függvény: %s",
                    this.getLoggingLevel(),
                    this.getZonedDateTime(),
                    this.getMessage(),
                    this.getStackTraceFileName(),
                    this.getStackTraceLineNumber(),
                    this.getStackTraceMethodName());
        }

        return String.format("[%s] -> (%s) - Üzenet: %s Logolás helye: %s (%d. sor) | Függvény: %s",
                this.getLoggingLevel(),
                this.getZonedDateTime(),
                this.getMessage(),
                this.getStackTraceFileName(),
                this.getStackTraceLineNumber(),
                this.getStackTraceMethodName());
    }
}
