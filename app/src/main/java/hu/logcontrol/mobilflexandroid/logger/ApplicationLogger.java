package hu.logcontrol.mobilflexandroid.logger;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import hu.logcontrol.mobilflexandroid.BuildConfig;


public class ApplicationLogger {

    private static List<LogObject> myLogObjects = new ArrayList<>();

    public static void logging(LogLevel logLevel, String message){
        MethodCallCounter.add();

        switch (logLevel){
            case DEBUG: ApplicationLogger.debug(message); break;
            case ERROR: ApplicationLogger.error(message); break;
            case FATAL: ApplicationLogger.fatal(message); break;
            case TRACE: ApplicationLogger.trace(message); break;
            case WARNING: ApplicationLogger.warning(message); break;
            case INFORMATION: ApplicationLogger.info(message); break;
            default:
                break;
        }
    }

    private static void debug(String debugMessage){
        try {
            MethodCallCounter.add();

            MethodCallCounter.addNumber(2);
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[MethodCallCounter.counter - 1];

            String zonedDateTime = getUTCDateTimeString();
            LogObject logObject = new LogObject(LogLevel.DEBUG, stackTraceElement, zonedDateTime, debugMessage);

            if(BuildConfig.DEBUG) Log.d(null, logObject.toString());
            MethodCallCounter.clear();

            myLogObjects.add(logObject);
        }
        catch (ArrayIndexOutOfBoundsException aex){
            aex.getStackTrace();
        }
        catch (SecurityException e){
            //getStackTrace metódus exceptionje
            e.getStackTrace();
        }
    }

    private static void error(String errorMessage){
        try {
            MethodCallCounter.add();

            MethodCallCounter.addNumber(2);
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[MethodCallCounter.counter - 1];

            String zonedDateTime = getUTCDateTimeString();
            LogObject logObject = new LogObject(LogLevel.ERROR, stackTraceElement, zonedDateTime, errorMessage);

            if(BuildConfig.DEBUG) Log.e(null, logObject.toString());
            MethodCallCounter.clear();

            myLogObjects.add(logObject);
        }
        catch (ArrayIndexOutOfBoundsException aex){
            aex.getStackTrace();
        }
        catch (SecurityException e){
            //getStackTrace metódus exceptionje
            e.getStackTrace();
        }
    }

    private static void fatal(String fatalMessage){
        try {
            MethodCallCounter.add();

            MethodCallCounter.addNumber(2);
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[MethodCallCounter.counter - 1];

            String zonedDateTime = getUTCDateTimeString();
            LogObject logObject = new LogObject(LogLevel.FATAL, stackTraceElement, zonedDateTime, fatalMessage);

            if(BuildConfig.DEBUG) Log.e(null, logObject.toString());
            MethodCallCounter.clear();

            myLogObjects.add(logObject);
        }
        catch (ArrayIndexOutOfBoundsException aex){
            aex.getStackTrace();
        }
        catch (SecurityException e){
            //getStackTrace metódus exceptionje
            e.getStackTrace();
        }
    }

    private static void trace(String traceMessage){
        try {
            MethodCallCounter.add();

            MethodCallCounter.addNumber(2);
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[MethodCallCounter.counter - 1];

            String zonedDateTime = getUTCDateTimeString();
            LogObject logObject = new LogObject(LogLevel.TRACE, stackTraceElement, zonedDateTime, traceMessage);

            if(BuildConfig.DEBUG) Log.i(null, logObject.toString());
            MethodCallCounter.clear();

            myLogObjects.add(logObject);
        }
        catch (ArrayIndexOutOfBoundsException aex){
            aex.getStackTrace();
        }
        catch (SecurityException e){
            //getStackTrace metódus exceptionje
            e.getStackTrace();
        }
    }

    private static void warning(String warningMessage){
        try {
            MethodCallCounter.add();

            MethodCallCounter.addNumber(2);
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[MethodCallCounter.counter - 1];

            String zonedDateTime = getUTCDateTimeString();
            LogObject logObject = new LogObject(LogLevel.WARNING, stackTraceElement, zonedDateTime, warningMessage);

            if(BuildConfig.DEBUG) Log.w(null, logObject.toString());
            MethodCallCounter.clear();

            myLogObjects.add(logObject);
        }
        catch (ArrayIndexOutOfBoundsException aex){
            aex.getStackTrace();
        }
        catch (SecurityException e){
            //getStackTrace metódus exceptionje
            e.getStackTrace();
        }
    }

    private static void info(String infoMessage){

        try {
            MethodCallCounter.add();

            MethodCallCounter.addNumber(2);
            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[MethodCallCounter.counter - 1];

            String zonedDateTime = getUTCDateTimeString();
            LogObject logObject = new LogObject(LogLevel.INFORMATION, stackTraceElement, zonedDateTime, infoMessage);

            if(BuildConfig.DEBUG) Log.i(null, logObject.toString());
            MethodCallCounter.clear();

            myLogObjects.add(logObject);

        }
        catch (ArrayIndexOutOfBoundsException aex){
            aex.getStackTrace();
        }
        catch (SecurityException e){
            //getStackTrace metódus exceptionje
            e.getStackTrace();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getUTCDateTimeString(){

        String result = null;

        try {
            Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            result = df.format(date.getTimeInMillis()) + " " + date.getTimeZone().getID();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(result == null) return null;
        return result;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateTimeString(){

        String result = null;

        try {
            Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            result = df.format(date.getTimeInMillis());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(result == null) return null;
        return result;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateTimeFileString(){

        String result = null;

        try {
            Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            result = df.format(date.getTimeInMillis());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(result == null) return null;
        return result;
    }
}
