package hu.logcontrol.mobilflexandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import hu.logcontrol.mobilflexandroid.adapters.ProgramsRecycleAdapter;
import hu.logcontrol.mobilflexandroid.interfaces.IProgramsActivity;
import hu.logcontrol.mobilflexandroid.models.ProgramsResultObject;
import hu.logcontrol.mobilflexandroid.presenters.ProgramsPresenter;

public class ProgramsActivity extends AppCompatActivity implements IProgramsActivity {

    private RecyclerView programsRV;
    private ProgramsPresenter programsPresenter;
    private int applicationNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);

        initView();
    }

    void initView(){
        programsRV = findViewById(R.id.programsRV);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDatasFromIntent();
        initPresenter();
        initAppDataManager();
        downloadSVGLogoFromWeb();
    }

    private void initPresenter(){
        programsPresenter = new ProgramsPresenter(this, getApplicationContext());
    }

    private void initAppDataManager(){
        if(programsPresenter == null) return;
        programsPresenter.initAppDataManager();
    }

    private void downloadSVGLogoFromWeb() {
        if(programsPresenter == null) return;
        programsPresenter.getDownloadedLogoFromWeb(applicationNumber);
    }

    private void getDatasFromIntent(){

        Intent i = getIntent();

        if(i != null){
            applicationNumber = getIntent().getIntExtra("applicationsSize", -1);
        }
    }

    /* ---------------------------------------------------------------------------------------------------------------------------------------------------------- */
    /* ILoginActivity interfész függvényei */
    @Override
    public void openViewByIntent(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void getMessageFromPresenter(String message) {
        if(message == null) return;
        new Handler(Looper.getMainLooper()).post(() -> { Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); });
    }

    @Override
    public void getObjectList(List<ProgramsResultObject> programsResultObjectList){
        if(programsResultObjectList == null) return;
        if(programsRV == null) return;
        if(programsPresenter == null) return;

        ProgramsRecycleAdapter adapter = new ProgramsRecycleAdapter(getApplicationContext(), programsPresenter, programsResultObjectList);
        programsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        programsRV.setAdapter(adapter);
        adapter.refreshAdapterData();
    }
}