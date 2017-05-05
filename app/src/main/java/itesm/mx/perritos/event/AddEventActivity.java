package itesm.mx.perritos.event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.bumptech.glide.Glide;

import java.util.Calendar;

import itesm.mx.perritos.R;
import itesm.mx.perritos.pet.Pet;


public class AddEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener, TimePickerDialog.OnTimeSetListener  {

    private static final int RC_PHOTO_PICKER = 2;
    private TextView textStartDate;
    private TextView textEndDate;
    private TextView textStartTime;
    private TextView textEndTime;
    private Button btnEliminar;
    private Button btnAceptar;

    private Toolbar tlToolbar;
    private EditText tvTituloEvento;
    private TextView tvDescripcionEvento;
    private TextView textHoraInicio;
    private TextView tvHoraFinal;
    private CheckBox LugarVisible;
    private TextView AgregarLugar;
    private Evento MyEvent;
    private boolean StartDating = true;
    private boolean isEditing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnEliminar = (Button) findViewById(R.id.button_eliminar);
        btnAceptar = (Button) findViewById(R.id.button_aceptar);


        textStartDate = (TextView) findViewById(R.id.text_startDate);
        textEndDate = (TextView) findViewById(R.id.text_endDate);
        textStartTime = (TextView) findViewById(R.id.text_startTime);
        textEndTime = (TextView) findViewById(R.id.text_endTime);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTituloEvento = (EditText) findViewById(R.id.edit_title);
        tvDescripcionEvento = (EditText) findViewById(R.id.edit_description);
        textHoraInicio = (TextView) findViewById(R.id.text_startTime);
        tvHoraFinal = (TextView) findViewById(R.id.text_startTime);
        LugarVisible = (CheckBox) findViewById(R.id.check_visible);
        AgregarLugar = (TextView) findViewById(R.id.text_location);


        textStartDate.setOnClickListener(this);
        textEndDate.setOnClickListener(this);
        textStartTime.setOnClickListener(this);
        textEndTime.setOnClickListener(this);
        textStartDate.setOnClickListener(this);
        textEndDate.setOnClickListener(this);
        getSupportActionBar().setTitle("Nuevo Evento");


        btnEliminar.setOnClickListener(this);
        btnAceptar.setOnClickListener(this);
        isEditing = false;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isEditing = bundle.getBoolean("isEditing");
            getSupportActionBar().setTitle("Editar Evento");
            Evento evento = (Evento) bundle.getSerializable("Evento");
        } else {
            getSupportActionBar().setTitle("Nuevo evento");
            btnEliminar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confirm,menu);
        return true;
    }

    private void showDatePickerDialog(TextView Paramater) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = new DatePickerFragment(AddEventActivity.this);
        newFragment.show(ft, "date_dialog");
    }

    private void showTimePickerDialog(TextView Paramater) {
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
                StartDating = true;
                showDatePickerDialog(textStartDate);
                break;
            case R.id.text_startTime:
                StartDating = true;
                showTimePickerDialog(textStartTime);
                break;
            case R.id.text_endDate:
                StartDating = false;
                showDatePickerDialog(textEndDate);
                break;
            case R.id.text_endTime:
                StartDating = false;
                showTimePickerDialog(textEndTime);
                break;
            case R.id.AgregarImagen:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                break;

            case R.id.button_aceptar:
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
                //MyEvent.setIdImage(AgregarImagen.getId());

                if (isAllDataCorrect()) {
                    intent = new Intent();
                    intent.putExtra("Event", MyEvent);
                    setResult(RESULT_OK,intent);
                   // Toast.makeText(getApplicationContext(),"Logre mandar el Intent",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Por favor introduce todos los campos",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_eliminar:
                finish();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(StartDating){
            textStartDate.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
        }
        else{
            textEndDate.setText(String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(StartDating){
            textStartTime.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        }
        else{
            textEndTime.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        }
    }

    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener mDateSetListener;
        public String Date;

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

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_PHOTO_PICKER) {
                Uri imageLink = data.getData();
                StorageReference photoRef = mPetPhotosStorageReference.child(imageLink.getLastPathSegment());

                photoRef.putFile(imageLink).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        Glide.with(imgCover.getContext())
                                .load(downloadUri.toString())
                                .into(imgCover);
                        selectedImage = downloadUri.toString();
                        pet.setPhotoUrl(downloadUri.toString());
                    }
                });
            }
        }
    }*/
}
