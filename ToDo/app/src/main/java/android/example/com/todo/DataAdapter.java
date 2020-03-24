package android.example.com.todo;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder> {

    private LayoutInflater mInflater;
    private TaskOpenHelper mDB;

    public DataAdapter(Context context, TaskOpenHelper db){
        mInflater = LayoutInflater.from(context);
        //mTaskData = taskData;
        mDB = db;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_data_view, parent, false);
        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        Task task = mDB.queryTask(position);
        holder.mHeadingTextView.setText(task.getHeading());
        holder.mDetailTextView.setText(task.getDetail());
        if(task.isPending()){
            holder.mPendingImageView.setImageResource(R.drawable.ic_action_pending);
            holder.mPendingImageView.setBackgroundColor(Color.YELLOW);
        }
        else{
            holder.mPendingImageView.setImageResource(R.drawable.ic_action_done);
            holder.mPendingImageView.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return mDB.getSize();
    }

    class DataHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private TextView mHeadingTextView;
        private TextView mDetailTextView;
        private ImageView mPendingImageView;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public DataHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnCreateContextMenuListener(this);
            mHeadingTextView = itemView.findViewById(R.id.heading);
            mDetailTextView = itemView.findViewById(R.id.detail);
            mPendingImageView = itemView.findViewById(R.id.pending_img);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            String changState;
            menu.setHeaderTitle("Select the Action");
            Task task = mDB.queryTask(getAdapterPosition());
            if(task.isPending()) {
                changState = "Mark Done";
            }
            else{
                changState = "Mark Pending";
            }
            menu.add(getAdapterPosition(), v.getId(), 0, changState);
            menu.add(getAdapterPosition(), v.getId(), 1, "Delete");
        }

    }
}
