package itesm.mx.perritos.pet;

import android.content.ActivityNotFoundException;
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
import com.bumptech.glide.load.model.ResourceLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import itesm.mx.perritos.R;

public class AddPetActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Toolbar tlToolbar;

    private Button btnOK;
    private Button btnDelete;

    private ImageView imgCover;
    private static final int RC_PHOTO_PICKER = 2;
    private static final int CROP_IMAGE = 19;
    private Pet pet;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mPetPhotosStorageReference;

    private EditText editName;
    private EditText editDescription;
    private EditText editAge;

    private Spinner spinner;

    private String gender;

    private String selectedImage;

    private CheckBox checkVisibility;

    private boolean isEditing;

    private Uri imageLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pets);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnOK = (Button) findViewById(R.id.action_confirm);
        btnDelete = (Button) findViewById(R.id.button_delete);
        btnOK.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
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
        isEditing = false;
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
            selectedImage = pet.getPhotoUrl();
        } else {
            getSupportActionBar().setTitle("Nueva mascota");
            btnDelete.setVisibility(View.INVISIBLE);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.action_confirm) {

            pet.setName(editName.getText().toString());
            pet.setDescription(editDescription.getText().toString());
            pet.setAge(editAge.getText().toString());
            pet.setGender(this.gender);
            pet.setVisible(checkVisibility.isChecked());
            pet.setRequests(0);
            pet.setPhotoUrl(selectedImage);
            pet.setFav(false);
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
                setToastMessage("Por favor introduce todos los campos");
            }

        } else if (id == R.id.button_delete){
            Intent intent = new Intent();
            intent.putExtra("Delete",true);
            pet.setName(editName.getText().toString());
            pet.setDescription(editDescription.getText().toString());
            pet.setAge(editAge.getText().toString());
            pet.setGender(this.gender);
            pet.setVisible(checkVisibility.isChecked());
            pet.setRequests(0);
            pet.setPhotoUrl(selectedImage.toString());
            intent.putExtra("Pet",pet);
            setResult(RESULT_OK,intent);
            setToastMessage("Mascota eliminada");
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_PHOTO_PICKER) {
                imageLink = data.getData();
                CropImage.activity(imageLink)
                        .setAspectRatio(4,4)
                        .start(this);

            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if(resultCode == RESULT_OK){
                    Uri resultUri = result.getUri();

                    StorageReference photoRef = mPetPhotosStorageReference.child(resultUri.getLastPathSegment());

                    photoRef.putFile(resultUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
        }
    }
}
