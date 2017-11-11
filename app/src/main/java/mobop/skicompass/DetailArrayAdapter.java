package mobop.skicompass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by artanpapaj on 09.11.17.
 */

public class DetailArrayAdapter extends ArrayAdapter<DetailRowData> {

    private final Context context;
    private final DetailRowData[] rowData;

    public DetailArrayAdapter(Context context, DetailRowData[] rowData) {
        super(context, R.layout.detail_rowlayout, rowData);
        this.context = context;
        this.rowData = rowData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = vi.inflate(R.layout.detail_rowlayout, parent, false);
        }

        if (rowData[position] != null){
            TextView listDescription = rowView.findViewById(R.id.listDescription);
            TextView listValue = rowView.findViewById(R.id.listValue);

            if (listDescription != null) {
                listDescription.setText(rowData[position].description);
            }

            if (listValue != null) {
                listValue.setText(rowData[position].value);
            }
        }

        return rowView;
    }

}
