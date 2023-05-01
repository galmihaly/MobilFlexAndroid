package hu.logcontrol.mobilflexandroid.adapters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.logcontrol.mobilflexandroid.ProgramsActivity;
import hu.logcontrol.mobilflexandroid.R;
import hu.logcontrol.mobilflexandroid.enums.ViewEnums;
import hu.logcontrol.mobilflexandroid.models.ProgramsResultObject;
import hu.logcontrol.mobilflexandroid.presenters.ProgramsPresenter;

public class ProgramsRecycleAdapter extends RecyclerView.Adapter<ProgramsRecycleAdapter.ProgramItemViewHolder> {

    private ProgramsPresenter programsPresenter;
    private List<ProgramsResultObject> programsResultObjectList;

    public ProgramsRecycleAdapter(List<ProgramsResultObject> programsResultObjectList, ProgramsPresenter programsPresenter) {
        this.programsResultObjectList = programsResultObjectList;
        this.programsPresenter = programsPresenter;
    }

    @NonNull
    @Override
    public ProgramItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_item_layout, parent, false);
        return new ProgramItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramItemViewHolder holder, int position) {
        if(programsResultObjectList != null){
            if(programsResultObjectList.size() != 0){
                if(holder.getAdapterPosition() != RecyclerView.NO_POSITION){

                    Log.e("beléptem ide", "ok");

                    Bitmap logo = programsResultObjectList.get(position).getLogos();
                    if(logo != null) holder.getLogoItemIV().setImageBitmap(programsResultObjectList.get(position).getLogos());

                    holder.getApplicationTitleTV().setText(programsResultObjectList.get(position).getTitles());

                    int[] colors = {
                            Color.parseColor(programsResultObjectList.get(position).getBackgroundColors()),
                            Color.parseColor(programsResultObjectList.get(position).getBackgroundGradientColors())
                    };

                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
                    holder.getProgramsItemCL().setBackground(g);

                    holder.getProgramsItemCL().setOnClickListener(v -> programsPresenter.openActivityByEnum(
                            ViewEnums.LOGIN_ACTIVITY,
                            programsResultObjectList.get(position).getDefaultThemeId(),
                            position + 1
                    ));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return programsResultObjectList.size();
    }

    public static class ProgramItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView logoItemIV;
        private TextView applicationTitleTV;
        private ConstraintLayout programsItemCL;

        public ProgramItemViewHolder(@NonNull View itemView) {
            super(itemView);

            logoItemIV = itemView.findViewById(R.id.logoItemIV);
            applicationTitleTV = itemView.findViewById(R.id.applicationTitleTV);
            programsItemCL = itemView.findViewById(R.id.programsItemCL);
        }

        public ImageView getLogoItemIV() {
            return logoItemIV;
        }

        public TextView getApplicationTitleTV() {
            return applicationTitleTV;
        }

        public ConstraintLayout getProgramsItemCL() {
            return programsItemCL;
        }
    }
}
