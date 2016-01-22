package com.example.administrator.bazipaipan.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.bazipaipan.R;
import com.example.administrator.bazipaipan.entity.address.CityByProModel;
import com.example.administrator.bazipaipan.entity.address.ProvinceModel;
import com.example.administrator.bazipaipan.entity.address.ZoneByCityModel;


public class ReceiveSiteSelectListAdapter extends BaseAdapter {
	private List<Object> list;
	private LayoutInflater inflater;
	private int flag;

	public ReceiveSiteSelectListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	public ReceiveSiteSelectListAdapter(Context context, List<Object> list,
			int flag) {
		this(context);
		this.list = list;
		this.flag = flag;
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater
					.inflate(
							R.layout.receive_site_add_addrselect_list_item_layout,
							null);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_receive_site_add_list_item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (flag == 1) {
			ProvinceModel model = (ProvinceModel) list.get(position);
			holder.tv_name.setText(model.getProName());
		} else if (flag == 2) {
			CityByProModel model = (CityByProModel) list.get(position);
			holder.tv_name.setText(model.getCityName());
		} else {
			ZoneByCityModel model = (ZoneByCityModel) list.get(position);
			holder.tv_name.setText(model.getZoneName());
		}
		return convertView;
	}

	private class ViewHolder {
		private TextView tv_name;
	}

	public void setNewData(int flag, List<Object> list) {
		this.flag = flag;
		this.list = list;
	}
}
