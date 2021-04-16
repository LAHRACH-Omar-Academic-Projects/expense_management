package ma.ensaj.expensemanagement.ui.listOfMovements;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ma.ensaj.expensemanagement.R;
import ma.ensaj.expensemanagement.entity.Movement;
import ma.ensaj.expensemanagement.ui.newMovement.NewMovementActivity;
import ma.ensaj.expensemanagement.viewModel.MovementViewModel;

public class PopUpClass {
    private PopupWindow popupWindow;
    private View popupView;

    private Button edit_btn;
    private Button delete_btn;

    private Context context;
    private Movement movement;

    private MovementViewModel movementViewModel;

    public PopUpClass(Context context, Movement movement) {
        this.context = context;
        this.movement = movement;
    }

    public void showPopupWindow(final View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_window, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;

        popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        edit_btn = popupView.findViewById(R.id.update_btn);
        delete_btn = popupView.findViewById(R.id.delete_btn);

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateMovement.class);
                intent.putExtra("title", "Update Movement");

                intent.putExtra("id", movement.getId());
                intent.putExtra("type", movement.getType());
                intent.putExtra("amount", movement.getAmount());
                intent.putExtra("account", movement.getAccount());
                intent.putExtra("category", movement.getCategory());
                intent.putExtra("date", movement.getDate());
                intent.putExtra("time", movement.getTime());
                intent.putExtra("description", movement.getDescription());

                context.startActivity(intent);
                popupWindow.dismiss();
            }
        });


        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movementViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(MovementViewModel.class);
                movementViewModel.delete(movement);
                popupWindow.dismiss();
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

}
