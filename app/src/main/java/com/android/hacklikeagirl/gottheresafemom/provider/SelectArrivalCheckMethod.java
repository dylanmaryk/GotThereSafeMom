package com.android.hacklikeagirl.gottheresafemom.provider;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.hacklikeagirl.gottheresafemom.ContactSelectionActivity;
import com.android.hacklikeagirl.gottheresafemom.MainActivity;
import com.android.hacklikeagirl.gottheresafemom.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import static com.android.hacklikeagirl.gottheresafemom.MainActivity.PLACE_PICKER_REQUEST;

public class SelectArrivalCheckMethod extends AppCompatActivity {

    private boolean chosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_arrival_check_method);

        final Button buttonDetermineByTime = (Button) findViewById(R.id.button_determine_by_time);
        buttonDetermineByTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onButtonDetermineByDateClick(v);
            }
        });
        final Button buttonDetermineByFlightNumber = (Button) findViewById(R.id.button_determine_by_flightnr);
        buttonDetermineByFlightNumber.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onButtonDetermineByFlightNumberClick(v);
            }
        });
        final Button buttonDetermineByLocation = (Button) findViewById(R.id.button_determine_by_location);
        buttonDetermineByLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddPlaceButtonClicked(v);
            }
        });

    }

    /***
     * Button Click event handler to handle clicking the "Add new location" Button
     *
     * @param view
     */
    public void onAddPlaceButtonClicked(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.need_location_permission_message), Toast.LENGTH_LONG).show();
            return;
        }
        try {
            // Start a new Activity for the Place Picker API, this will trigger {@code #onActivityResult}
            // when a place is selected or with the user cancels.
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(this);
            Intent ii = new Intent(SelectArrivalCheckMethod.this, MainActivity.class);
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(MainActivity.class.getSimpleName(), String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(MainActivity.class.getSimpleName(), String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(MainActivity.class.getSimpleName(), String.format("PlacePicker Exception: %s", e.getMessage()));
        }
    }

    public void onButtonDetermineByDateClick(View view) {

        // get a reference to the already created main layout
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.activity_select_arrival_check_method);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pick_time_layout, null);
        final TimePicker timePicker1 = (TimePicker) popupView.findViewById(R.id.timePicker1);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        Button setTimeButton = (Button) popupView.findViewById(R.id.button_select_time);
        setTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              int chosenHour = timePicker1.getHour();
              int chosenMinute = timePicker1.getMinute();
              NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
              builder.setContentTitle("My notification");
              builder.setContentText("Hello World!");
              builder.setSmallIcon(R.drawable.ic_globe_primary_24dp);
              Intent intent = new Intent(getApplicationContext(), SelectArrivalCheckMethod.class);
              PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
              builder.setContentIntent(pendingIntent);
              Notification notificationCompat = builder.build();
              NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
              managerCompat.notify(0, notificationCompat);
              popupWindow.dismiss();
            }
        });


    }

    public void onButtonDetermineByFlightNumberClick(View view) {
        // get a reference to the already created main layout
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.activity_select_arrival_check_method);

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.enter_flight_number, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        final DatePicker datePicker = (DatePicker) popupView.findViewById(R.id.flight_date_picker);
        final EditText flightNumber = (EditText) popupView.findViewById(R.id.flight_number);
        Button saveFlight = (Button) popupView.findViewById(R.id.button_save_the_flight);
        saveFlight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePicker.getDayOfMonth();
                popupWindow.dismiss();
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                Intent ii = new Intent(SelectArrivalCheckMethod.this, ContactSelectionActivity.class);
                SelectArrivalCheckMethod.this.startActivity(ii);
            }
        }
    }
}
