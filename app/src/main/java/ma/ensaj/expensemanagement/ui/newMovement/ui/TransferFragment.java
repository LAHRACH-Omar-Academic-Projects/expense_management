package ma.ensaj.expensemanagement.ui.newMovement.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

import ma.ensaj.expensemanagement.R;
import ma.ensaj.expensemanagement.entity.Movement;
import ma.ensaj.expensemanagement.viewModel.MovementViewModel;

public class TransferFragment extends Fragment {
    private View root;

    private EditText amount_editText;
    private Spinner source_account_spinner;
    private Spinner destination_account_spinner;;
    private Button date_button;
    private Button time_button;
    private EditText description_editText;
    private Button save_button;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    private double amount;
    private String sourceAccount;
    private String destinationAccount;
    private String date;
    private String time;
    private String description;

    private MovementViewModel movementViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root =  inflater.inflate(R.layout.fragment_transfer, container, false);

        amount_editText = root.findViewById(R.id.amount);
        source_account_spinner = root.findViewById(R.id.source_account);
        destination_account_spinner = root.findViewById(R.id.destination_account);
        date_button = root.findViewById(R.id.date);
        time_button = root.findViewById(R.id.time);
        description_editText = root.findViewById(R.id.description);
        save_button = root.findViewById(R.id.save);

        initializeSourceAccountSpinner();
        initializeDestinationAccountSpinner();
        initializeDate();
        initializeTime();

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
                    String type1 = "Income";
                    String type2 = "Expense";
                    String category = "Transfer";
                    insertMovement(type2, -getAmount(), getSourceAccount(), category, getDate(), getTime(), getDescription());
                    insertMovement(type1, getAmount(), getDestinationAccount(), category, getDate(), getTime(), getDescription());
                    root.startAnimation(success());
                }
            }
        });

        return root;
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 0, 0, 20);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(2));
        return shake;
    }

    public Animation success() {
        Animation an = new RotateAnimation(360.0f, 0.0f, 0, 0);

        // Set the animation's parameters
        an.setDuration(300);               // duration in ms
        an.setRepeatCount(0);                // -1 = infinite repeated
        an.setRepeatMode(Animation.REVERSE); // reverses each repeat
        an.setFillAfter(true);
        return an;
    }

    public void initializeSourceAccountSpinner() {
        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.accounts_array, R.layout.simple_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        source_account_spinner.setAdapter(spinnerArrayAdapter);
    }

    public void initializeDestinationAccountSpinner() {
        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(
                getContext(), R.array.accounts_array, R.layout.simple_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        destination_account_spinner.setAdapter(spinnerArrayAdapter);
    }

    public void initializeDate() {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        date_button.setText(mDay + "/"
                + (mMonth + 1) + "/" + mYear);
    }

    public void initializeTime() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String hourStr = String.valueOf(hour);
        String minuteStr = String.valueOf(minute);
        if(hour < 10) {
            hourStr = "0" + hourStr;
        }
        if(minute < 10) {
            minuteStr = "0" + minuteStr;
        }
        time_button.setText( hourStr + ":" + minuteStr);
    }

    public double getAmount() {
        amount = Double.parseDouble(String.valueOf(amount_editText.getText()));
        return amount;
    }

    public String getSourceAccount() {
        sourceAccount = (String) source_account_spinner.getSelectedItem();
        return sourceAccount;
    }

    public String getDestinationAccount() {
        destinationAccount = (String) destination_account_spinner.getSelectedItem();
        return destinationAccount;
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

    public void insertMovement(String type, double amount, String sourceAccount, String destinationAccount, String date, String time, String description) {
        movementViewModel = new ViewModelProvider(this).get(MovementViewModel.class);
        Movement movement = new Movement(type, amount, sourceAccount, destinationAccount, date, time, description);
        movementViewModel.insert(movement);
    }

    public void displayDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(getContext(),
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
        timePickerDialog = new TimePickerDialog(getContext(),
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
}