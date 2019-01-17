package com.bogdan.drown;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import restclient.DroneOrder;
import restclient.DroneOrderAction;
import restclient.DroneOrderActionPair;
import restclient.DroneOrderCreate;
import restclient.NetworkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderCreate.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderCreate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderCreate extends Fragment {
    public static final String TAG = "ORDER_CREATE_FRAGMENT";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_DEVICE_ID = "deviceId";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String deviceId;

    private FusedLocationProviderClient mFusedLocationClient;
    private Spinner mActionView;
    private View mCoordsView;
    private EditText mLatitudeView;
    private EditText mLongitudeView;
    private DroneOrderActionPair selectedAction;
    private String moveToLocation = DroneOrderAction.getActionName(DroneOrderAction.MOVE_TO_LOCATION);
    private Double latitude;
    private Double longitude;

    private OnFragmentInteractionListener mListener;

    public OrderCreate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DroneOrderCreate.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderCreate newInstance(String deviceId) {
        OrderCreate fragment = new OrderCreate();
        Bundle args = new Bundle();
        args.putString(ARG_DEVICE_ID, deviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceId = getArguments().getString(ARG_DEVICE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_create, container, false);
        mActionView = view.findViewById(R.id.order_create_action);
        final List<DroneOrderActionPair> pairs = DroneOrderAction.getPairs(true);
        ArrayAdapter<DroneOrderActionPair> arrayAdapter = new ArrayAdapter<>(
                this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                pairs
        );
        mActionView.setAdapter(arrayAdapter);
        mActionView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DroneOrderActionPair pair = pairs.get(position);
                if (pair != selectedAction) {
                    selectedAction = pair;
                    switchCoordsBoard();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int i = 0;
        while (i < pairs.size() && pairs.get(i).getName().equals(moveToLocation)) {
            i++;
        }
        mActionView.setSelection(i);

        mLatitudeView = view.findViewById(R.id.order_create_latitude);
        mLongitudeView = view.findViewById(R.id.order_create_longitude);

        mCoordsView = view.findViewById(R.id.order_action_coordinates);
        Button getCoords = view.findViewById(R.id.order_create_get_coordinates);
        getCoords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFusedLocationClient == null) {
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                ){//Can add more as per requirement

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                                    123);
                        }
                    }
                }
                try {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            mLatitudeView.setText(latitude.toString());
                            mLongitudeView.setText(longitude.toString());
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            failedToGetLocation();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            failedToGetLocation();
                        }
                    });
                } catch (SecurityException err) {
                    failedToGetLocation();
                }
            }
        });
        Button submit = view.findViewById(R.id.order_create_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });

        return view;
    }

    private void failedToGetLocation() {
        Toast.makeText(getActivity(), getActivity().getString(R.string.get_location_error), Toast.LENGTH_SHORT)
                .show();
    }

    public void switchCoordsBoard() {
        if (selectedAction != null && selectedAction.getName().equals(moveToLocation)) {
            mCoordsView.setVisibility(View.VISIBLE);
        } else {
            mCoordsView.setVisibility(View.GONE);
        }
    }

    public void sendOrder() {
        DroneOrderCreate order;
        if (selectedAction.getCode() == DroneOrderAction.MOVE_TO_LOCATION) {
            Activity activity = getActivity();
            boolean hasError = false;
            String latString = mLatitudeView.getText().toString();
            if (latString == null || latString.length() == 0) {
                hasError = true;
                mLatitudeView.setError(activity.getString(R.string.latitude_invalid));
            } else {
                latitude = Double.parseDouble(latString);
                if (latitude <= -90 || latitude >= 90) {
                    hasError = true;
                    mLatitudeView.setError(activity.getString(R.string.latitude_invalid));
                }
            }
            String lonString = mLongitudeView.getText().toString();
            if (lonString == null || lonString.length() == 0) {
                hasError = true;
                mLongitudeView.setError(activity.getString(R.string.longitude_invalid));
            } else {
                longitude = Double.parseDouble(lonString);
                if (longitude <= -180 || longitude >= 180) {
                    hasError = true;
                    mLongitudeView.setError(activity.getString(R.string.longitude_invalid));
                }
            }
            if (hasError) {
                return;
            }
            order = new DroneOrderCreate();
            order.setLatitude(latitude);
            order.setLongitude(longitude);
        } else {
            order = new DroneOrderCreate();
        }
        order.setDeviceId(deviceId);
        order.setAction(selectedAction.getCode());
        NetworkManager.getService().sendOrder(order).enqueue(new Callback<DroneOrder>() {
            @Override
            public void onResponse(Call<DroneOrder> call, Response<DroneOrder> response) {
                if (response.isSuccessful()) {
                    if (getActivity() instanceof MainActivity) {
                        MainActivity activity = (MainActivity)getActivity();
                        activity.replaceFragment(OrderFragment.newInstance(deviceId), OrderFragment.TAG);
                    } else {
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
                    }
                    return;
                }
                try {
                    Log.e(TAG, response.errorBody().string());
                } catch (IOException err) {
                    Log.e(TAG, "Error while getting error", err);
                }
                Activity activity = getActivity();
                Toast.makeText(activity, activity.getString(R.string.network_error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<DroneOrder> call, Throwable t) {
                Activity activity = getActivity();
                Toast.makeText(activity, activity.getString(R.string.network_error), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error while requesting", t);
            }
        });
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
