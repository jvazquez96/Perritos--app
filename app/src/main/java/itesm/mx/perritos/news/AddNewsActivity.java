package itesm.mx.perritos.news;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import itesm.mx.perritos.R;

public class AddNewsActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar tlToolbar;

    private EditText editTitle;
    private EditText editDescription;
    private ImageButton imageCover;
    private CheckBox checkBox;


    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mNewsPhotosStorageReference;


    private News news;

    private Uri selectedImage;

    private static final int RC_PHOTO_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        tlToolbar = (Toolbar) findViewById(R.id.toolbar);
        editTitle = (EditText) findViewById(R.id.edit_title);
        editDescription = (EditText) findViewById(R.id.edit_description);
        imageCover = (ImageButton) findViewById(R.id.image_cover);
        checkBox = (CheckBox) findViewById(R.id.check_visible);
        setSupportActionBar(tlToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nueva noticia");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mNewsPhotosStorageReference = mFirebaseStorage.getReference().child("news_photo");
        imageCover.setOnClickListener(this);
        selectedImage = null;
        news = new News();

    }

    private boolean isAllDataCorrect() {
        if (editTitle.getText().toString().length() == 0 || editDescription.getText().toString().trim().length() == 0 ||selectedImage == null) {
            return false;
        }
        return true;
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
                news.setTitle(editTitle.getText().toString());
                news.setDescription((editDescription.getText().toString()));
                if (isAllDataCorrect()) {
                    Intent intent = new Intent();
                    intent.putExtra("News",news);
                    setResult(RESULT_OK,intent);
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
                StorageReference photoRef = mNewsPhotosStorageReference.child(selectedImage.getLastPathSegment());
                photoRef.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        Glide.with(imageCover.getContext())
                                .load(downloadUri.toString())
                                .into(imageCover);
                        news.setPhotoUrl(downloadUri.toString());
                    }
                });
            }
        }
    }
}
