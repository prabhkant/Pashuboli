package biding.animal.com.animalbiding.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import biding.animal.com.animalbiding.R;

/**
 * Created by Prabhakant.Agnihotri on 21-04-2018.
 */

public class AutoScrollAnimalViewPager extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> images;
    private boolean isClickable;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.cow)
            .showImageOnFail(R.drawable.cow)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    public AutoScrollAnimalViewPager(Context context, ArrayList<String> images, boolean b) {
        mContext = context;
        this.images = images;
        isClickable = b;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.animal_image_pager, container, false);

        ImageView img_pager = (ImageView) itemView.findViewById(R.id.cattle_img);
        try {
            if (images != null && !images.get(position).isEmpty()) {
                imageLoader.displayImage(images.get(position), img_pager, options);
            } else {
                img_pager.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cow));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        container.addView(itemView);

        if (isClickable)
            img_pager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Image Click", Toast.LENGTH_LONG).show();
                }
            });
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
