package ma.ensaj.expensemanagement.ui.listOfMovements;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ma.ensaj.expensemanagement.R;
import ma.ensaj.expensemanagement.entity.Movement;
import ma.ensaj.expensemanagement.viewModel.MovementViewModel;

public class UpdateMovement extends AppCompatActivity {
    private MovementViewModel movementViewModel;
    private Toolbar toolbar;
    private Intent intent;

    private EditText amount_editText;
    private Spinner account_spinner;
    private Spinner category_spinner;
    private Button date_button;
    private Button time_button;
    private EditText description_editText;
    private Button save_button;

    private ArrayAdapter<CharSequence> spinnerAccountArrayAdapter;
    private ArrayAdapter<CharSequence> spinnerCategoryArrayAdapter;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private String type;
    private double amount;
    private String account;
    private String category;
    private String date;
    private String time;
    private String description;

    private TextView title;
    private ImageView close_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_movement);

        intent = getIntent();
        configureToolbar();

        amount_editText = findViewById(R.id.amount);
        account_spinner = findViewById(R.id.account);
        category_spinner = findViewById(R.id.category);
        date_button = findViewById(R.id.date);
        time_button = findViewById(R.id.time);
        description_editText = findViewById(R.id.description);
        save_button = findViewById(R.id.save);

        setType();

        initializeAccountSpinner();
        initializeCategorySpinner();

        setAmount();
        setAccount();
        setCategory();
        setDate();
        setTime();
        setDescription();

        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker();
            }
        });

        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTimePicker();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount_editText.getText().length() == 0) {
                    amount_editText.startAnimation(shakeError());
                }
                else {
                    updateMovement(type,
                            getAmount(),
                            getAccount(),
                            getCategory(),
                            getDate(),
                            getTime(),
                            getDescription());
                    finish();
                }
            }
        });
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 0, 0, 20);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(2));
        return shake;
    }

    public void configureToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String titleText = intent.getStringExtra("title");
        title = (TextView) findViewById(R.id.title);
        title.setText(titleText);

        setCloseAction();
    }

    public void setCloseAction() {
        close_btn = (ImageView) findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initializeAccountSpinner() {
        account_spinner = findViewById(R.id.account);
        if(!intent.getStringExtra("category").equals("Transfer")) {
            spinnerAccountArrayAdapter = ArrayAdapter.createFromResource(
                    this, R.array.accounts_array, R.layout.simple_spinner_item);
        }
        else {
            List<String> accounts = new ArrayList<>();
            accounts.add(intent.getStringExtra("account"));
            spinnerAccountArrayAdapter = new ArrayAdapter(
                                this, R.layout.simple_spinner_item, accounts);
        }
        spinnerAccountArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        account_spinner.setAdapter(spinnerAccountArrayAdapter);
    }

    public void initializeCategorySpinner() {
        category_spinner = findViewById(R.id.category);
        if(type.equals("Income")) {
            spinnerCategoryArrayAdapter = ArrayAdapter.createFromResource(
                    this, R.array.income_category_array, R.layout.simple_spinner_item);
        }
        else if(type.equals("Expense")) {
            spinnerCategoryArrayAdapter = ArrayAdapter.createFromResource(
                    this, R.array.expense_category_array, R.layout.simple_spinner_item);
        }
        if(intent.getStringExtra("category").equals("Transfer")) {
            spinnerCategoryArrayAdapter = ArrayAdapter.createFromResource(
                    this, R.array.transfer_category, R.layout.simple_spinner_item);
        }
        spinnerCategoryArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(spinnerCategoryArrayAdapter);
    }

    public void setType() {
        type = intent.getStringExtra("type");
    }

    public void setAmount() {
        amount = intent.getDoubleExtra("amount", 0.0);
        amount_editText.setText(String.valueOf(Math.abs(amount)));
    }

    public void setAccount() {
        account = intent.getStringExtra("account");
        account_spinner.setSelection(spinnerAccountArrayAdapter.getPosition(account));
    }

    public void setCategory() {
        category = intent.getStringExtra("category");
        category_spinner.setSelection(spinnerCategoryArrayAdapter.getPosition(category));
    }

    public void setDate() {
        date = intent.getStringExtra("date");
        date_button.setText(date);
    }

    public void setTime() {
        time = intent.getStringExtra("time");
        time_button.setText(time);
    }

    public void setDescription() {
        description = intent.getStringExtra("description");
        description_editText.setText(description);
    }

    public double getAmount() {
        amount = Double.parseDouble(String.valueOf(amount_editText.getText()));
        if(type.equals("Income")) {
            return amount;
        }
        else {
            return -amount;
        }
    }

    public String getAccount() {
        account = (String) account_spinner.getSelectedItem();
        return account;
    }

    public String getCategory() {
        category = (String) category_spinner.getSelectedItem();
        return category;
    }

    public String getDate() {
        date = (String) date_button.getText();
        return date;
    }

    public String  getTime() {
        time = (String) time_button.getText();
        return time;
    }

    public String getDescription() {
        description = String.valueOf(description_editText.getText());
        return description;
    }

    public void displayDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        date_button.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void displayTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hourStr = String.valueOf(selectedHour);
                        String minuteStr = String.valueOf(selectedMinute);
                        if(selectedHour < 10) {
                            hourStr = "0" + hourStr;
                        }
                        if(selectedMinute < 10) {
                            minuteStr = "0" + minuteStr;
                        }
                        time_button.setText( hourStr + ":" + minuteStr);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    public void updateMovement(String type, double amount, String account, String category, String date, String time, String description) {
        movementViewModel = new ViewModelProvider(UpdateMovement.this).get(MovementViewModel.class);
        Movement movement = new Movement(type, amount, account, category, date, time, description);
        movement.setId(intent.getIntExtra("id", 0));
        movementViewModel.update(movement);
    }
}