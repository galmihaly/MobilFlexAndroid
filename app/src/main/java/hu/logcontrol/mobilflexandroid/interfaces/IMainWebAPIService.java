package hu.logcontrol.mobilflexandroid.interfaces;

import hu.logcontrol.mobilflexandroid.models.MainWebAPIResponseObject;

public interface IMainWebAPIService {
    void onSuccesMainWebAPI(MainWebAPIResponseObject mainWebAPIResponseObject);
    void onFailureMainWebAPI(String errorMessage);
}
