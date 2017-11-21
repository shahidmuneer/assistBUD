package org.medcada.android.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.medcada.android.R;
import org.medcada.android.db.User;
import org.medcada.android.object.Contacts;
import org.medcada.android.tools.Preferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shariq Khan on 6/7/2017.
 */

public class ContactsAdapter extends BaseAdapter {
    Context context;
    ArrayList<Contacts> list;

    public ContactsAdapter(Context context, ArrayList<Contacts> list) {
        this.context = context;
        this.list = list;
    }
    boolean allowDelete = false;
    public ContactsAdapter(Context context, ArrayList<Contacts> list,boolean allowDelete) {
        this.context = context;
        this.list = list;
        this.allowDelete = allowDelete;
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
        convertView = inflater.inflate(R.layout.lay_contact, null);
        ViewHolder holder = new ViewHolder(convertView);
        holder.tvName.setText(list.get(position).getName());
        holder.tvNumber.setText(list.get(position).getNumber());
        if (!allowDelete){
            holder.btn_delete.setVisibility(View.GONE);
        }
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("You sure want to delete this contact").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        notifyDataSetChanged();
                        new Preferences(context).setEmergencyContacts(list);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();

            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.btn_delete)
        ImageButton btn_delete;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
