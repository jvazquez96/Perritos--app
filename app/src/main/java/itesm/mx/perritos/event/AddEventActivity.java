package itesm.mx.perritos.event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import itesm.mx.perritos.R;

public class AddEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener, TimePickerDialog.OnTimeSetListener  {

    private TextView textStartDate;
    private TextView textEndDate;
    private Toolbar tlToolbar;
    private EditText tvTituloEvento;
    private TextView tvDescripcionEvento;
    private TextView textHoraInicio;
    private TextView tvHoraFinal;
    private ImageButton ButonAgregar;
    private CheckBox LugarVisible;
    private TextView AgregarLugar;
    private ImageView ImagenEvento;
    private Evento MyEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        textStartDate = (TextView) findViewById(R.id.text_startTime);
        textEndDate = (TextView) findViewById(R.id.text_endTime);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTituloEvento = (EditText) findViewById(R.id.edit_title);
        tvDescripcionEvento = (EditText) findViewById(R.id.edit_description);
        textHoraInicio = (TextView) findViewById(R.id.text_startTime);
        tvHoraFinal = (TextView) findViewById(R.id.text_startTime);
        ButonAgregar = (ImageButton) findViewById(R.id.ButtonAgregarEvento);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textStartDate.setOnClickListener(this);
        textEndDate.setOnClickListener(this);
        ButonAgregar.setOnClickListener(this);
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

    private boolean isAllDataCorrect() {
        if (tvTituloEvento.getText().toString().length() == 0 || tvDescripcionEvento.getText().toString().trim().length() == 0
                || textStartDate.getText().toString().length() == 0 ||  textEndDate.getText().toString().length() == 0
                || textHoraInicio.getText().toString().length() == 0
                || tvHoraFinal.getText().toString().length() == 0 || AgregarLugar.getText().length() == 0){
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_startDate:
                showDatePickerDialog();
                break;
            case R.id.text_startTime:
                showTimePickerDialog();
                break;
            case R.id.text_endDate:
                showDatePickerDialog();
                break;
            case R.id.text_endTime:
                showTimePickerDialog();
                break;
            case R.id.ButtonAgregarEvento:
                MyEvent = new Evento();
                MyEvent.setStartDate(textStartDate.getText().toString());
                MyEvent.setEndDate(textEndDate.getText().toString());
                MyEvent.setTitle(tvTituloEvento.getText().toString());
                MyEvent.setDescription(tvDescripcionEvento.getText().toString());
                MyEvent.setStartDate(textStartDate.getText().toString());
                MyEvent.setHoraInicio(textHoraInicio.getText().toString());
                MyEvent.setHoraFinal(tvHoraFinal.getText().toString());
                MyEvent.setLugarVisible(LugarVisible.isChecked());
                MyEvent.setLugar(AgregarLugar.getText().toString());
                MyEvent.setTitle(tvTituloEvento.getText().toString());
                MyEvent.setIdImage(ImagenEvento.getId());

                if (isAllDataCorrect()) {
                    Intent intent = new Intent();
                    intent.putExtra("Event", MyEvent);
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Por favor introduce todos los campos",Toast.LENGTH_SHORT).show();
                }
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
