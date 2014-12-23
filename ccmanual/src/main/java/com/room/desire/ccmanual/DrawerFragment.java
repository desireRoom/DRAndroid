package com.room.desire.ccmanual;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * Created by desire on 14/12/15.
 */
public class DrawerFragment extends Fragment {

    private OnDrawerChildSelected mListener;

    private ExpandableListView mList;
    private DrawerAdapter mAdapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnDrawerChildSelected) {
            mListener = (OnDrawerChildSelected) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        mList = (ExpandableListView) view.findViewById(R.id.drawer_expand_list);
        mAdapter = new DrawerAdapter(getActivity());
        mList.setAdapter(mAdapter);
        mList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (mListener != null) {
                    mListener.onDrawerChildSelected(groupPosition, childPosition);
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    private static class DrawerAdapter implements ExpandableListAdapter {
        private Context dContext;
        private String[] dGroups;
        private String[][] dChildren = new String[6][];

        private DrawerAdapter(Context context) {
            Resources res = context.getResources();
            dGroups = res.getStringArray(R.array.ccm_six_tech_strs);
            dChildren[0] = res.getStringArray(R.array.ccm_push_up_strs);
            dChildren[1] = res.getStringArray(R.array.ccm_leg_raise_strs);
            dChildren[2] = res.getStringArray(R.array.ccm_pull_up_strs);
            dChildren[3] = res.getStringArray(R.array.ccm_deep_aquat_strs);
            dChildren[4] = res.getStringArray(R.array.ccm_on_hold_strs);
            dChildren[5] = res.getStringArray(R.array.ccm_bridge_strs);
            dContext = context;
        }

        @Override
        public int getGroupCount() {
            return dGroups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return dChildren[groupPosition].length;
        }

        @Override
        public String getGroup(int groupPosition) {
            return dGroups[groupPosition];
        }

        @Override
        public String getChild(int groupPosition, int childPosition) {
            return dChildren[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition * 100 + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(dContext, R.layout.drawer_group_item, null);
            } else {
                view = convertView;
            }

            TextView tv = (TextView) view.findViewById(R.id.drawer_group_item_tv);
            tv.setText(getGroup(groupPosition));
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(dContext, R.layout.drawer_child_item, null);
            } else {
                view = convertView;
            }

            TextView tv = (TextView) view.findViewById(R.id.drawer_child_item_tv);
            tv.setText(getChild(groupPosition, childPosition));
            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }
    }

    public interface OnDrawerChildSelected {
        public void onDrawerChildSelected(int groupId, int childId);

    }
}
