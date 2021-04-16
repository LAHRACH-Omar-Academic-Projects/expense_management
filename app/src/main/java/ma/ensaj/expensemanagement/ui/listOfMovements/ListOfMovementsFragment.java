package ma.ensaj.expensemanagement.ui.listOfMovements;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ma.ensaj.expensemanagement.R;
import ma.ensaj.expensemanagement.entity.Movement;
import ma.ensaj.expensemanagement.ui.newMovement.NewMovementActivity;
import ma.ensaj.expensemanagement.viewModel.MovementViewModel;

public class ListOfMovementsFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {
    private View root;

    private RecyclerView myRecyclerView;

    private Spinner month_spinner;
    private Spinner year_spinner;
    private Spinner account_spinner;
    private LinearLayout account_checkbox_group;
    private CheckBox card_checkbox;
    private CheckBox cash_checkbox;
    private CheckBox saving_checkbox;
    private RadioGroup movement_radio_group;
    private RadioButton allMovements_radio_button;
    private Spinner income_category_spinner;
    private Spinner expense_category_spinner;
    private TextView current_sold_editText;

    private String date;
    private List<String> accounts;
    private List<String> movementTypes;
    private List<String> categories;

    private String sortType = "ASC";

    private MyRecyclerViewAdapter adapter;
    private MovementViewModel movementViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_list_of_movements, container, false);

        month_spinner = root.findViewById(R.id.month);
        year_spinner = root.findViewById(R.id.year);
        account_spinner = root.findViewById(R.id.account);
        account_checkbox_group = root.findViewById(R.id.checkbox_group);
        card_checkbox = root.findViewById(R.id.card);
        cash_checkbox = root.findViewById(R.id.cash);
        saving_checkbox = root.findViewById(R.id.saving);
        movement_radio_group = root.findViewById(R.id.movement);
        allMovements_radio_button = root.findViewById(R.id.all);
        income_category_spinner = root.findViewById(R.id.income_category);
        expense_category_spinner = root.findViewById(R.id.expense_category);
        current_sold_editText = root.findViewById(R.id.current_sold);

        movementViewModel = new ViewModelProvider(this).get(MovementViewModel.class);

        initializeMonthSpinner();
        initializeYearSpinner();
        initializeAccountSpinner();
        initializeAccountCheckboxes();
        initializeMovementsRadioGroup();
        initializeIncomeCategories();
        initializeExpenseCategories();

        ImageView search_btn = getActivity().findViewById(R.id.search_btn);
        search_btn.setVisibility(View.VISIBLE);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout filter_card = getActivity().findViewById(R.id.filter_card);
                filter_card.setVisibility(View.GONE);
                TextView title = getActivity().findViewById(R.id.title);
                title.setVisibility(View.GONE);
                EditText search_editText = getActivity().findViewById(R.id.search_editText);
                search_editText.setVisibility(View.VISIBLE);
                ImageView close_btn = getActivity().findViewById(R.id.close_btn);
                close_btn.setVisibility(View.VISIBLE);
                search_btn.setVisibility(View.GONE);
                ImageView search_btn_left = getActivity().findViewById(R.id.search_btn_left);
                search_btn_left.setVisibility(View.VISIBLE);
                close_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search_btn.setVisibility(View.VISIBLE);
                        search_editText.setVisibility(View.GONE);
                        search_btn_left.setVisibility(View.GONE);
                        close_btn.setVisibility(View.GONE);
                        title.setVisibility(View.VISIBLE);
                        search_editText.setText("");
                        filter_card.setVisibility(View.VISIBLE);
                    }
                });

                search_editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        movementViewModel.getMovements("%" + s + "%", getAccounts()).observe(getViewLifecycleOwner(), new Observer<List<Movement>>() {
                            @Override
                            public void onChanged(List<Movement> movements) {
                                RecyclerView recyclerView = root.findViewById(R.id.listOfMovements);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                adapter = new MyRecyclerViewAdapter(getContext(), movements, true);
                                adapter.setClickListener(ListOfMovementsFragment.this::onItemClick);
                                recyclerView.setAdapter(adapter);
                            }
                        });
                        displayCurrentSoldByKeyWord("%" + s + "%");
                    }
                });
            }
        });


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
        FloatingActionButton ascSort = root.findViewById(R.id.ascendant);
        ascSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortType = "ASC";
                displayMovements();
            }
        });
        FloatingActionButton descSort = root.findViewById(R.id.descendant);
        descSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortType = "DESC";
                displayMovements();
            }
        });

        myRecyclerView = root.findViewById(R.id.listOfMovements);
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    newIncomeFab.setVisibility(View.GONE);
                    newExpenseFab.setVisibility(View.GONE);
                    newTransferFab.setVisibility(View.GONE);
                    ascSort.setVisibility(View.GONE);
                    descSort.setVisibility(View.GONE);
                } else {
                    newIncomeFab.setVisibility(View.VISIBLE);
                    newExpenseFab.setVisibility(View.VISIBLE);
                    newTransferFab.setVisibility(View.VISIBLE);
                    ascSort.setVisibility(View.VISIBLE);
                    descSort.setVisibility(View.VISIBLE);
                }
            }
        });

        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                displayMovements();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                displayMovements();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
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
        movement_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                if(radioButton.getText().equals("Income")) {
                    income_category_spinner.setVisibility(View.VISIBLE);
                    expense_category_spinner.setVisibility(View.GONE);
                }
                else if(radioButton.getText().equals("Expense")) {
                    expense_category_spinner.setVisibility(View.VISIBLE);
                    income_category_spinner.setVisibility(View.GONE);
                }
                else {
                    income_category_spinner.setVisibility(View.GONE);
                    expense_category_spinner.setVisibility(View.GONE);
                }
                displayMovements();
            }
        });
        income_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                displayMovements();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        expense_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                displayMovements();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        displayMovements();
        return root;
    }

    public void initializeMonthSpinner() {
        List<String> monthOptionsList = new ArrayList<>();
        monthOptionsList.addAll(Arrays.asList(getResources().getStringArray(R.array.months_array)));
        String newMonthOption = "All";
        monthOptionsList.add(newMonthOption);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter
                (getContext(), R.layout.simple_spinner_item, monthOptionsList);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        month_spinner.setAdapter(adapter);
        month_spinner.setSelection(month_spinner.getCount()-1);
    }

    public void initializeYearSpinner() {
        movementViewModel.getDateList().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> dateList) {
                ArrayList<String> yearOptionsList = new ArrayList<>();
                for(String date: dateList) {
                    String year = date.split("/")[2];
                    if(!yearOptionsList.contains(year)) {
                        yearOptionsList.add(year);
                    }
                }

                String newYearOption = "All";
                yearOptionsList.add(newYearOption);

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                        (getContext(), R.layout.simple_spinner_item, yearOptionsList);
                spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                year_spinner.setAdapter(spinnerArrayAdapter);
            }
        });
        year_spinner.setSelection(year_spinner.getCount()-1);
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

    public void initializeMovementsRadioGroup() {
        allMovements_radio_button.setChecked(true);
    }

    public void initializeIncomeCategories() {
        List<String> incomeCategoryOptionsList = new ArrayList<>();
        incomeCategoryOptionsList.addAll(Arrays.asList(getResources().getStringArray(R.array.income_category_array)));
        String newIncomeCategoryOption = "All";
        incomeCategoryOptionsList.add(newIncomeCategoryOption);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), R.layout.simple_spinner_item, incomeCategoryOptionsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        income_category_spinner.setAdapter(spinnerArrayAdapter);
        income_category_spinner.setSelection(income_category_spinner.getCount() - 1);
    }

    public void initializeExpenseCategories() {
        List<String> expenseCategoryOptionsList = new ArrayList<>();
        expenseCategoryOptionsList.addAll(Arrays.asList(getResources().getStringArray(R.array.expense_category_array)));
        String newExpenseCategoryOption = "All";
        expenseCategoryOptionsList.add(newExpenseCategoryOption);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), R.layout.simple_spinner_item, expenseCategoryOptionsList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        expense_category_spinner.setAdapter(spinnerArrayAdapter);
        expense_category_spinner.setSelection(expense_category_spinner.getCount() - 1);
    }

    public String getDate() {
        String yearOption = (String) year_spinner.getSelectedItem();
        int monthOptionNumber = month_spinner.getSelectedItemPosition() + 1;
        String monthOptionStr = String.valueOf(monthOptionNumber);
        if(yearOption == "All") {
            yearOption = "%";
        }
        if(monthOptionNumber == 13) {
            monthOptionStr = "%";
        }
        date = "%/" + monthOptionStr + "/" + yearOption;
        return date;
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

    public List<String> getMovementTypes() {
        movementTypes = new ArrayList<>();
        int checkedId = movement_radio_group.getCheckedRadioButtonId();
        if(checkedId == R.id.expense) {
            movementTypes.add("Expense");
        }
        else if(checkedId == R.id.income) {
            movementTypes.add("Income");
        }
        else {
            movementTypes.add("Income");
            movementTypes.add("Expense");
        }
        return movementTypes;
    }

    public List<String> getCategories() {
        categories = new ArrayList<>();
        int checkedId = movement_radio_group.getCheckedRadioButtonId();
        if(checkedId == R.id.expense) {
            String selectedCategory = expense_category_spinner.getSelectedItem().toString();
            if(selectedCategory.equals("All")) {
                categories.addAll(Arrays.asList(getResources().getStringArray(R.array.expense_category_array)));
                categories.add("Transfer");
            }
            else {
                categories.add(selectedCategory);
            }
        }
        else if(checkedId == R.id.income) {
            String selectedCategory = income_category_spinner.getSelectedItem().toString();
            if(selectedCategory.equals("All")) {
                categories.addAll(Arrays.asList(getResources().getStringArray(R.array.income_category_array)));
                categories.add("Transfer");
            }
            categories.add(selectedCategory);
        }
        else if(checkedId == R.id.all) {
            categories = Arrays.asList(getResources().getStringArray(R.array.all_categories_array));
        }
        else {
            categories.add("Transfer");
        }
        return categories;
    }

    public void displayMovements() {
        movementViewModel.getMovements(sortType, getDate(), getAccounts(), getMovementTypes(), getCategories()).observe(getViewLifecycleOwner(), new Observer<List<Movement>>() {
            @Override
            public void onChanged(List<Movement> movements) {
                RecyclerView recyclerView = root.findViewById(R.id.listOfMovements);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new MyRecyclerViewAdapter(getContext(), movements, true);
                adapter.setClickListener(ListOfMovementsFragment.this::onItemClick);
                recyclerView.setAdapter(adapter);
            }
        });
        displayCurrentSold();
    }

    public void displayCurrentSold() {
        movementViewModel.getSumAmount(getDate(), getAccounts(), getMovementTypes(), getCategories()).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double sum) {
                if(!(sum == null)) {
                    current_sold_editText.setText(String.valueOf(sum));
                }
                else {
                    current_sold_editText.setText("0.00");
                }
            }
        });
    }

    public void displayCurrentSoldByKeyWord(String key) {
        movementViewModel.getSumAmountByKeyWord(key, getAccounts()).observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double sum) {
                if(!(sum == null)) {
                    current_sold_editText.setText(String.valueOf(sum));
                }
                else {
                    current_sold_editText.setText("0.00");
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Movement movement = adapter.getItem(position);
        PopUpClass popUpClass = new PopUpClass(getContext(), movement);
        popUpClass.showPopupWindow(view);
    }
}
