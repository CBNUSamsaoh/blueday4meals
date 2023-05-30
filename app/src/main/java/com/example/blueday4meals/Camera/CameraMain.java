package com.example.blueday4meals.Camera;

import static com.doinglab.foodlens.sdk.NutritionRetrieveMode.TOP1_NUTRITION_ONLY;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.doinglab.foodlens.sdk.FoodLens;
import com.doinglab.foodlens.sdk.FoodLensBundle;
import com.doinglab.foodlens.sdk.NetworkService;
import com.doinglab.foodlens.sdk.NutritionResultHandler;
import com.doinglab.foodlens.sdk.RecognizeResultHandler;
import com.doinglab.foodlens.sdk.SearchResultHandler;
import com.doinglab.foodlens.sdk.UIService;
import com.doinglab.foodlens.sdk.UIServiceMode;
import com.doinglab.foodlens.sdk.UIServiceResultHandler;
import com.doinglab.foodlens.sdk.errors.BaseError;
import com.doinglab.foodlens.sdk.network.model.Box;
import com.doinglab.foodlens.sdk.network.model.Food;
import com.doinglab.foodlens.sdk.network.model.FoodPosition;
import com.doinglab.foodlens.sdk.network.model.FoodSearchResult;
import com.doinglab.foodlens.sdk.network.model.Nutrition;
import com.doinglab.foodlens.sdk.network.model.NutritionResult;
import com.doinglab.foodlens.sdk.network.model.RecognitionResult;
import com.doinglab.foodlens.sdk.network.model.UserSelectedResult;
import com.example.blueday4meals.Camera.listview.ListViewAdapter;
import com.example.blueday4meals.ChildMainPage;
import com.example.blueday4meals.Function.navigationbar;
import com.example.blueday4meals.NaverMap.NaverMapMain;
import com.example.blueday4meals.Nutrient.NutrientMain;
import com.example.blueday4meals.R;
import com.example.blueday4meals.SettingMain;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class CameraMain extends AppCompatActivity {

    private NetworkService ns;
    private UIService uiService;

    ListViewAdapter adapter;
    ListView listview;


    final int REQ_PICTURE = 0x02;

    RecognitionResult recognitionResult = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        try {
            FoodLensBundle bundle = new FoodLensBundle();
            bundle.setEnableManualInput(true);
            bundle.setSaveToGallery(true);
            bundle.setEnableManualInput(true);
            uiService.setDataBundle(bundle);
        } catch (Exception e) {

        }

        String userID = getIntent().getStringExtra("userID");

        Button btnMain, btnCam, btnNut, btnMap, btnSet, btncam;

        btnMain = findViewById(R.id.button1);
        btnMap = findViewById(R.id.button2);
        btnNut = findViewById(R.id.button3);
        btnCam = findViewById(R.id.button4);
        btnSet = findViewById(R.id.button5);
        btncam = findViewById(R.id.cam);

        ns = FoodLens.createNetworkService(this);
        uiService = FoodLens.createUIService(this);
        uiService.setUiServiceMode(UIServiceMode.USER_SELECTED_WITH_CANDIDATES);

        btncam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiService.startFoodLensCamera(CameraMain.this, new UIServiceResultHandler() {
                    @Override
                    public void onSuccess(UserSelectedResult result) {
                        recognitionResult = result;
                        setRecognitionResultData(recognitionResult);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "Recognition Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(BaseError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, ChildMainPage.class, userID);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, NaverMapMain.class, userID);
            }
        });

        btnNut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, NutrientMain.class, userID);
            }
        });

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, CameraMain.class, userID);
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new navigationbar(CameraMain.this, SettingMain.class, userID);
            }
        });
    }

    private void setRecognitionResultData(RecognitionResult recognitionResultData)
    {
        listview.setAdapter(null);
        adapter.clearItems();

        List<FoodPosition> foodPositions = recognitionResultData.getFoodPositions();

        String foodName = "";
        String foodNutritionInfo = "";
        String foodLocation = "";
        for(int i=0; i<foodPositions.size(); i++)
        {

            foodName = "";
            foodNutritionInfo = "";
            foodLocation = "";

            FoodPosition foodPosition = foodPositions.get(i);
            List<Food> foodList = foodPosition.getFoods();

            foodName = foodList.get(0).getFoodName();
            Nutrition nutrition = foodList.get(0).getNutrition();
            if(nutrition != null)
            {
                String carbon = "탄수화물: " + nutrition.getCarbonHydrate();
                String protein = "단백질: " + nutrition.getProtein();
                String fat = "지방: " + nutrition.getFat();

                foodNutritionInfo += (carbon +" " + protein +" "+fat);
            }

            Bitmap bitmap = BitmapFactory.decodeFile(foodPosition.getFoodImagePath());
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);


            Box box = foodPosition.getImagePosition();
            if(box!=null)
            {
                foodLocation = "음식 위치: ";
                foodLocation += String.valueOf(box.getXmin()) + " " + String.valueOf(box.getXmax()) + " " + String.valueOf(box.getYmin()) + " " + String.valueOf(box.getYmax());
            }

            adapter.addItem(drawable, foodName, foodNutritionInfo, foodLocation);
        }

        listview.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            final byte[] byteData = readContentIntoByteArray(new File(picturePath));

            ns = FoodLens.createNetworkService(getApplicationContext());
            ns.searchFoodsByName("라면", new SearchResultHandler() {
                @Override
                public void onSuccess(FoodSearchResult result) {
                    Toast.makeText(getApplicationContext(), "can", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(BaseError errorReason) {
                    Toast.makeText(getApplicationContext(), errorReason.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
            ns.setNutritionRetrieveMode(TOP1_NUTRITION_ONLY);
            ns.predictMultipleFood(byteData, new RecognizeResultHandler() {
                @Override
                public void onSuccess(RecognitionResult result) {
                    setRecognitionResultData(result);

                }

                @Override
                public void onError(BaseError errorReason) {
                    Log.e("FOODLENS_LOG", errorReason.getMessage());

                }
            });

            ns.getNutritionInfo(20, new NutritionResultHandler() {
                @Override
                public void onSuccess(NutritionResult result) {

                }

                @Override
                public void onError(BaseError errorReason) {

                }
            });
        }

        uiService.onActivityResult(requestCode, resultCode, data);
    }

    private byte[] readContentIntoByteArray(File file) {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }
}