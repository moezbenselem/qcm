package com.qcm.moez.qcm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;

/**
 * Created by Moez on 10/11/2017.
 */

public class CustumLvAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustumLvAdapter(Context context, int resourceId,
                           List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        CheckBox checkBox;
        EditText editText;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        try {

            ViewHolder holder = null;
            RowItem rowItem = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.listview_sugg, null);
                holder = new ViewHolder();
                holder.editText = (EditText) convertView.findViewById(R.id.etSugg);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkSugg);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.editText.setText("");
            holder.editText.setHint("Votre suggestion");
            //holder.editText.setText(rowItem.editText.getText().toString());
            //holder.imageView.setImageResource(rowItem.getImageId());
            //holder.checkBox = rowItem.checkBox;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}