package hu.logcontrol.mobilflexandroid.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LocalEncryptedPreferences {

    private static LocalEncryptedPreferences mInstance;

    private String filename;
    private KeyGenParameterSpec masterKeyAlias;
    private Context context;
    private EncryptedSharedPreferences.PrefKeyEncryptionScheme keyEncryptionScheme;
    private EncryptedSharedPreferences.PrefValueEncryptionScheme valueEncryptionScheme;

    private SharedPreferences sharedPreferences;

    public LocalEncryptedPreferences(String filename, KeyGenParameterSpec masterKeyAlias, Context context, EncryptedSharedPreferences.PrefKeyEncryptionScheme keyEncryptionScheme, EncryptedSharedPreferences.PrefValueEncryptionScheme valueEncryptionScheme) {
        this.filename = filename;
        this.masterKeyAlias = masterKeyAlias;
        this.context = context;
        this.keyEncryptionScheme = keyEncryptionScheme;
        this.valueEncryptionScheme = valueEncryptionScheme;

        createEncryptedSharedPreferences();
    }

    public static synchronized LocalEncryptedPreferences getInstance(String filename, KeyGenParameterSpec masterKeyAlias, Context context, EncryptedSharedPreferences.PrefKeyEncryptionScheme keyEncryptionScheme, EncryptedSharedPreferences.PrefValueEncryptionScheme valueEncryptionScheme){
        if(mInstance == null){
            mInstance = new LocalEncryptedPreferences(filename, masterKeyAlias, context, keyEncryptionScheme, valueEncryptionScheme);
        }

        return mInstance;
    }

    private void createEncryptedSharedPreferences(){
        try {
            sharedPreferences = EncryptedSharedPreferences.create(filename, MasterKeys.getOrCreate(masterKeyAlias), context.getApplicationContext(), keyEncryptionScheme, valueEncryptionScheme);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void putString(String key, String value){
        if(!sharedPreferences.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void putBoolean(String key, boolean value){
        if(!sharedPreferences.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void putFloat(String key, float value){
        if(!sharedPreferences.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.sharedPreferences.edit();
            editor.putFloat(key, value);
            editor.apply();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void putInt(String key, int value){
        if(!sharedPreferences.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.sharedPreferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void putLong(String key, long value){
        if(!sharedPreferences.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.sharedPreferences.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    @SuppressLint("CommitPrefEdits")
    public void replaceString(String key, String value){
        SharedPreferences.Editor editor;
        editor = this.sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public void replaceBoolean(String key, boolean value){
        SharedPreferences.Editor editor;
        editor = this.sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public void replaceFloat(String key, float value){
        SharedPreferences.Editor editor;
        editor = this.sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public void replaceInt(String key, int value){
        SharedPreferences.Editor editor;
        editor = this.sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public void replaceLong(String key, long value){
        SharedPreferences.Editor editor;
        editor = this.sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void removeItemByKey(String key){
        if(sharedPreferences.contains(key)){
            SharedPreferences.Editor editor;
            editor = this.sharedPreferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public void removeStringItemByValue(String key, String value){
        if(sharedPreferences.contains(key)){
            String string = this.sharedPreferences.getString(key, null);

            if(string.equals(value)){
                SharedPreferences.Editor editor;
                editor = this.sharedPreferences.edit();
                editor.remove(key);
                editor.apply();
            }
        }
    }

    public void removeBooleanItemByValue(String key, boolean value){
        if(sharedPreferences.contains(key)){
            boolean bool = this.sharedPreferences.getBoolean(key, false);

            if(bool == value){
                SharedPreferences.Editor editor;
                editor = this.sharedPreferences.edit();
                editor.remove(key);
                editor.apply();
            }
        }
    }

    public void removeFloatItemByValue(String key, float value){
        if(sharedPreferences.contains(key)){
            float f = this.sharedPreferences.getFloat(key, Float.MAX_VALUE);

            if(f == value){
                SharedPreferences.Editor editor;
                editor = this.sharedPreferences.edit();
                editor.remove(key);
                editor.apply();
            }
        }
    }

    public void removeIntegerItemByValue(String key, int value){
        if(sharedPreferences.contains(key)){
            int i = this.sharedPreferences.getInt(key, Integer.MAX_VALUE);

            if(i == value){
                SharedPreferences.Editor editor;
                editor = this.sharedPreferences.edit();
                editor.remove(key);
                editor.apply();
            }
        }
    }

    public void removeLongItemByValue(String key, long value){
        if(sharedPreferences.contains(key)){
            long l = this.sharedPreferences.getLong(key, Long.MAX_VALUE);

            if(l == value){
                SharedPreferences.Editor editor;
                editor = this.sharedPreferences.edit();
                editor.remove(key);
                editor.apply();
            }
        }
    }

    public String getStringValueByKey(String key){
        if(sharedPreferences.contains(key)){
            return sharedPreferences.getString(key, "");
        };
        return null;
    }

    public boolean getBooleanValueByKey(String key){
        if(sharedPreferences.contains(key)) {
            return sharedPreferences.getBoolean(key, false);
        }

        return false;
    }

    public float getFloatValueByKey(String key) {
        if (sharedPreferences.contains(key)) {
            return sharedPreferences.getFloat(key, Float.MAX_VALUE);
        }

        return Float.MAX_VALUE;
    }

    public long getLongValueByKey(String key){
        if(sharedPreferences.contains(key)) {
            return sharedPreferences.getLong(key, Long.MAX_VALUE);
        }

        return Long.MAX_VALUE;
    }

    public int getIntValueByKey(String key){
        if(sharedPreferences.contains(key)) {
            return sharedPreferences.getInt(key, Integer.MAX_VALUE);
        }

        return Integer.MAX_VALUE;
    }

    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    public float getFloat(String key){ return sharedPreferences.getFloat(key, Float.MAX_VALUE); }

    public long getLong(String key){
        return sharedPreferences.getLong(key, Long.MAX_VALUE);
    }

    public int getInt(String key){
        return sharedPreferences.getInt(key, Integer.MAX_VALUE);
    }
}
