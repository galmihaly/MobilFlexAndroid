package hu.logcontrol.mobilflexandroid.tasks;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.enums.MessageIdentifiers;
import hu.logcontrol.mobilflexandroid.enums.WindowSizeTypes;
import hu.logcontrol.mobilflexandroid.helpers.Helper;
import hu.logcontrol.mobilflexandroid.logger.ApplicationLogger;
import hu.logcontrol.mobilflexandroid.logger.LogLevel;
import hu.logcontrol.mobilflexandroid.taskmanager.CustomThreadPoolManager;

public class CreateLoginButtons implements Callable {
    private WeakReference<CustomThreadPoolManager> ctpmw;
    private int modesNumber;
    private Context context;
    private List<Integer> typesOfLoginButton = new ArrayList<>();
    private WindowSizeTypes[] wsc;

    //LoginButtons
    private ImageButton loginAccAndPassButton = null;
    private ImageButton loginPinButton = null;
    private ImageButton loginRFIDButton = null;
    private ImageButton loginBarcodeButton = null;

    //StyleParameters
    private int height;
    private int width;
    private int[] margins;
    private int imageUserPassId;
    private int imageRFIDId;
    private int imageBarcodeId;
    private int imagePincodeId;

    //Message
    private Message message;
    private List<ImageButton> resultImageButtonList;

    public CreateLoginButtons(Context context, int modeNumber, WindowSizeTypes[] wsc) {
        this.modesNumber = modeNumber;
        this.context = context;
        this.wsc = wsc;
    }

    public void setCustomThreadPoolManager(CustomThreadPoolManager customThreadPoolManager) {
        this.ctpmw = new WeakReference<>(customThreadPoolManager);
    }

    @Override
    public Object call() {

        try{

            if (Thread.interrupted()) throw new InterruptedException();

            if(modesNumber == 1 || modesNumber == 2 || modesNumber == 4 || modesNumber == 8){
                typesOfLoginButton.add(modesNumber);
            }
            else if(modesNumber == 3){
                typesOfLoginButton.add(1);
                typesOfLoginButton.add(2);
            }
            else if(modesNumber == 5){
                typesOfLoginButton.add(1);
                typesOfLoginButton.add(4);
            }
            else if(modesNumber == 6){
                typesOfLoginButton.add(2);
                typesOfLoginButton.add(4);
            }
            else if(modesNumber == 7){
                typesOfLoginButton.add(1);
                typesOfLoginButton.add(2);
                typesOfLoginButton.add(4);
            }
            else if(modesNumber == 9){
                typesOfLoginButton.add(1);
                typesOfLoginButton.add(8);
            }
            else if(modesNumber == 10){
                typesOfLoginButton.add(2);
                typesOfLoginButton.add(8);
            }
            else if(modesNumber == 11){
                typesOfLoginButton.add(1);
                typesOfLoginButton.add(2);
                typesOfLoginButton.add(8);
            }
            else if(modesNumber == 12){
                typesOfLoginButton.add(4);
                typesOfLoginButton.add(8);
            }
            else if(modesNumber == 13){
                typesOfLoginButton.add(1);
                typesOfLoginButton.add(4);
                typesOfLoginButton.add(8);
            }
            else if(modesNumber == 14){
                typesOfLoginButton.add(2);
                typesOfLoginButton.add(4);
                typesOfLoginButton.add(8);
            }
            else if(modesNumber == 15){
                typesOfLoginButton.add(1);
                typesOfLoginButton.add(2);
                typesOfLoginButton.add(4);
                typesOfLoginButton.add(8);
            }

            message = Helper.createMessage(MessageIdentifiers.BUTTONS_LIST_SIZE, "A létrehozandó gombok száma!");
            message.obj = typesOfLoginButton.size();

            if(ctpmw != null && ctpmw.get() != null && message != null) { ctpmw.get().sendResultToPresenter(message); }

            // mobile - portrait
            if(wsc[0] == WindowSizeTypes.COMPACT && wsc[1] == WindowSizeTypes.MEDIUM){
                setValuesForButtons(new int[] {10, 10, 10, 10}, 110, 110, false);
            }
            else if(wsc[0] == WindowSizeTypes.MEDIUM && wsc[1] == WindowSizeTypes.COMPACT){
                setValuesForButtons(new int[] {10, 10, 10, 10}, 110, 110, false);
            }
            // mobile - landscape
            else if(wsc[0] == WindowSizeTypes.COMPACT && wsc[1] == WindowSizeTypes.EXPANDED){
                setValuesForButtons(new int[] {15, 10, 15, 10}, 110, 110, false);
            }
            // tablet - portrait
            else if(wsc[0] == WindowSizeTypes.EXPANDED && wsc[1] == WindowSizeTypes.MEDIUM){
                setValuesForButtons(new int[] {30, 10, 30, 20}, 120, 120, false);
            }
            // tablet - landscape
            else if(wsc[0] == WindowSizeTypes.MEDIUM && wsc[1] == WindowSizeTypes.EXPANDED){
                setValuesForButtons(new int[] {30, 10, 30, 10}, 120, 120, false);
            }
            // pda - portrait
            else if(wsc[0] == WindowSizeTypes.COMPACT && wsc[1] == WindowSizeTypes.COMPACT){
                setValuesForButtons(new int[] {10, 10, 10, 10}, 90, 90, true);
            }

            resultImageButtonList = new ArrayList<>();

            for (int i = 0; i < typesOfLoginButton.size(); i++) {
                switch (typesOfLoginButton.get(i)){
                    case 0: {
                        savingGlobalMessageHandling(-1, null);
                        break;
                    }
                    case 1:{
                        savingGlobalMessageHandling(1, null);
                        if(loginAccAndPassButton == null) loginAccAndPassButton = new ImageButton(context.getApplicationContext());

                        setStyleForButton(
                                loginAccAndPassButton,
                                imageUserPassId,
                                margins,
                                width,
                                height
                        );

                        savingGlobalMessageHandling(2, null);
                        loginAccAndPassButton.setId(MessageIdentifiers.BUTTON_USER_PASS);
                        resultImageButtonList.add(loginAccAndPassButton);

                        break;
                    }
                    case 2:{
                        savingGlobalMessageHandling(3, null);
                        if(loginPinButton == null) loginPinButton = new ImageButton(context.getApplicationContext());

                        setStyleForButton(
                                loginPinButton,
                                imagePincodeId,
                                margins,
                                width,
                                height
                        );

                        savingGlobalMessageHandling(4, null);
                        loginPinButton.setId(MessageIdentifiers.BUTTON_PINCODE);
                        resultImageButtonList.add(loginPinButton);

                        break;
                    }
                    case 4:{
                        savingGlobalMessageHandling(5, null);
                        if(loginRFIDButton == null) loginRFIDButton = new ImageButton(context.getApplicationContext());

                        setStyleForButton(
                                loginRFIDButton,
                                imageRFIDId,
                                margins,
                                width,
                                height
                        );

                        savingGlobalMessageHandling(6, null);
                        loginRFIDButton.setId(MessageIdentifiers.BUTTON_RFID);
                        resultImageButtonList.add(loginRFIDButton);

                        break;
                    }
                    case 8:{
                        savingGlobalMessageHandling(7, null);
                        if(loginBarcodeButton == null) loginBarcodeButton = new ImageButton(context.getApplicationContext());

                        setStyleForButton(
                                loginBarcodeButton,
                                imageBarcodeId,
                                margins,
                                width,
                                height
                        );

                        savingGlobalMessageHandling(8, null);
                        loginBarcodeButton.setId(MessageIdentifiers.BUTTON_BARCODE);
                        resultImageButtonList.add(loginBarcodeButton);

                        break;
                    }
                }
            }

            message = Helper.createMessage(MessageIdentifiers.BUTTONS_IS_CREATED, "Elkészült!");

            if(ctpmw != null && ctpmw.get() != null && message != null) {
                message.obj = resultImageButtonList;
                ctpmw.get().sendResultToPresenter(message);
            }

        } catch (Exception e){
            savingGlobalMessageHandling(-2, e.getMessage());
        }

        return null;
    }

    private void setValuesForButtons(int[] marginParameters, int widthParameter, int heightParameter, boolean isMiniPDA) {
        if(isMiniPDA){
            margins = marginParameters;
            width = widthParameter;
            height = heightParameter;
            imagePincodeId = R.drawable.mini_keyboard;
            imageRFIDId = R.drawable.mini_rfid;
            imageUserPassId = R.drawable.mini_personalcard;
            imageBarcodeId = R.drawable.mini_barcode;
        }
        else {
            margins = marginParameters;
            width = widthParameter;
            height = heightParameter;
            imagePincodeId = R.drawable.ic_keyboard;
            imageRFIDId = R.drawable.ic_rfid;
            imageUserPassId = R.drawable.ic_personalcard;
            imageBarcodeId = R.drawable.ic_barcode;
        }
    }

    private void setStyleForButton(ImageButton button, int imageId, int[] margins, int width, int height) {
        if(button == null) return;
        if(margins == null) return;

        button.setImageResource(imageId);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        lp.setMargins(margins[0],margins[1], margins[2], margins[3]);

        button.setLayoutParams(lp);
    }

    private void savingGlobalMessageHandling(int messageID, String exceptionMessage) {
        switch (messageID){

            case -1:{ ApplicationLogger.logging(LogLevel.WARNING, "Nem lehet ilyen módon bejelentkezni!"); break; }
            case -2:{
                ApplicationLogger.logging(LogLevel.FATAL, exceptionMessage);
                sendMessageToAdapterHandler(CreateLoginButtonsEnums.EXCEPTION);
                break;
            }

            case 1:{ ApplicationLogger.logging(LogLevel.INFORMATION, "Felhasználónév és jelszó gomb létrehozásának elindítása!"); break; }
            case 2:{ ApplicationLogger.logging(LogLevel.INFORMATION, "Felhasználónév és jelszó gomb létrehozásának befejezése!"); break; }
            case 3: { ApplicationLogger.logging(LogLevel.INFORMATION, "Pinkód gomb létrehozásának elindítása!"); break; }
            case 4:{ ApplicationLogger.logging(LogLevel.INFORMATION, "Pinkód gomb létrehozásának befejezése!"); break; }
            case 5:{ ApplicationLogger.logging(LogLevel.INFORMATION, "RFID gomb létrehozásának elindítása!"); break; }
            case 6: { ApplicationLogger.logging(LogLevel.INFORMATION, "RFID gomb létrehozásának befejezése!"); break; }
            case 7:{ ApplicationLogger.logging(LogLevel.INFORMATION, "Barcode gomb létrehozásának elindítása!"); break; }
            case 8:{ ApplicationLogger.logging(LogLevel.INFORMATION, "Barcode gomb létrehozásának befejezése!"); break; }
        }
    }

    private void sendMessageToAdapterHandler(CreateLoginButtonsEnums createFileEnum){

        switch (createFileEnum){
            case EXCEPTION:{
                message = Helper.createMessage(MessageIdentifiers.EXCEPTION, "Hiba történt!");
                break;
            }
        }

        if(ctpmw != null && ctpmw.get() != null && message != null) {
            ctpmw.get().sendResultToPresenter(message);
        }
    }

    private enum CreateLoginButtonsEnums {
        EXCEPTION,
    }
}
