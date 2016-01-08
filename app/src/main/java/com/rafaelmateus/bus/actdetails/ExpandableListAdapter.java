package com.rafaelmateus.bus.actdetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.rafaelmateus.bus.R;
import com.rafaelmateus.bus.models.Hour;

class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final Hour[] arrayHours;

    public ExpandableListAdapter(Hour[] arrayHours) {
        this.arrayHours = arrayHours;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.arrayHours[groupPosition].getListDeparture().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_departure_child, null);
        }

        final String children = (String) this.getChild(groupPosition, childPosition);
        TextView textView = (TextView) convertView.findViewById(R.id.ListViewDepartureChild_Text1);
        textView.setText(children);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.arrayHours[groupPosition].getListDeparture().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.arrayHours[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return this.arrayHours.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_departure_group, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.ListViewDepartureGroup_Text1);
        textView.setText(this.arrayHours[groupPosition].getHour() + " Hours (" + this.arrayHours[groupPosition].getListDeparture().size() + " Departures)");

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
