package ma.ensaj.expensemanagement.ui.reportsByDate;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ma.ensaj.expensemanagement.R;
import ma.ensaj.expensemanagement.entity.Movement;
import ma.ensaj.expensemanagement.ui.listOfMovements.MyRecyclerViewAdapter;
import ma.ensaj.expensemanagement.viewModel.MovementViewModel;

public class ReportsByDateFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {
    private View root;

    private Spinner accounts_spinner;
    private Spinner movement_types_spinner;
    private Spinner durations_spinner;
    private Button full_date_btn;
    private TextView income_text_view;
    private TextView expense_text_view;
    private TextView current_sold_text_view;

    private List<String> accounts;
    private List<String> movementTypes;
    private String duration;
    private List<String> categories;

    private String fullDate;

    private MyRecyclerViewAdapter adapter;
    private MovementViewModel movementViewModel;

    private String movementType;
    private double income;
    private double expense;
    private double currentSold;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_reports_by_date, container, false);
        movementViewModel = new ViewModelProvider(this).get(MovementViewModel.class);

        defineUi();
        initializeUi();

        ImageView search_btn = (ImageView) getActivity().findViewById(R.id.search_btn);
        search_btn.setVisibility(View.GONE);

        accounts_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                displayData();
                getReport();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        movement_types_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                displayData();
                getReport();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        durations_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    full_date_btn.setVisibility(View.VISIBLE);
                    initializeFullDate();
                }
                else {
                    full_date_btn.setVisibility(View.GONE);
                }
                displayData();
                getReport();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        displayData();
        getReport();
        return root;
    }

    public void defineUi() {
        accounts_spinner = root.findViewById(R.id.account);
        movement_types_spinner = root.findViewById(R.id.movement_types);
        durations_spinner = root.findViewById(R.id.durations);

        full_date_btn = root.findViewById(R.id.date);

        income_text_view = root.findViewById(R.id.income);
        expense_text_view = root.findViewById(R.id.expense);
        current_sold_text_view = root.findViewById(R.id.current_sold);
    }

    public void initializeUi() {
        initializeAccounts();
        initializeMovementTypes();
        initializeDurations();
    }

    public void initializeAccounts() {
        List<String> accountOptionsList = new ArrayList<>();
        accountOptionsList.addAll(Arrays.asList(getResources().getStringArray(R.array.accounts_array)));
        List<String> newAccountOptions = new ArrayList<>();
        newAccountOptions.add("All accounts");
        accountOptionsList.addAll(newAccountOptions);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), R.layout.simple_spinner_item, accountOptionsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        accounts_spinner.setAdapter(spinnerArrayAdapter);
        accounts_spinner.setSelection(accounts_spinner.getCount()-1);
    }

    public void initializeMovementTypes() {
        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.movement_types_array, R.layout.simple_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        movement_types_spinner.setAdapter(spinnerArrayAdapter);
    }

    public void initializeDurations() {
        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.durations_array, R.layout.simple_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        durations_spinner.setAdapter(spinnerArrayAdapter);
    }

    public void initializeFullDate() {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        full_date_btn.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        full_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                full_date_btn.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                displayData();
                                getReport();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    public List<String> getAccounts() {
        accounts = new ArrayList<>();
        String account = String.valueOf(accounts_spinner.getSelectedItem());
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
        }
        return accounts;
    }

    public List<String> getMovementTypes() {
        movementTypes = new ArrayList<>();
        String movementType = String.valueOf(movement_types_spinner.getSelectedItem());
        switch (movementType) {
            case "Income":
                movementTypes.add("Income");
                break;
            case "Expense":
                movementTypes.add("Expense");
                break;
            case "All":
                movementTypes.add("Income");
                movementTypes.add("Expense");
        }
        return movementTypes;
    }

    public String getDuration() {
        duration = String.valueOf(durations_spinner.getSelectedItem());
        return duration;
    }

    public String getFullDate() {
        fullDate = (String) full_date_btn.getText();
        return fullDate;
    }

    public List<String> getCategories() {
        categories = new ArrayList<>();
        categories.addAll(Arrays.asList(getResources().getStringArray(R.array.all_categories_array)));
        categories.remove(categories.size() - 1);
        return categories;
    }

    public void displayData() {
        if(getDuration().equals("Per day")) {
            movementViewModel.getMovements("ASC", getFullDate(), getAccounts(), getMovementTypes(), getCategories()).observe(getViewLifecycleOwner(), new Observer<List<Movement>>() {
                @Override
                public void onChanged(List<Movement> movements) {
                    RecyclerView recyclerView = root.findViewById(R.id.reports);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new MyRecyclerViewAdapter(getContext(), movements, true);
                    adapter.setClickListener(ReportsByDateFragment.this::onItemClick);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
    }

    public void getReport() {
        movementType = String.valueOf(movement_types_spinner.getSelectedItem());
        if(movementType.equals("Income")) {
            movementViewModel.getSumAmountByDate(getAccounts(), "Income", getFullDate()).observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double sum) {
                    if(!(sum == null)) {
                        income_text_view.setText(String.valueOf(sum));
                        expense_text_view.setText("0.0");
                        current_sold_text_view.setText(String.valueOf(sum));
                    }
                    else {
                        income_text_view.setText("0.0");
                        expense_text_view.setText("0.0");
                        current_sold_text_view.setText("0.0");
                    }
                }
            });
        }
        else if(movementType.equals("Expense")) {
            movementViewModel.getSumAmountByDate(getAccounts(), "Expense", getFullDate()).observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double sum) {
                    if(!(sum == null)) {
                        income_text_view.setText("0.0");
                        expense_text_view.setText(String.valueOf(sum));
                        current_sold_text_view.setText(String.valueOf(sum));
                    }
                    else {
                        income_text_view.setText("0.0");
                        expense_text_view.setText("0.0");
                        current_sold_text_view.setText("0.0");
                    }
                }
            });
        }
        else {
            movementViewModel.getSumAmountByDate(getAccounts(), "Income", getFullDate()).observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double sum) {
                    if(!(sum == null)) {
                        income = sum;
                    }
                    else {
                        income = 0.0;
                    }
                    setIncome(income);
                }
            });
            movementViewModel.getSumAmountByDate(getAccounts(), "Expense", getFullDate()).observe(getViewLifecycleOwner(), new Observer<Double>() {
                @Override
                public void onChanged(Double sum) {
                    if(!(sum == null)) {
                        expense = sum;
                    }
                    else {
                        expense = 0.0;
                    }
                    setExpense(expense);
                }
            });
        }
        Log.d("Income", getIncome() + "" );
        currentSold = income + expense;
        income_text_view.setText(String.valueOf(income));
        expense_text_view.setText(String.valueOf(expense));
        current_sold_text_view.setText(String.valueOf(currentSold));
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public double getCurrentSold() {
        return currentSold;
    }

    public void setCurrentSold(double currentSold) {
        this.currentSold = currentSold;
    }
}