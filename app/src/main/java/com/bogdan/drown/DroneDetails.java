package com.bogdan.drown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import restclient.Drone;
import restclient.NetworkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DroneDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DroneDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DroneDetails extends Fragment {
    public static final String TAG_FRAGMENT = "DRONE_DETAILS_FRAGMENT";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DRONE_ID = "droneId";
    private static final String ARG_DRONE = "droneId";

    // TODO: Rename and change types of parameters
    private String mDroneId;
    private Drone drone;

    private OnFragmentInteractionListener mListener;

    public DroneDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DroneDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static DroneDetails newInstance(String param1) {
        DroneDetails fragment = new DroneDetails();
        Bundle args = new Bundle();
        args.putString(ARG_DRONE_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException("No arguments");
        }
        mDroneId = getArguments().getString(ARG_DRONE_ID);
    }

    @Override
    public void onStart() {
        super.onStart();
        NetworkManager.getService().getDrone(mDroneId).enqueue(new Callback<Drone>() {
            @Override
            public void onResponse(Call<Drone> call, Response<Drone> response) {
                drone = response.body();
                updateView();
            }

            @Override
            public void onFailure(Call<Drone> call, Throwable t) {

            }
        });
    }

    private void updateView() {
        View view = getView();
        ((TextView) view.findViewById(R.id.drone_details_device_id)).setText(drone.getDeviceId());
        ((TextView) view.findViewById(R.id.drone_details_status)).setText(drone.getStatusName());
        ((TextView) view.findViewById(R.id.drone_details_base_latitude)).setText(drone.getBaseLatitude().toString());
        ((TextView) view.findViewById(R.id.drone_details_base_longitude)).setText(drone.getBaseLongitude().toString());
        ((TextView) view.findViewById(R.id.drone_details_battery_power)).setText(drone.getBatteryPower().toString());
        ((TextView) view.findViewById(R.id.drone_details_engine_power)).setText(drone.getEnginePower().toString());
        ((TextView) view.findViewById(R.id.drone_details_load_capacity)).setText(drone.getLoadCapacity().toString());
        String liquidsMessage = getString(drone.canCarryLiquids() ? R.string.drone_can_carry_liquids : R.string.drone_cannot_carry_liquids);
        ((TextView) view.findViewById(R.id.drone_details_can_carry_liquids)).setText(liquidsMessage);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_drone_details, container, false);

        Button deleteBtn = view.findViewById(R.id.drone_details_delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.prompt_drone_delete)
                        .setPositiveButton(R.string.prompt_drone_delete_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NetworkManager.getService().deleteDrone(drone.getDroneId()).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                                Log.w(TAG_FRAGMENT, "deleted");
                                getFragmentManager().popBackStackImmediate();
                            }
                        })
                        .setNegativeButton(R.string.prompt_drone_delete_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });
        Button telemetryBtn = view.findViewById(R.id.drone_details_telemetry);
        telemetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity == null) {
                    Log.e(TAG_FRAGMENT, "mainActivity is null");
                    return;
                }
                mainActivity.replaceFragment(TelemetryFragment.newInstance(drone.getDeviceId()), TelemetryFragment.TAG);
            }
        });
        Button ordersBtn = view.findViewById(R.id.drone_details_orders);
        ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity == null) {
                    Log.e(TAG_FRAGMENT, "mainActivity is null");
                    return;
                }
                mainActivity.replaceFragment(OrderFragment.newInstance(drone.getDeviceId()), OrderFragment.TAG);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
