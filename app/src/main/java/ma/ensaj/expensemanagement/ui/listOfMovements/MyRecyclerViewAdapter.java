package ma.ensaj.expensemanagement.ui.listOfMovements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import ma.ensaj.expensemanagement.R;
import ma.ensaj.expensemanagement.entity.Movement;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private View view;
    private List<Movement> movementList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;
    private boolean moreDetails;

    public MyRecyclerViewAdapter(Context context, List<Movement> movementList, boolean moreDetails) {
        this.mInflater = LayoutInflater.from(context);
        this.movementList = movementList;
        this.context = context;
        this.moreDetails = moreDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (moreDetails) {
             view = mInflater.inflate(R.layout.movement_row, parent, false);
        }
        else {
             view = mInflater.inflate(R.layout.lastmovement_row, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movement movement = movementList.get(position);
        holder.category.setText(movement.getCategory());
        holder.description.setText(movement.getDescription());
        if(movement.getAmount() < 0) {
            holder.amount.setText(String.valueOf(-movement.getAmount()));
            holder.sign.setText(" -");
            holder.sign.setTextColor(context.getResources().getColor(R.color.red));
        }
        else {
            holder.amount.setText(String.valueOf(movement.getAmount()));
            holder.sign.setText(" +");
            holder.sign.setTextColor(context.getResources().getColor(R.color.green));
        }
        if(moreDetails) {
            holder.account.setText(movement.getAccount());
            holder.date_time.setText(movement.getDate() + ", " + movement.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return movementList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView category;
        TextView account;
        TextView description;
        TextView date_time;
        TextView amount;
        TextView sign;

        ViewHolder(View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.category);
            account = itemView.findViewById(R.id.account);
            description = itemView.findViewById(R.id.description);
            date_time = itemView.findViewById(R.id.date_time);
            amount = itemView.findViewById(R.id.amount);
            sign = itemView.findViewById(R.id.sign);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Movement getItem(int id) {
        return movementList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
