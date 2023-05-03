package hu.logcontrol.mobilflexandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

    private Context context;
    private ProgramsPresenter programsPresenter;
    private List<ProgramsResultObject> programsResultObjectList;

    public ProgramsRecycleAdapter(Context context, ProgramsPresenter programsPresenter, List<ProgramsResultObject> programsResultObjectList) {
        this.context = context;
        this.programsPresenter = programsPresenter;
        this.programsResultObjectList = programsResultObjectList;
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

//            Bitmap logo = programsResultObjectList.get(position).getLogo();
            Drawable logo = programsResultObjectList.get(position).getLogo();
            if(logo != null) holder.getLogoItemIV().setImageDrawable(logo);

            holder.getApplicationTitleTV().setText(programsResultObjectList.get(position).getTitle());

            int[] colors = {
                    Color.parseColor(programsResultObjectList.get(position).getBackgroundColor()),
                    Color.parseColor(programsResultObjectList.get(position).getBackgroundGradientColor())
            };

            GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
            holder.getProgramsItemCL().setBackground(g);

            holder.getProgramsItemCL().setOnClickListener(v -> programsPresenter.openActivityByEnum(
                    ViewEnums.LOGIN_ACTIVITY,
                    programsResultObjectList.get(position).getDefaultThemeId(),
                    programsResultObjectList.get(position).getApplicationId()
            ));
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
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
