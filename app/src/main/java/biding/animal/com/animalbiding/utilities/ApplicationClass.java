package biding.animal.com.animalbiding.utilities;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.model.UserDetailModel;

/**
 * Created by JitendraSingh on 28-11-2016.
 */
public class ApplicationClass extends MultiDexApplication {

    private Context mContext;
    private RequestQueue mRequestQueue;
    private static ApplicationClass mInstance;
    private Activity mCurrentActivity = null;
    private Fragment mCurrentfragment;
    private List<UserDetailModel> userDetailModelList;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = this.getApplicationContext();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized ApplicationClass getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void setCurrentContext(Context context) {
        mContext = context;
    }

    public Context getCurrentContext() {
        return mContext;
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    public Context getContext() {
        return mContext;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.mCurrentfragment = currentFragment;
    }

    public Fragment getCurrentfragment() {
        return mCurrentfragment;
    }

    public List<UserDetailModel> getUserDetailModelList() {
        return userDetailModelList;
    }

    public void setUserDetailModelList(List<UserDetailModel> userDetailModelList) {
        this.userDetailModelList = userDetailModelList;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {

        }
    }

    //.....................................................................................................
    public DisplayImageOptions displayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.cow)
                .showImageOnFail(R.drawable.cow)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        return options;
    }

    //....
    public void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

}