package hu.logcontrol.mobilflexandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import hu.logcontrol.mobilflexandroid.R;

public class LanguagesSpinnerAdapter extends BaseAdapter {

    private Context context;
    private int[] languageImages;

    public LanguagesSpinnerAdapter(Context context, int[] languageImages) {
        this.context = context;
        this.languageImages = languageImages;
    }

    @Override
    public int getCount() {
        if(languageImages == null) return -1;
        return languageImages.length;
    }

    @Override
    public Object getItem(int position) {
        return languageImages[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.language_icon, parent, false);

        ImageView languageIcon = rootView.findViewById(R.id.language_icon);
        languageIcon.setImageResource(this.languageImages[position]);
        return rootView;
    }
}
