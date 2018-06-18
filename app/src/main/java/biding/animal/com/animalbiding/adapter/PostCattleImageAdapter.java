package biding.animal.com.animalbiding.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.activities.PostCattleActivity;

/**
 * Created by Prabhakant on 09-02-2018.
 */

public class PostCattleImageAdapter extends RecyclerView.Adapter<PostCattleImageAdapter.MyViewHolder> {

    private ArrayList<Bitmap> mBitmapList;
    private Context mContext;
    private PostCattleActivity mFragment;

    public PostCattleImageAdapter(Context context, ArrayList<Bitmap> bitmapList, PostCattleActivity fragment) {
        mContext = context;
        mBitmapList = bitmapList;
        mFragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_image_lay, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.img.setImageBitmap(mBitmapList.get(position));
        holder.imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBitmapList.remove(position);
                notifyDataSetChanged();
                mFragment.imageCancelClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mBitmapList != null) {
            return mBitmapList.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private ImageView imgCancel;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_card);
            imgCancel = (ImageView) itemView.findViewById(R.id.img_cancel_iv);
        }
    }
}
