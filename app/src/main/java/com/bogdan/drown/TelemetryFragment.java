package com.bogdan.drown;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import restclient.NetworkManager;
import restclient.Telemetry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TelemetryFragment extends Fragment {
    public static final String TAG = "TELEMETRY_FRAGMENT";

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_DEVICE_ID = "deviceId";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private String deviceId;
    private List<Telemetry> telemetryList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TelemetryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TelemetryFragment newInstance(String deviceId) {
        TelemetryFragment fragment = new TelemetryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_ID, deviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            deviceId = getArguments().getString(ARG_DEVICE_ID);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        NetworkManager.getMeasurements(deviceId).enqueue(new Callback<List<Telemetry>>() {
            @Override
            public void onResponse(Call<List<Telemetry>> call, Response<List<Telemetry>> response) {
                telemetryList = response.body();
                updateView(getView());
            }

            @Override
            public void onFailure(Call<List<Telemetry>> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_telemetry_list, container, false);

        // Set the adapter
        updateView(view);
        return view;
    }

    private void updateView(View view) {
        if (view == null) {
            return;
        }
        view = view.findViewById(R.id.telemetry_list);
        if (view instanceof RecyclerView && telemetryList != null) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new TelemetryRecyclerViewAdapter(telemetryList, mListener));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Telemetry item);
    }
}
