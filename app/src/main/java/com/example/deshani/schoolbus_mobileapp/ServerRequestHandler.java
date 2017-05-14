package com.example.deshani.schoolbus_mobileapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//import com.example.vollylibrary.vollylibrary.AuthFailureError;
//import com.example.vollylibrary.DefaultRetryPolicy;
//import com.example.vollylibrary.Request;
//import com.example.vollylibrary.RequestQueue;
//import com.example.vollylibrary.Response;
//import com.example.vollylibrary.RetryPolicy;
//import com.example.vollylibrary.VolleyError;
//import com.example.vollylibrary.toolbox.StringRequest;
//import com.example.vollylibrary.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by deshani on 5/11/17.
 */
public class ServerRequestsHandler {

    private final String server_address = "http://schoolbus-team-inlaws.herokuapp.com/";

    public final static String EXTRA_RESPONSE = "team.inlaws.schoolbus.RESPONSE";
    public final static String EXTRA_WAYPOINTS = "team.inlaws.schoolbus.WAYPOINTS";
    public final static String EXTRA_TARGET = "team.inlaws.schoolbus.TARGET";
    public final static String EXTRA_START = "team.inlaws.schoolbus.START";
    public final static String EXTRA_MY_VANS_RESPONSE = "team.inlaws.schoolbus.MY_VANS_RESPONSE";
    public final static String EXTRA_TYPE = "team.inlaws.schoolbus.TYPE";
    public final static String EXTRA_SCHOOL_LOC = "team.inlaws.schoolbus.SCHOOL_LOC";
    public final static String EXTRA_MY_VAN = "team.inlaws.schoolbus.MY_VAN";

    public final static String EXTRA_CHILD_FIRST_NAME = "team.inlaws.schoolbus.CHILD_FIRST_NAME";
    public final static String EXTRA_CHILD_LAST_NAME = "team.inlaws.schoolbus.CHILD_LAST_NAME";
    public final static String EXTRA_CHILD_LOCATION = "team.inlaws.schoolbus.CHILD_LOCATION";
    public final static String EXTRA_CHILD_TARGET = "team.inlaws.schoolbus.CHILD_TARGET";

    public final static String EXTRA_PARENT_FIRST_NAME = "team.inlaws.schoolbus.PARENT_FIRST_NAME";
    public final static String EXTRA_PARENT_LAST_NAME = "team.inlaws.schoolbus.PARENT_LAST_NAME";
    public final static String EXTRA_PARENT_MOBILE = "team.inlaws.schoolbus.PARENT_MOBILE";

    private static ServerRequestsHandler serverRequestsHandler;

    private ServerRequestsHandler() {

    }

    public static ServerRequestsHandler getServerRequestsHandler() {
        if (serverRequestsHandler == null) {
            serverRequestsHandler = new ServerRequestsHandler();
        }
        return serverRequestsHandler;
    }

//    //driver token login
//    public synchronized void driverTokenLogin(final String nic, final String token, final View view) {
//        String url = server_address + "driver-token-login?nic=" + nic + "&token=" + token;
//
//        RequestQueue queue = Volley.newRequestQueue(view.getContext());
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                //makeText(view.getContext(), response, //LENGTH_LONG).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //makeText(view.getContext(), error.toString(), //LENGTH_LONG).show();
//            }
//        });
//        queue.add(stringRequest);
//    }

    //driver normal login
    public synchronized void driverLogin(final String nic, final String password, final View view) {
        String url = server_address + "driver-login?nic=" + nic + "&password=" + password;

        RequestQueue queue = .newRequestQueue(view.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean isInserted = false;
                if (response != null) {
                    try {
                        JSONObject driver = new JSONObject(response);
                        DatabaseHelper db;
                        db = new DatabaseHelper(view.getContext());
                        db.getReadableDatabase().execSQL("delete from " + SchoolBusContract.DriverEntry.TABLE_NAME);
                        isInserted = db.insertToDrivers(driver.getString("token"), driver.getInt("id"), driver.getString("first_name"), driver.getString("last_name"), driver.getString("nic"), driver.getString("mobile"), driver.getString("plate_number"), driver.getString("licence_number"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (isInserted) {
                        Intent viewDriver = new Intent(view.getContext(), DriverMainActivity.class);
                        view.getContext().startActivity(viewDriver);
                    } else {
                        //makeText(view.getContext(), "Login Failed! Please check your connection", //LENGTH_LONG).show();

                    }
                } else {
                    DatabaseHelper db;
                    db = new DatabaseHelper(view.getContext());
                    db.getReadableDatabase().execSQL("delete from " + SchoolBusContract.GuardianEntry.TABLE_NAME);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //makeText(view.getContext(), error.toString(), //LENGTH_LONG).show();
            }
        });
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

//    //guardian token login
//    public synchronized void guardianTokenLogin(final String nic, final String token, final View view) {
//        String url = server_address + "guardian-token-login?nic=" + nic + "&token=" + token;
//
//        RequestQueue queue = Volley.newRequestQueue(view.getContext());
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                //makeText(view.getContext(), response, //LENGTH_LONG).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //makeText(view.getContext(), error.toString(), //LENGTH_LONG).show();
//            }
//        });
//        queue.add(stringRequest);
//    }

    //driver normal login
    public synchronized void guardianLogin(final String nic, final String password, final View view) {
        String url = server_address + "guardian-login?nic=" + nic + "&password=" + password;

        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                boolean isInserted = false;
                if (response != null) {
                    try {
                        JSONObject guardian = new JSONObject(response);
                        DatabaseHelper db;
                        db = new DatabaseHelper(view.getContext());
                        db.getReadableDatabase().execSQL("delete from " + SchoolBusContract.GuardianEntry.TABLE_NAME);
                        isInserted = db.insertToGuardians(guardian.getString("token"), guardian.getInt("id"), guardian.getString("first_name"), guardian.getString("last_name"), guardian.getString("nic"), guardian.getString("mobile"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (isInserted) {
                        Intent viewGuardian = new Intent(view.getContext(), ParentMainActivity.class);
                        view.getContext().startActivity(viewGuardian);
                    } else {
                        //makeText(view.getContext(), "Login Failed! Please check your connection", //LENGTH_LONG).show();
                    }
                } else {
                    DatabaseHelper db;
                    db = new DatabaseHelper(view.getContext());
                    db.getReadableDatabase().execSQL("delete from " + SchoolBusContract.DriverEntry.TABLE_NAME);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //makeText(view.getContext(), "Please check your connection", //LENGTH_LONG).show();
            }
        });
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

    public synchronized void getStudentProfile(final String first_name, final String last_name, final Integer school_id, final String location, final Integer guardian_id, final View view) {
        String url = server_address + "get-school_location?id=" + school_id;

        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                getOtherStudentDetails(first_name, last_name, location, response, guardian_id, view);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //makeText(view.getContext(), "Please check your connection", //LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

}