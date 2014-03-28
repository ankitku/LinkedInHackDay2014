package model;

import java.util.List;

import util.Utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.linker.R;

public class ListviewAdapter extends BaseAdapter {
	private Context context;

	private List<ListItemWithIcon> listItems;

	public ListviewAdapter(Context context, List<ListItemWithIcon> listItems) {
		this.context = context;
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ListItemWithIcon entry = listItems.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, null);
		}

		TextView nameText = (TextView) convertView.findViewById(R.id.titleText);
		nameText.setText(entry.getTitle());

		TextView subtitleText = (TextView) convertView
				.findViewById(R.id.subtitleText);
		if (!Utils.isEmpty(entry.getSubtitle())) {
			subtitleText.setText(entry.getSubtitle());
		} else
			subtitleText.setVisibility(View.GONE);

		ImageView picIcon = (ImageView) convertView.findViewById(R.id.pic);
		if (entry.getId() == 0)
			picIcon.setVisibility(View.GONE);
		else
			picIcon.setImageResource(entry.getId());

		return convertView;
	}

}