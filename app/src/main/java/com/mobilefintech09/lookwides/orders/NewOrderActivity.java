package com.mobilefintech09.lookwides.orders;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ActivityNavigator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilefintech09.lookwides.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class NewOrderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "NewOrderActivity";
    Spinner mSpinner, mSpinner2;
    EditText mEditText, mEditText2, txtRName, txtRPhone, txtRAddress;
    TextView mTextViewImg;
    Button mButton, mButton2;
    private String spinnerLabel;
    Bitmap bitmap;
    File file;
    Uri filePath;
    int prices, prices2;
    private String mAmount;
    private EditText mDesc;
    private String mDescribe;
    private String mSpin;
    private String mSpin2;
    private String mWeight;
    private String mNumItems;
    private String recieverName;
    private String recieverAddress;
    private String recieverPhone;
    private byte[] mByteArray;
    private String mFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        mEditText = findViewById(R.id.editText4);
        mEditText2 = findViewById(R.id.editText5);
        txtRName = findViewById(R.id.editTextRName);
        txtRAddress = findViewById(R.id.editTextRAddress);
        txtRPhone = findViewById(R.id.editTextRPhone);

        mTextViewImg = findViewById(R.id.textViewImage);

        mDesc = findViewById(R.id.editText);
        mSpinner = findViewById(R.id.spinner);
        mSpinner2 = findViewById(R.id.spinner2);

        if (mSpinner != null) {
            mSpinner.setOnItemSelectedListener(this);
        }
        if (mSpinner2 != null) {
            mSpinner2.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this, R.array.locations_array,
                android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (mSpinner != null) {
            mSpinner.setAdapter(adapterSpinner);
        }

        ArrayAdapter<CharSequence> adapterSpinner2 = ArrayAdapter.createFromResource(this, R.array.locations_array,
                android.R.layout.simple_spinner_item);
        adapterSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (mSpinner2 != null) {
            mSpinner2.setAdapter(adapterSpinner2);
        }



        mButton2 = findViewById(R.id.buttonImg);
         mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });


        mButton = findViewById(R.id.button_Proceed);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent();
            }
        });
    }

    private String calculatePrice(String location, String destination) {
        String[] locationAmount = this.getResources().getStringArray(R.array.locations_array);
        String[] price = this.getResources().getStringArray(R.array.prices_array);

        for(int i = 0; i < locationAmount.length; i++){
            if(location != null && location.equals(locationAmount[i])){
                prices = Integer.parseInt(price[i]);
            }
            if(destination != null && destination.equals(locationAmount[i])){
                prices2 = Integer.parseInt(price[i]);
            }
        }
        if(location == destination || location == "Lokogoma"){
            mAmount = String.valueOf(prices2);
        }else {
            mAmount = String.valueOf(prices + (prices2));
        }
        Log.w(TAG, mAmount);
        return mAmount;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                file =  new File(Objects.requireNonNull(filePath.getPath()));
                mFilename = file.getName();
                FileOutputStream stream= this.openFileOutput(mFilename, Context.MODE_PRIVATE);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                  bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                mByteArray = stream.toByteArray();
                //Cleanup
                stream.close();
                bitmap.recycle();
                mTextViewImg.setText(mFilename);

//                mImageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        String[] mimeTypes = new String[] { "image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerLabel = parent.getItemAtPosition(position).toString();

    }

    private void displayToast(String label) {
        Toast.makeText(this, label, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void sendIntent() {
        mDescribe = mDesc.getEditableText().toString();
        mSpin = mSpinner.getSelectedItem().toString();
        mSpin2 = mSpinner2.getSelectedItem().toString();
        mWeight = mEditText.getEditableText().toString();
        mNumItems = mEditText2.getEditableText().toString();

        recieverName = txtRName.getEditableText().toString();
        recieverAddress = txtRAddress.getEditableText().toString();
        recieverPhone = txtRPhone.getEditableText().toString();
        Log.w(TAG, mSpin);
        String newAmount = calculatePrice(mSpin, mSpin2);

        //Validate Inputs
        if (validateInputs(mDescribe, mDesc) && validateInputs(mWeight, mEditText)&& validateInputs(mNumItems, mEditText2) &&
        validateInputs(recieverName, txtRName) && validateInputs(recieverAddress, txtRAddress)&& validateInputs(recieverPhone, txtRPhone))
        {

            Intent summaryIntent = new Intent(NewOrderActivity.this, OrderSummaryActivity.class);
            summaryIntent.putExtra("Description", mDescribe);
            summaryIntent.putExtra("Location", mSpin);
            summaryIntent.putExtra("Destination", mSpin2);
            summaryIntent.putExtra("Weight", mWeight);
            summaryIntent.putExtra("Items", mNumItems);
            summaryIntent.putExtra("Reciever_Name", recieverName);
            summaryIntent.putExtra("Reciever_Address", recieverAddress);
            summaryIntent.putExtra("Reciever_Phone", recieverPhone);
            if (newAmount != null) {
                summaryIntent.putExtra("Price", newAmount);
            }
            if (mFilename != null) {
                summaryIntent.putExtra("Bitmap", mFilename);
            }
            startActivity(summaryIntent);
        }
    }

    private boolean validateInputs(String value, EditText view) {
        if (value.isEmpty()) {
            view.setError(view.getHint() + " Field must not be empty");
            view.requestFocus();
            return false;
        } else {
            view.setError(null);
            return true;
        }
    }
}
