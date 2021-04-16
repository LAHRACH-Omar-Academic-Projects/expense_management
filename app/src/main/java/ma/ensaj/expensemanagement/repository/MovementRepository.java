package ma.ensaj.expensemanagement.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import java.util.List;

import ma.ensaj.expensemanagement.dao.MovementDao;
import ma.ensaj.expensemanagement.database.ExpenseManagementDatabase;
import ma.ensaj.expensemanagement.entity.Movement;

public class MovementRepository {
    private MovementDao movementDao;
    private LiveData<List<Movement>> movements;
    private LiveData<List<String>> dateList;
    private LiveData<Double> sumAmount;
    private LiveData<Double> sumAmountByDate;

    public MovementRepository(Application application) {
        ExpenseManagementDatabase expenseManagementDatabase = ExpenseManagementDatabase.getInstance(application);
        movementDao = expenseManagementDatabase.movementDao();
    }

    public void insert(Movement movement) {
        new MovementRepository.InsertMovementAsyncTask(movementDao).execute(movement);
    }

    public void update(Movement movement) {
        new MovementRepository.UpdateMovementAsyncTask(movementDao).execute(movement);
    }

    public void delete(Movement movement) {
        new MovementRepository.DeleteMovementAsyncTask(movementDao).execute(movement);
    }

    public LiveData<List<Movement>> getMovements(String sortType, String date, List<String> accounts, List<String> movementTypes, List<String> categories) {
        if(sortType.equals("ASC")) {
            movements = movementDao.getMovementsAsc(date, accounts, movementTypes, categories);
        }
        else {
            movements = movementDao.getMovementsDesc(date, accounts, movementTypes, categories);
        }
        return movements;
    }

    public LiveData<List<String>> getDateList() {
        dateList = movementDao.getDateList();
        return dateList;
    }

    public LiveData<List<Movement>> getMovements(String key, List<String> accounts) {
        movements = movementDao.getMovements(key, accounts);
        return movements;
    }

    public LiveData<List<Movement>> getLast10MovementsByAccount(List<String> accounts) {
        movements = movementDao.getLast10MovementsByAccount(accounts);
        return movements;
    }

    public LiveData<Double> getSumAmount(String date, List<String> accounts, List<String> movementTypes, List<String> categories) {
        sumAmount = movementDao.getSumAmount(date, accounts, movementTypes, categories);
        return sumAmount;
    }

    public LiveData<Double> getSumAmountByKeyWord(String key, List<String> accounts) {
        sumAmount = movementDao.getSumAmountByKeyWord(key, accounts);
        return sumAmount;
    }

    public LiveData<Double> getSumAmountByDate(List<String> accounts, String movementTypes, String date) {
        sumAmountByDate = movementDao.getSumAmountByDate(accounts, movementTypes, date);
        return sumAmountByDate;
    }

    public static class InsertMovementAsyncTask extends AsyncTask<Movement, Void, Void> {
        private MovementDao movementDao;

        private InsertMovementAsyncTask(MovementDao movementDao) {
            this.movementDao = movementDao;
        }
        @Override
        protected Void doInBackground(Movement... movements) {
            movementDao.insert(movements[0]);
            return null;
        }
    }

    public static class UpdateMovementAsyncTask extends AsyncTask<Movement, Void, Void> {
        private MovementDao movementDao;

        private UpdateMovementAsyncTask(MovementDao movementDao) {
            this.movementDao = movementDao;
        }
        @Override
        protected Void doInBackground(Movement... movements) {
            movementDao.update(movements[0]);
            return null;
        }
    }

    public static class DeleteMovementAsyncTask extends AsyncTask<Movement, Void, Void> {
        private MovementDao movementDao;

        private DeleteMovementAsyncTask(MovementDao movementDao) {
            this.movementDao = movementDao;
        }
        @Override
        protected Void doInBackground(Movement... movements) {
            movementDao.delete(movements[0]);
            return null;
        }
    }
}
