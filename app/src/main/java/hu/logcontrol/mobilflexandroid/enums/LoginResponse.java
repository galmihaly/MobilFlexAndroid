package hu.logcontrol.mobilflexandroid.enums;

public class LoginResponse {
    public static final int Undefined = -1;
    public static final int OK = 0;
    public static final int InvalidRequestGuid  = 1;
    public static final int SqlError  = 2;
    public static final int DataServiceInstanceDoesNotExist  = 3;
    public static final int UserDoesNotExists   = 100;
    public static final int UserInactive  = 101;
    public static final int UserAccessDenied  = 102;
    public static final int InvalidCredentials  = 103;
}
