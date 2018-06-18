package biding.animal.com.animalbiding.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import biding.animal.com.animalbiding.R;
import biding.animal.com.animalbiding.activities.NotificationActivity;
import biding.animal.com.animalbiding.model.NotificationModel;
import biding.animal.com.animalbiding.utilities.ApplicationClass;

/**
 * Created by Prabhakant.Agnihotri on 21-01-2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CategoryVH> {

    private LayoutInflater mInflater;
    private List<NotificationModel> mNotificationList;
    private ApplicationClass mInsatance;
    private NotificationActivity mActivity;

    public NotificationAdapter(NotificationActivity activity, List<NotificationModel> notificationList) {
        mActivity = activity;
        mNotificationList = notificationList;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInsatance = ApplicationClass.getInstance();
    }

    @Override
    public CategoryVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_notification_layout, parent, false);
        return new CategoryVH(view);
    }

    @Override
    public void onBindViewHolder(final CategoryVH holder, final int position) {
        holder.notificationText.setText(mNotificationList.get(position).getNotificationName());
        //set animation for itemview
        mInsatance.setScaleAnimation(holder.itemView);

    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    //class to hold the views
    class CategoryVH extends RecyclerView.ViewHolder {
        TextView notificationText, notificationDate, notificationTime;
        public CategoryVH(View itemView) {
            super(itemView);
            notificationDate = (TextView) itemView.findViewById(R.id.notificationDate);
            notificationText = (TextView) itemView.findViewById(R.id.notificationDetail);
            notificationTime = (TextView) itemView.findViewById(R.id.notificationTime);
        }
    }

}
