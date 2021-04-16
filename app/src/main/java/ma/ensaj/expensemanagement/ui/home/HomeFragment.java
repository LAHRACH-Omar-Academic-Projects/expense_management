package ma.ensaj.expensemanagement.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ma.ensaj.expensemanagement.R;
import ma.ensaj.expensemanagement.entity.Movement;
import ma.ensaj.expensemanagement.ui.listOfMovements.ListOfMovementsFragment;
import ma.ensaj.expensemanagement.ui.listOfMovements.MyRecyclerViewAdapter;
import ma.ensaj.expensemanagement.ui.newMovement.NewMovementActivity;
import ma.ensaj.expensemanagement.viewModel.MovementViewModel;

public class HomeFragment extends Fragment {
    private View root;

    private Spinner account_spinner;
    private LinearLayout account_checkbox_group;
    private CheckBox card_checkbox;
    private CheckBox cash_checkbox;
    private CheckBox saving_checkbox;

    private List<String> accounts;

    private MovementViewModel movementViewModel;
    private MyRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.listOfMovements);
        account_spinner = root.findViewById(R.id.account);
        account_checkbox_group = root.findViewById(R.id.checkbox_group);
        card_checkbox = root.findViewById(R.id.card);
        cash_checkbox = root.findViewById(R.id.cash);
        saving_checkbox = root.findViewById(R.id.saving);

        movementViewModel = new ViewModelProvider(this).get(MovementViewModel.class);

        initializeAccountSpinner();
        initializeAccountCheckboxes();

        ImageView search_btn = (ImageView) getActivity().findViewById(R.id.search_btn);
        search_btn.setVisibility(View.GONE);

        FloatingActionButton newIncomeFab = root.findViewById(R.id.newIncome);
        newIncomeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewMovementActivity.class);
                intent.putExtra("tabIndex", 0);
                intent.putExtra("title", "Add Incomes");
                startActivity(intent);
            }
        });
        FloatingActionButton newExpenseFab = root.findViewById(R.id.newExpense);
        newExpenseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewMovementActivity.class);
                intent.putExtra("tabIndex", 1);
                intent.putExtra("title", "Add Expenses");
                startActivity(intent);
            }
        });
        FloatingActionButton newTransferFab = root.findViewById(R.id.newTransfer);
        newTransferFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewMovementActivity.class);
                intent.putExtra("tabIndex", 2);
                intent.putExtra("title", "Transfers");
                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    Log.d("Scroll", "scroll");
                    newIncomeFab.setVisibility(View.GONE);
                    newExpenseFab.setVisibility(View.GONE);
                    newTransferFab.setVisibility(View.GONE);
                } else {
                    newIncomeFab.setVisibility(View.VISIBLE);
                    newExpenseFab.setVisibility(View.VISIBLE);
                    newTransferFab.setVisibility(View.VISIBLE);
                }
            }
        });

        account_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0 || position == 1 || position == 2 || position == 4) {
                    account_checkbox_group.setVisibility(View.GONE);
                }
                else  {
                    account_checkbox_group.setVisibility(View.VISIBLE);
                }
                displayMovements();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        card_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                displayMovements();
            }
        });
        cash_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                displayMovements();
            }
        });
        saving_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                displayMovements();
            }
        });
        return root;
    }

    public void initializeAccountSpinner() {
        List<String> accountOptionsList = new ArrayList<>();
        accountOptionsList.addAll(Arrays.asList(getResources().getStringArray(R.array.accounts_array)));
        List<String> newAccountOptions = new ArrayList<>();
        newAccountOptions.add("Some accounts");
        newAccountOptions.add("All accounts");
        accountOptionsList.addAll(newAccountOptions);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), R.layout.simple_spinner_item, accountOptionsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        account_spinner.setAdapter(spinnerArrayAdapter);
        account_spinner.setSelection(account_spinner.getCount()-1);
    }

    public void initializeAccountCheckboxes() {
        card_checkbox.setChecked(true);
        cash_checkbox.setChecked(true);
        saving_checkbox.setChecked(true);
    }

    public List<String> getAccounts() {
        accounts = new ArrayList<>();
        String account = String.valueOf(account_spinner.getSelectedItem());
        switch (account) {
            case "Card":
                accounts.add("Card");
                break;
            case "Cash":
                accounts.add("Cash");
                break;
            case "Saving":
                accounts.add("Saving");
                break;
            case "All accounts":
                accounts.add("Card");
                accounts.add("Cash");
                accounts.add("Saving");
            case "Some accounts":
                if(card_checkbox.isChecked()) {
                    accounts.add("Card");
                }
                if(cash_checkbox.isChecked()) {
                    accounts.add("Cash");
                }
                if(saving_checkbox.isChecked()) {
                    accounts.add("Saving");
                }
                break;
        }
        return accounts;
    }

    public void displayMovements() {
        movementViewModel.getLast10MovementsByAccount(getAccounts()).observe(getViewLifecycleOwner(), new Observer<List<Movement>>() {
            @Override
            public void onChanged(List<Movement> movements) {
                RecyclerView recyclerView = root.findViewById(R.id.listOfMovements);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new MyRecyclerViewAdapter(getContext(), movements, false);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}