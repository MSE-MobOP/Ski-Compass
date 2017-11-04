package mobop.skicompass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mobop.skicompass.dataarchitecture.SkiResort;

/**
 * Created by Martin on 03.11.2017.
 */

public class SkiResortArrayAdapter extends ArrayAdapter<SkiResort> {
    private final List<SkiResort> skiResortList;
    private final Context context;

    public SkiResortArrayAdapter(Context context, List<SkiResort> objects) {
        super(context, R.layout.list_entry, objects);
        this.skiResortList = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.list_entry, parent, false);
        }
        SkiResort resort = skiResortList.get(position);
        if (resort != null) {
            TextView name = rowView.findViewById(R.id.Name);
            ImageView status = rowView.findViewById(R.id.Status);

            if (name != null) {
                name.setText(resort.getName());
            }

            // Set image to OPERATING Status
            if (status != null) {
                switch (resort.getOperatingStatus()) {
                    case Operating:
                        status.setImageResource(R.drawable.open);
                        break;
                    case Closed:
                        status.setImageResource(R.drawable.closed);
                        break;
                    case Unknown:
                        status.setImageResource(R.drawable.unknown);
                        break;
                    default:
                        status.setImageResource(R.drawable.unknown);
                }
            }
        }

        return rowView;
    }
}
