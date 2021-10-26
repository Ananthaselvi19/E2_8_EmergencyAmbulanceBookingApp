package com.example.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyAccount_Fragment extends Fragment{
    Button update;
    Button TakePhoto;
    TextView usernameView;
    EditText editUsername;
    EditText editedPass;
    EditText editContact;
    String currentPhotoPath, imageFileName;
    private DatabaseHelper myDB;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView profile;
    private final String CREDENTIAL_SHARED_PREF = "our_shared_pref";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_account_fragment, container, false);

        update = (Button) view.findViewById(R.id.Update);
        usernameView = (TextView) view.findViewById(R.id.usernameView);
        editUsername = (EditText) view.findViewById(R.id.editUsername);
        editedPass = (EditText) view.findViewById(R.id.editedPass);
        editContact = (EditText) view.findViewById(R.id.editContact);
        profile = (ImageView) view.findViewById(R.id.profile);
        TakePhoto = (Button) view.findViewById(R.id.TakePhoto);

        SharedPreferences credentials = this.getActivity().getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);

        String strUsername = credentials.getString("Username", null);
        usernameView.setText(strUsername);
        String strEditUsername = credentials.getString("Username",null);
        editUsername.setText(strEditUsername);
        String strPassword = credentials.getString("Password",null);
        editedPass.setText(strPassword);
        String strcontact_no = credentials.getString("contact_no", null);
        editContact.setText(strcontact_no);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences credentials = MyAccount_Fragment.this.getActivity().getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE);
                String updatedUsername = usernameView.getText().toString().trim();
                String updatedPassword = editedPass.getText().toString().trim();
                String updatedContact = editContact.getText().toString().trim();
//                myDB.update(strUsername, updatedUsername, updatedPassword, updatedContact);
                SharedPreferences.Editor editor = credentials.edit();
                editor.putString("editedPass", updatedPassword);
                editor.putString("editUsername", updatedUsername);
                editor.putString("editContact", updatedContact);
                editor.commit();
                Toast.makeText(getActivity(),"Update Successful", Toast.LENGTH_SHORT).show();
//                getFragmentManager().beginTransaction().remove(MyAccount_Fragment.this).commit();
                getActivity().onBackPressed();
            }
        });

        TakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent(view);
            }
        });

        return view;
    }

    public void dispatchTakePictureIntent(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            File photofile = null;
            try {
                photofile = createImageFile();
            }
            catch (IOException ex){
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            }
            if(photofile != null){
                Uri photoURI = FileProvider.getUriForFile(getActivity(),"com.example.login",photofile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            final File imageFile = new File(getActivity().getExternalFilesDir("images"), "my_profile_picture.jpg");
            if (imageFile.exists()) {
                imageFile.delete();
            }
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentPhotoPath);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(imageFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (final Exception e) {
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (final IOException e) {
                }
            }
            profile.setImageBitmap(imageBitmap);
            galleryAddPic();
        }
        else{
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public File createImageFile() throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
          imageFileName, ".jpg", storageDir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}

