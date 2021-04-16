package ma.ensaj.expensemanagement.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movement_table")
public class Movement {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String type;

    private double amount;

    private String account;

    private String category;

    private String date;

    private String time;

    private String description;

    public Movement(String type, double amount, String account, String category, String date, String time, String description) {
        this.type = type;
        this.amount = amount;
        this.account = account;
        this.category = category;
        this.date = date;
        this.time = time;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Movement{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", account='" + account + '\'' +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
