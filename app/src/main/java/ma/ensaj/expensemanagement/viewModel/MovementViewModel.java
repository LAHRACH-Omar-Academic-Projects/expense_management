package ma.ensaj.expensemanagement.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.List;

import ma.ensaj.expensemanagement.entity.Movement;
import ma.ensaj.expensemanagement.repository.MovementRepository;

public class MovementViewModel extends AndroidViewModel {
    private MovementRepository repository;
    private LiveData<List<Movement>> movements;
    private LiveData<List<String>> dateList;
    private LiveData<Double> sumAmount;
    private LiveData<Double> sumAmountByDate;

    public MovementViewModel(@NonNull Application application) {
        super(application);
        repository = new MovementRepository(application);
    }

    public void insert(Movement movement) {
        repository.insert(movement);
    }

    public void update(Movement movement) {
        repository.update(movement);
    }

    public void delete(Movement movement) {
        repository.delete(movement);
    }

    public LiveData<List<Movement>> getMovements(String sortType, String date, List<String> accounts, List<String> movementTypes, List<String> categories) {
        movements = repository.getMovements(sortType, date, accounts, movementTypes, categories);
        return movements;
    }

    public LiveData<List<String>> getDateList() {
        dateList = repository.getDateList();
        return dateList;
    }

    public LiveData<List<Movement>> getMovements(String key, List<String> accounts) {
        movements = repository.getMovements(key, accounts);
        return movements;
    }

    public LiveData<List<Movement>> getLast10MovementsByAccount(List<String> accounts) {
        movements = repository.getLast10MovementsByAccount(accounts);
        return movements;
    }

    public LiveData<Double> getSumAmount(String date, List<String> accounts, List<String> movementTypes, List<String> categories) {
        sumAmount = repository.getSumAmount(date, accounts, movementTypes, categories);
        return sumAmount;
    }

    public LiveData<Double> getSumAmountByKeyWord(String key, List<String> accounts) {
        sumAmount = repository.getSumAmountByKeyWord(key, accounts);
        return sumAmount;
    }

    public LiveData<Double> getSumAmountByDate(List<String> accounts, String movementTypes, String date) {
        sumAmountByDate = repository.getSumAmountByDate(accounts, movementTypes, date);
        return sumAmountByDate;
    }
}
