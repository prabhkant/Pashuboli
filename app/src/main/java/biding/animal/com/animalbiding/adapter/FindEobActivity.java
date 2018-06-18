package biding.animal.com.animalbiding.adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.Request;

import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.utilities.Constants;
import biding.animal.com.animalbiding.utilities.VolleyInvokeWebService;

public class FindEobActivity extends AppCompatActivity {

    private static final int GET_PRODUCT_TAG = 1;
    private static final int GET_EOB_TAG = 2;

    private ImageView mBack;
    private Spinner categorySpinner;
    private RecyclerView productListView;
    private ProgressBar mProgressBar;

    private List<String> mCategoryNameArray;
    private List<String> mCategoryIdArray;
    private String catId = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_eob);
    }

    //...
    private void initiateViews() {
        mBack = (ImageView) findViewById(R.id.back_arrow);

    }

    //method to get animal category
    private void getAnimalCategory() {
        mProgressBar.setVisibility(View.VISIBLE);
//        VolleyInvokeWebService volleyClient = new VolleyInvokeWebService(mInstance.getContext(), VolleyInvokeWebService.JSON_TYPE_REQUEST, this, Request.Method.GET);
//        volleyClient.hitWithOutTokenService(Constants.GET_ANIMAL_CATEGORY, null, GET_ANIMAL_CATEGORY_TAG);
    }
}
