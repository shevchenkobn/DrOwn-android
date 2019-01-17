package com.bogdan.drown;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogdan.drown.OrderFragment.OnListFragmentInteractionListener;
import com.bogdan.drown.dummy.DummyContent.DummyItem;

import java.util.List;

import restclient.DroneOrder;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {

    private final List<DroneOrder> mValues;
    private final OnListFragmentInteractionListener mListener;

    public OrderRecyclerViewAdapter(List<DroneOrder> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String action = holder.mItem.getActionName();
        holder.mActionView.setText(action);
        holder.mStatusView.setText(holder.mItem.getStatusName());
        Double latitude = holder.mItem.getLatitude();
        if (latitude != null) {
            holder.mLatitudeView.setText("x = " + latitude.toString());
        } else {
            holder.mLatitudeView.setText("");
        }
        Double longitude = holder.mItem.getLongitude();
        if (longitude != null) {
            holder.mLongitudeView.setText("y = " + longitude.toString());
        } else {
            holder.mLongitudeView.setText("");
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mActionView;
        public final TextView mStatusView;
        public final TextView mLatitudeView;
        public final TextView mLongitudeView;
        public DroneOrder mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mActionView = (TextView) view.findViewById(R.id.order_action);
            mStatusView = (TextView) view.findViewById(R.id.order_status);
            mLatitudeView = (TextView) view.findViewById(R.id.order_latitude);
            mLongitudeView = (TextView) view.findViewById(R.id.order_longitude);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mStatusView.getText() + "'";
        }
    }
}
