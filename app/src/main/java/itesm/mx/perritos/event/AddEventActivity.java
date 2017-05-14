/*
        Copyright (C) 2017  Jorge Armando Vazquez Ortiz, Valentin Alexandro Trujillo, Santiago Sandoval
        Treviño and Gerardo Suarez Martinez
        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>
*/
package itesm.mx.perritos.event;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DateFormatSymbols;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import itesm.mx.perritos.R;
import itesm.mx.perritos.Utils.CurrentUser;


public class AddEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener, TimePickerDialog.OnTimeSetListener  {

    private static final int RC_PHOTO_PICKER = 2;
    private TextView textStartDate;
    private TextView textEndDate;
    private TextView textHoraInicio;
    private TextView tvHoraFinal;
    private Button btnEliminar;
    private Button btnAceptar;
    private Button AgregarImagen;
    private ImageView ImagenEvento;
    private Toolbar tlToolbar;
    private EditText tvTituloEvento;
    private TextView tvDescripcionEvento;
    private CheckBox LugarVisible;
    private EditText AgregarLugar;
    private Evento MyEvent;
    private boolean StartDating = true;
    private boolean isEditing;
    private StorageReference mEventPhotosStorageReference;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private Uri downloadUri;
    private boolean isFav;

    private Uri imageLink;
    private String selectedImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnEliminar = (Button) findViewById(R.id.button_eliminar);
        btnAceptar = (Button) findViewById(R.id.button_aceptar);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mEventPhotosStorageReference = mFirebaseStorage.getReference().child("event_photos");
        MyEvent = new Evento();
        isFav = false;
        // AgregarImagen = (Button) findViewById(R.id.button_picture);


        textStartDate = (TextView) findViewById(R.id.text_startDate);
        textEndDate = (TextView) findViewById(R.id.text_endDate);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTituloEvento = (EditText) findViewById(R.id.edit_title);
        tvDescripcionEvento = (EditText) findViewById(R.id.edit_description);
        textHoraInicio = (TextView) findViewById(R.id.text_startTime);
        tvHoraFinal = (TextView) findViewById(R.id.text_endTime);
        LugarVisible = (CheckBox) findViewById(R.id.check_visible);
        AgregarLugar = (EditText) findViewById(R.id.autocomplete);
        ImagenEvento = (ImageView) findViewById(R.id.ImagenEvento);


        textStartDate.setOnClickListener(this);
        textEndDate.setOnClickListener(this);
        textHoraInicio.setOnClickListener(this);
        tvHoraFinal.setOnClickListener(this);
        getSupportActionBar().setTitle("Nuevo Evento");


        btnEliminar.setOnClickListener(this);
        btnAceptar.setOnClickListener(this);
        //AgregarImagen.setOnClickListener(this);
        isEditing = false;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isEditing = bundle.getBoolean("isEditing");
            getSupportActionBar().setTitle("Editar Evento");
            Evento evento = (Evento) bundle.getSerializable("Event");
            textStartDate.setText(evento.getStartDate());
            textEndDate.setText(evento.getEndDate());
            tvTituloEvento.setText(evento.getTitle());
            tvDescripcionEvento.setText(evento.getDescription());
            textHoraInicio.setText(evento.getHoraInicio());
            tvHoraFinal.setText(evento.getHoraFinal());
            LugarVisible.setChecked(evento.getLugarVisible());
            AgregarLugar.setText(evento.getLugar());
            isFav = evento.isUserInList(CurrentUser.getmInstance().getUserEmail());
            selectedImage = evento.getphotoURL();
            if(evento.getphotoURL() != null) {
                // Glide library using circular image crop
                Glide.with(ImagenEvento.getContext()).load(evento.getphotoURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ImagenEvento) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(ImagenEvento.getContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ImagenEvento.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
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
                showTimePickerDialog(textHoraInicio);
                break;
            case R.id.text_endDate:
                StartDating = false;
                showDatePickerDialog(textEndDate);
                break;
            case R.id.text_endTime:
                StartDating = false;
                showTimePickerDialog(tvHoraFinal);
                break;

            case R.id.button_aceptar:
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
                MyEvent.setPhotoURL(selectedImage);
                if(isFav) {
                    MyEvent.addLikedUser(CurrentUser.getmInstance().getUserEmail());
                }
                if (isAllDataCorrect()) {
                    Intent intent = new Intent();
                    intent.putExtra("Event", MyEvent);
                    setResult(RESULT_OK,intent);
                   // Toast.makeText(getApplicationContext(),"Logre mandar el Intent",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Por favor introduce todos los campos",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_eliminar:
                Intent intent = new Intent();
                intent.putExtra("Delete",true);
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
               // MyEvent.setPhotoURL(downloadUri.toString());
                setResult(RESULT_OK,intent);
                Toast.makeText(getApplicationContext(),"Evento Eliminado",Toast.LENGTH_SHORT).show();
                finish();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(StartDating){
            textStartDate.setText(String.valueOf(dayOfMonth) + " " + new DateFormatSymbols().getMonths()[month-1]  + " " + String.valueOf(year));
        }
        else{
            textEndDate.setText(String.valueOf(dayOfMonth) + " " + new DateFormatSymbols().getMonths()[month-1] + " " + String.valueOf(year));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String TextHour;
        String TextMin;
        if(hourOfDay < 10){
            TextHour = "0" + String.valueOf(hourOfDay) + ":";
        } else{
            TextHour = String.valueOf(hourOfDay) + ":";
        }
        if(minute < 10){
            TextMin = "0" + String.valueOf(minute);
        } else{
            TextMin = String.valueOf(minute);
        }
        if(StartDating){
            textHoraInicio.setText(TextHour + TextMin);
        }
        else{
            tvHoraFinal.setText(TextHour + TextMin);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.button_picture:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                break;
        }
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_PHOTO_PICKER) {
                Uri imageLink = data.getData();
                StorageReference photoRef = mEventPhotosStorageReference.child(imageLink.getLastPathSegment());

                photoRef.putFile(imageLink).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadUri = taskSnapshot.getDownloadUrl();
                        Glide.with(ImagenEvento.getContext())
                                .load(downloadUri.toString())
                                .into(ImagenEvento);
                        selectedImage = downloadUri.toString();
                    }
                });
            }
        }
    }

}
