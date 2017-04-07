package itesm.mx.perritos.event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import itesm.mx.perritos.R;

public class AddEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener, TimePickerDialog.OnTimeSetListener  {

    private TextView textStartDate;
    private TextView textEndDate;

    private Toolbar tlToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        textStartDate = (TextView) findViewById(R.id.text_startTime);
        textEndDate = (TextView) findViewById(R.id.text_endTime);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textStartDate.setOnClickListener(this);
        textEndDate.setOnClickListener(this);
        getSupportActionBar().setTitle("Nuevo Evento");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confirm,menu);
        return true;
    }

    private void showDatePickerDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = new DatePickerFragment(AddEventActivity.this);
        newFragment.show(ft, "date_dialog");
    }

    private void showTimePickerDialog() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = new TimePickerFragment(AddEventActivity.this);
        newFragment.show(fragmentTransaction,"time_dialog");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_startTime:
                showDatePickerDialog();
                break;
            case R.id.text_endTime:
                showTimePickerDialog();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }

    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener mDateSetListener;

        public DatePickerFragment() {

        }

        @SuppressLint("ValidFragment")
        public DatePickerFragment(DatePickerDialog.OnDateSetListener callback) {
            mDateSetListener = (DatePickerDialog.OnDateSetListener) callback;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Calendar cal = Calendar.getInstance();

            return new DatePickerDialog(getActivity(),
                    mDateSetListener,cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));
        }
    }

    public static class TimePickerFragment extends DialogFragment {

        private TimePickerDialog.OnTimeSetListener mTimeSetListener;

        public TimePickerFragment() {

        }

        @SuppressLint("ValidFragment")
        public TimePickerFragment(TimePickerDialog.OnTimeSetListener callback) {
            mTimeSetListener = (TimePickerDialog.OnTimeSetListener) callback;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar cal = Calendar.getInstance();
            return new TimePickerDialog(getActivity(),
                    mTimeSetListener,
                    cal.get(Calendar.HOUR),
                    cal.get(Calendar.MINUTE),
                    true);
        }
    }
}
