package ma.ensaj.expensemanagement.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import ma.ensaj.expensemanagement.dao.MovementDao;
import ma.ensaj.expensemanagement.entity.Movement;

@Database(entities = {Movement.class}, version = 3)
public abstract class ExpenseManagementDatabase extends RoomDatabase {
    private static ExpenseManagementDatabase instance;

    public abstract MovementDao movementDao();

    public static synchronized ExpenseManagementDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ExpenseManagementDatabase.class, "expense_management_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private final MovementDao movementDao;

        private PopulateDbAsyncTask(ExpenseManagementDatabase db) {
            movementDao = db.movementDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
