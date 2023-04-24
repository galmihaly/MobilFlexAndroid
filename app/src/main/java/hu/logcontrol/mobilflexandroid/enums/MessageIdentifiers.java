package hu.logcontrol.mobilflexandroid.enums;

public class MessageIdentifiers {

    public static final String MESSAGE_BODY = "MESSAGE_BODY";

    //TODO : -> ezt majd át kell szervezni, úgy hogy számok helyett enumokat lehessen használni

    //Buttons created enums
    public static final int BUTTONS_IS_CREATED = 1;
    public static final int BUTTONS_NOT_CREATED = 2;

    //Buttons IDs
    public static final int BUTTON_USER_PASS = 10;
    public static final int BUTTON_PINCODE = 11;
    public static final int BUTTON_RFID = 12;
    public static final int BUTTON_BARCODE = 13;

    // Size of Buttons List
    public static final int BUTTONS_LIST_SIZE = 100;

    // CreateOrderItemsResponseEnums
    public static final int ADAPTER_CREATED_SUCCES = 20;
    public static final int ADAPTER_CREATED_FAILED = 21;

    //ERROR identifiers
    public static final int EXCEPTION = -2;
    public static final int HARDWARE_ID_FAILED = -3;
}
