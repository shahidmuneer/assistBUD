package org.medcada.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.medcada.android.R;
import org.medcada.android.activity.AddMedicationActivity;
import org.medcada.android.db.MedicationBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shariq Khan on 6/2/2017.
 */

public class MedicationAdapter extends BaseAdapter {
    Context context;
    ArrayList<MedicationBean> list;

    public MedicationAdapter(Context context, ArrayList<MedicationBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.lay_medication_item, null);
        ViewHolder holder = new ViewHolder(convertView);
        MedicationBean bean = list.get(position);
        holder.tvMedicName.setText(bean.getMedicationName());
        if (bean.isAsNeeded()){
            holder.tvMedicTime.setText("Take as needed");
        }else{
            holder.tvMedicTime.setText(bean.getInterval() + " times " + bean.getRepeat());
        }

        holder.ivColor.setColorFilter(bean.getColor());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddMedicationActivity.class);
                intent.putExtra("isForUpdate", true);
                intent.putExtra("id", list.get(position).getId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.iv_color)
        ImageView ivColor;
        @BindView(R.id.tv_medicName)
        TextView tvMedicName;
        @BindView(R.id.tv_medic_time)
        TextView tvMedicTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
