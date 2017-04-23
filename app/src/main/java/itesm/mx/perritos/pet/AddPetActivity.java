package itesm.mx.perritos.pet;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import itesm.mx.perritos.R;

public class AddPetActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Toolbar tlToolbar;
    private Button btnPicture;
    private ImageView imgCover;
    private static final int RC_PHOTO_PICKER = 2;
    private Pet pet;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPetPhotosStorageReference;

    private EditText editName;
    private EditText editDescription;
    private EditText editAge;

    private Spinner spinner;

    private String gender;

    private Uri selectedImage;

    private CheckBox checkVisibility;

    private Boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pets);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnPicture = (Button) findViewById(R.id.button_picture);
        btnPicture.setOnClickListener(this);
        imgCover = (ImageView) findViewById(R.id.image_pet);

        editName = (EditText) findViewById(R.id.edit_name);
        editDescription = (EditText) findViewById(R.id.edit_description);
        editAge = (EditText) findViewById(R.id.edit_age);
        checkVisibility = (CheckBox) findViewById(R.id.check_visible);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mPetPhotosStorageReference = mFirebaseStorage.getReference().child("pets_photos");
        pet = new Pet();

        gender = null;

        selectedImage = null;

        spinner = (Spinner) findViewById(R.id.spinner);
        String[] genderArray = {"Hembra","Macho"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter <String>(this,android.R.layout.simple_list_item_1,genderArray);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(genderAdapter);
        spinner.setOnItemSelectedListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Editing pet
            isEditing = bundle.getBoolean("isEditing");
            getSupportActionBar().setTitle("Editar mascota");
            Pet pet = (Pet) bundle.getSerializable("Pet");
            editName.setText(pet.getName());
            editDescription.setText(pet.getDescription());
            editAge.setText(pet.getAge());
            Glide.with(imgCover.getContext()).load(pet.getPhotoUrl()).into(imgCover);
            selectedImage = Uri.parse(pet.getPhotoUrl());
        } else {
            getSupportActionBar().setTitle("Nueva mascota");
        }

    }

    private boolean isAllDataCorrect() {
        if (editAge.getText().toString().trim().length() == 0 ||
                editDescription.getText().toString().trim().length() == 0 ||
                editAge.getText().toString().trim().length() == 0 ||
                selectedImage == null) {
            return false;
        }
        return true;
    }

    private void setToastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("DEBUG_TAG","GENDER SET: " + parent.getSelectedItem().toString());
        this.gender = parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confirm,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                pet.setName(editName.getText().toString());
                pet.setDescription(editDescription.getText().toString());
                pet.setAge(editAge.getText().toString());
                pet.setGender(this.gender);
                pet.setVisible(checkVisibility.isChecked());
                Log.d("DEBUG_TAG","Gender of the dog: " + this.gender);
                pet.setRequests(0);
                pet.setPhotoUrl(selectedImage.toString());
                if (isAllDataCorrect()) {
                    Intent intent = new Intent();
                    intent.putExtra("Pet", pet);
                    setResult(RESULT_OK, intent);
                    if (isEditing) {
                        setToastMessage("Mascota editada");
                    } else {
                        setToastMessage("Mascota agregada");
                    }
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Por favor introduce todos los campos",Toast.LENGTH_SHORT).show();
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(Intent.createChooser(intent,"Complete action using"),RC_PHOTO_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_PHOTO_PICKER) {
                selectedImage = data.getData();
                StorageReference photoRef = mPetPhotosStorageReference.child(selectedImage.getLastPathSegment());

                photoRef.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        Glide.with(imgCover.getContext())
                                .load(downloadUri.toString())
                                .into(imgCover);
                        pet.setPhotoUrl(downloadUri.toString());
                    }
                });
            }
        }
    }
}
