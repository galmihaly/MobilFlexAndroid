package hu.logcontrol.mobilflexandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import hu.logcontrol.mobilflexandroid.adapters.ThemesSpinnerAdapter;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.helpers.StateChangeHelper;
import hu.logcontrol.mobilflexandroid.interfaces.IWebViewActivity;
import hu.logcontrol.mobilflexandroid.presenters.WebViewActivityPresenter;

public class WebViewActivity extends AppCompatActivity implements IWebViewActivity {

    private WebViewActivityPresenter webViewActivityPresenter;

    private WebView webView;
    private ImageButton logoutBut;
    private ConstraintLayout appBarCL;
    private Spinner themeSelector;

    private SwipeRefreshLayout swipeRefreshLayout;

    private int applicationId;
    private int isFromLoginPage;
    private int applicationsSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        initView();
        getDatasFromIntent();
        initPresenter();
        initAppDataManager();
        initWebAPIServices();
        initAppBarLayout();
        loadWebViewFromSettings();
        initSwipeRefreshLayout();
        initFunctions();
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* WebViewActivity fügvényei */

    private void initAppDataManager(){
        if(webViewActivityPresenter == null) return;
        webViewActivityPresenter.initAppDataManager();
    }

    private void initWebAPIServices() {
        if(webViewActivityPresenter == null) return;
        webViewActivityPresenter.initWebAPIServices();
    }

    private void getDatasFromIntent(){

        Intent i = getIntent();

        if(i != null){
            applicationId = i.getIntExtra("applicationId", -1);
            isFromLoginPage = i.getIntExtra("isFromLoginPage", -1);
            applicationsSize = i.getIntExtra("applicationsSize", -1);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebViewFromSettings() {
        if(webViewActivityPresenter == null) return;
        webViewActivityPresenter.getURLfromSettings(applicationId);
    }

    private void initFunctions() {
        if(logoutBut == null) return;

        logoutBut.setOnClickListener(v -> {
            if(isFromLoginPage == 0){
                if(applicationsSize == 1){
                    webViewActivityPresenter.openActivityByEnum(ViewEnums.MAIN_ACTIVITY, applicationId, applicationsSize);
                }
                else {
                    webViewActivityPresenter.openActivityByEnum(ViewEnums.PROGRAMS_ACTIVITY, applicationId, applicationsSize);
                }
            }
            else if(isFromLoginPage == 1) {
                webViewActivityPresenter.openActivityByEnum(ViewEnums.LOGIN_ACTIVITY, applicationId, applicationsSize);
            }
            else if(isFromLoginPage == 2) {
                webViewActivityPresenter.openActivityByEnum(ViewEnums.PROGRAMS_ACTIVITY, applicationId, applicationsSize);
            }
        });
    }

    private void initPresenter() {
        webViewActivityPresenter = new WebViewActivityPresenter(this, getApplicationContext());
    }

    private void initAppBarLayout() {
        if(webViewActivityPresenter == null) return;
        webViewActivityPresenter.getValuesFromSettingsPrefFile(applicationId);
        webViewActivityPresenter.getThemesSpinnerValues(applicationId);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView(){
        webView = findViewById(R.id.webView_portrait_mobile);
        logoutBut = findViewById(R.id.logoutBut_portrait_mobile);
        appBarCL = findViewById(R.id.appBarCL_portrait_mobile);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout_portrait_mobile);
        themeSelector = findViewById(R.id.themeSelector);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);
    }

    private void initSwipeRefreshLayout(){
        if(swipeRefreshLayout == null) return;
        if(webViewActivityPresenter == null) return;

        swipeRefreshLayout.setOnRefreshListener(() -> { webViewActivityPresenter.getURLfromSettings(applicationId); });
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* IWebViewActivity interfész függvényei */
    @Override
    public void openViewByIntent(Intent intent) { startActivity(intent); }

    @Override
    public void changeStateAppbarLayout(String backgroundColor, String backgroundGradientColor) {
        if(backgroundColor == null) return;
        if(backgroundGradientColor == null) return;
        if(appBarCL == null) return;

        int[] colors = {Color.parseColor(backgroundColor),Color.parseColor(backgroundGradientColor)};
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);

        appBarCL.setBackground(g);
    }

    @Override
    public void getMessageFromPresenter(String message) {
        if(message == null) return;
        new Handler(Looper.getMainLooper()).post(() -> { Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); });
    }

    @Override
    public void loadLoginWebAPIUrl(String loginWebAPIUrl) {
        if(loginWebAPIUrl == null) return;
        if(webViewActivityPresenter == null) return;
        if(swipeRefreshLayout == null) return;
        if(webView == null) return;

        webView.loadUrl(loginWebAPIUrl);
        if(swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void changeMobileBarsColors(String statusBarColor, String navigationBarColor){
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor(statusBarColor));
        window.setNavigationBarColor(Color.parseColor(navigationBarColor));
    }

    @Override
    public void changeStateLoginButton(String buttonBackgroundColor, String buttonBackgroundGradientColor) {
        if(buttonBackgroundColor == null) return;
        if(buttonBackgroundGradientColor == null) return;
        if(logoutBut == null) return;

        StateChangeHelper.changeStateButton(logoutBut, buttonBackgroundColor, buttonBackgroundGradientColor);
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */


    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* WebViewActivity Listeners */

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initThemesSpinner(ThemesSpinnerAdapter adapter, int currentThemeId, HashMap<Integer, Integer> vaulePairs) {
        if(adapter == null) return;
        if(currentThemeId == -1) return;
        if(themeSelector == null) return;
        if(vaulePairs == null) return;

        themeSelector.setAdapter(adapter);
        themeSelector.setSelection(currentThemeId);

        themeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (Map.Entry<Integer, Integer> entry : vaulePairs.entrySet()){
                    if(entry.getKey() == position){
                        webViewActivityPresenter.saveValueToCurrentThemeId(applicationId, (int)entry.getValue());
                        break;
                    }
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void hideThemesSpinner() {
        if(themeSelector == null) return;
        themeSelector.setVisibility(View.INVISIBLE);
    }
}