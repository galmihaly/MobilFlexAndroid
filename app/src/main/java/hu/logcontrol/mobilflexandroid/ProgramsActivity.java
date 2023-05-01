package hu.logcontrol.mobilflexandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import hu.logcontrol.mobilflexandroid.adapters.ProgramsRecycleAdapter;
import hu.logcontrol.mobilflexandroid.interfaces.IProgramsActivity;
import hu.logcontrol.mobilflexandroid.models.ProgramsResultObject;
import hu.logcontrol.mobilflexandroid.presenters.ProgramsPresenter;

public class ProgramsActivity extends AppCompatActivity implements IProgramsActivity {

    private RecyclerView programsRV;
    private ProgramsPresenter programsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
        initView();
        initPresenter();
        initAppDataManager();
        initRelativeLayoutElementsData();
    }

    void initView(){
        programsRV = findViewById(R.id.programsRV);
    }

    private void initPresenter(){
        programsPresenter = new ProgramsPresenter(this, getApplicationContext());
    }

    private void initAppDataManager(){
        if(programsPresenter == null) return;
        programsPresenter.initAppDataManager();
    }

    private void initRelativeLayoutElementsData() {
        if(programsPresenter == null) return;
        programsPresenter.getDataFromAppDataManager(getIntent());
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
    public void getProgramsCardElements(List<ProgramsResultObject> programsResultObjects){
        if(programsResultObjects == null) return;
        if(programsRV == null) return;
        if(programsPresenter == null) return;

        ProgramsRecycleAdapter adapter = new ProgramsRecycleAdapter(programsResultObjects, programsPresenter);
        programsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        programsRV.setAdapter(adapter);
    }
}