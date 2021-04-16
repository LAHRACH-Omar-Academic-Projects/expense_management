package ma.ensaj.expensemanagement.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ma.ensaj.expensemanagement.entity.Movement;

@Dao
public interface MovementDao {
    @Insert
    void insert(Movement movement);

    @Update
    void update(Movement movement);

    @Delete
    void delete(Movement movement);

    @Query("SELECT * FROM movement_table WHERE date like :date AND account IN (:accounts) AND type IN (:movementTypes) AND category IN (:categories) ORDER BY id ASC")
    LiveData<List<Movement>> getMovementsAsc(String date, List<String> accounts, List<String> movementTypes, List<String> categories);

    @Query("SELECT * FROM movement_table WHERE date like :date AND account IN (:accounts) AND type IN (:movementTypes) AND category IN (:categories) ORDER BY id DESC")
    LiveData<List<Movement>> getMovementsDesc(String date, List<String> accounts, List<String> movementTypes, List<String> categories);

    @Query("SELECT DISTINCT date FROM movement_table")
    LiveData<List<String>> getDateList();

    @Query("SELECT * FROM movement_table WHERE account IN (:accounts) AND (type LIKE :key OR account LIKE :key OR amount LIKE :key OR category LIKE :key OR date LIKE :key OR time LIKE :key OR description LIKE :key)")
    LiveData<List<Movement>> getMovements(String key, List<String> accounts);

    @Query("SELECT * FROM movement_table WHERE account IN (:accounts) ORDER BY id DESC LIMIT 10")
    LiveData<List<Movement>> getLast10MovementsByAccount(List<String> accounts);

    @Query("SELECT SUM(amount) FROM movement_table WHERE date like :date AND account IN (:accounts) AND type IN (:movementTypes) AND category IN (:categories)")
    LiveData<Double> getSumAmount(String date, List<String> accounts, List<String> movementTypes, List<String> categories);

    @Query("SELECT SUM(amount) FROM movement_table WHERE account IN (:accounts) AND (type LIKE :key OR account LIKE :key OR amount LIKE :key OR category LIKE :key OR date LIKE :key OR time LIKE :key OR description LIKE :key)")
    LiveData<Double> getSumAmountByKeyWord(String key, List<String> accounts);

    @Query("SELECT SUM(amount) FROM movement_table WHERE account IN (:accounts) AND type = :movementTypes AND category != 'Transfer' GROUP BY :date")
    LiveData<Double> getSumAmountByDate(List<String> accounts, String movementTypes, String date);
}
