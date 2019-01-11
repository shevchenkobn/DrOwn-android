package com.bogdan.drown;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogdan.drown.TelemetryFragment.OnListFragmentInteractionListener;
import com.bogdan.drown.dummy.DummyContent.DummyItem;

import java.util.List;

import restclient.Telemetry;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TelemetryRecyclerViewAdapter extends RecyclerView.Adapter<TelemetryRecyclerViewAdapter.ViewHolder> {

    private final List<Telemetry> mValues;
    private final OnListFragmentInteractionListener mListener;

    public TelemetryRecyclerViewAdapter(List<Telemetry> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_telemetry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mStatusView.setText(mValues.get(position).getStatusName());
        holder.mBatteryChargeView.setText(mValues.get(position).getBatteryCharge().toString() + " %");
        holder.mLatitudeView.setText("x: " + mValues.get(position).getLatitude().toString());
        holder.mLongitudeView.setText("y: " + mValues.get(position).getLongitude().toString());
        holder.mCreatedAtView.setText(mValues.get(position).getCreatedAt().toLocaleString());

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
        public final TextView mBatteryChargeView;
        public final TextView mStatusView;
        public final TextView mLongitudeView;
        public final TextView mLatitudeView;
        public final TextView mCreatedAtView;
        public Telemetry mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBatteryChargeView = (TextView) view.findViewById(R.id.telemetry_battery_charge);
            mStatusView = (TextView) view.findViewById(R.id.telemetry_status);
            mLongitudeView = (TextView) view.findViewById(R.id.telemetry_longitude);
            mLatitudeView = (TextView) view.findViewById(R.id.telemetry_latitude);
            mCreatedAtView = (TextView) view.findViewById(R.id.telemetry_created_at);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mStatusView.getText() + "'";
        }
    }
}
