package hu.logcontrol.mobilflexandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hu.logcontrol.mobilflexandroid.R;

public class ThemesSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<String> themesNames;

    public ThemesSpinnerAdapter(Context context, List<String> themesNames) {
        this.context = context;
        this.themesNames = themesNames;
    }

    @Override
    public int getCount() {
        if(themesNames == null) return -1;
        return themesNames.size();
    }

    @Override
    public Object getItem(int position) {
        if(themesNames == null) return -1;
        return themesNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.themes_text, parent, false);

        TextView textView = rootView.findViewById(R.id.themesTextItem);
        textView.setText(this.themesNames.get(position));
        return rootView;
    }
}
