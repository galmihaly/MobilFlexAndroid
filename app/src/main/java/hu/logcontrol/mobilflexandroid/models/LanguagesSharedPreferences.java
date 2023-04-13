package hu.logcontrol.mobilflexandroid.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class LanguagesSharedPreferences {

    private Context context;
    private String fileName;

    private SharedPreferences languageSharedPreferenceFile;

    public LanguagesSharedPreferences(Context context, String fileName) {
        this.context = context.getApplicationContext();
        this.fileName = fileName;
        this.languageSharedPreferenceFile = context.getSharedPreferences(this.fileName, Context.MODE_PRIVATE);
    }

    @SuppressLint("CommitPrefEdits")
    public void putString(String key, String value){
        if(!this.languageSharedPreferenceFile.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.languageSharedPreferenceFile.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void putBoolean(String key, boolean value){
        if(!this.languageSharedPreferenceFile.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.languageSharedPreferenceFile.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void putFloat(String key, float value){
        if(!this.languageSharedPreferenceFile.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.languageSharedPreferenceFile.edit();
            editor.putFloat(key, value);
            editor.apply();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void putInt(String key, int value){
        if(!this.languageSharedPreferenceFile.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.languageSharedPreferenceFile.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void putLong(String key, long value){
        if(!this.languageSharedPreferenceFile.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.languageSharedPreferenceFile.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void replaceString(String key, String value){
        SharedPreferences.Editor editor;
        editor = this.languageSharedPreferenceFile.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public void replaceBoolean(String key, boolean value){
        SharedPreferences.Editor editor;
        editor = this.languageSharedPreferenceFile.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public void replaceFloat(String key, float value){
        SharedPreferences.Editor editor;
        editor = this.languageSharedPreferenceFile.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public void replaceInt(String key, int value){
        SharedPreferences.Editor editor;
        editor = this.languageSharedPreferenceFile.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public void replaceLong(String key, long value){
        SharedPreferences.Editor editor;
        editor = this.languageSharedPreferenceFile.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void removeItemByKey(String key){
        if(this.languageSharedPreferenceFile.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.languageSharedPreferenceFile.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public void removeStringItemByValue(String key, String value){
        if(this.languageSharedPreferenceFile.contains(key)){
            String string = this.languageSharedPreferenceFile.getString(key, null);

            if(string.equals(value)){
                SharedPreferences.Editor editor;
                editor = this.languageSharedPreferenceFile.edit();
                editor.remove(key);
                editor.apply();
            }
        }
    }

    public void removeBooleanItemByValue(String key, boolean value){
        if(this.languageSharedPreferenceFile.contains(key)){
            boolean bool = this.languageSharedPreferenceFile.getBoolean(key, false);

            if(bool == value){
                SharedPreferences.Editor editor;
                editor = this.languageSharedPreferenceFile.edit();
                editor.remove(key);
                editor.apply();
            }
        }
    }

    public void removeFloatItemByValue(String key, float value){
        if(this.languageSharedPreferenceFile.contains(key)){
            float f = this.languageSharedPreferenceFile.getFloat(key, Float.MAX_VALUE);

            if(f == value){
                SharedPreferences.Editor editor;
                editor = this.languageSharedPreferenceFile.edit();
                editor.remove(key);
                editor.apply();
            }
        }
    }

    public void removeIntegerItemByValue(String key, int value){
        if(this.languageSharedPreferenceFile.contains(key)){
            int i = this.languageSharedPreferenceFile.getInt(key, Integer.MAX_VALUE);

            if(i == value){
                SharedPreferences.Editor editor;
                editor = this.languageSharedPreferenceFile.edit();
                editor.remove(key);
                editor.apply();
            }
        }
    }

    public void removeLongItemByValue(String key, long value){
        if(this.languageSharedPreferenceFile.contains(key)){
            long l = this.languageSharedPreferenceFile.getLong(key, Long.MAX_VALUE);

            if(l == value){
                SharedPreferences.Editor editor;
                editor = this.languageSharedPreferenceFile.edit();
                editor.remove(key);
                editor.apply();
            }
        }
    }

    public String getStringValueByKey(String key){
        if(this.languageSharedPreferenceFile.contains(key)){
            return this.languageSharedPreferenceFile.getString(key, "");
        };
        return null;
    }

    public boolean getBooleanValueByKey(String key){
        if(this.languageSharedPreferenceFile.contains(key)) {
            return this.languageSharedPreferenceFile.getBoolean(key, false);
        }

        return false;
    }

    public float getFloatValueByKey(String key) {
        if (this.languageSharedPreferenceFile.contains(key)) {
            return this.languageSharedPreferenceFile.getFloat(key, Float.MAX_VALUE);
        }

        return Float.MAX_VALUE;
    }

    public long getLongValueByKey(String key){
        if(this.languageSharedPreferenceFile.contains(key)) {
            return this.languageSharedPreferenceFile.getLong(key, Long.MAX_VALUE);
        }

        return Long.MAX_VALUE;
    }

    public int getIntValueByKey(String key){
        if(this.languageSharedPreferenceFile.contains(key)) {
            return this.languageSharedPreferenceFile.getInt(key, Integer.MAX_VALUE);
        }

        return Integer.MAX_VALUE;
    }

    public boolean getBoolean(String key){
        return this.languageSharedPreferenceFile.getBoolean(key, false);
    }

    public float getFloat(String key){ return this.languageSharedPreferenceFile.getFloat(key, Float.MAX_VALUE); }

    public long getLong(String key){
        return this.languageSharedPreferenceFile.getLong(key, Long.MAX_VALUE);
    }

    public int getInt(String key){
        return this.languageSharedPreferenceFile.getInt(key, Integer.MAX_VALUE);
    }
}
