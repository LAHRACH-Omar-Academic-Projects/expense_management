package ma.ensaj.expensemanagement.entity;

public class Report {
    private int id;
    private String date;
    private double incomeAmount;
    private double expenseAmount;
    private double currentSold;
    private String description;

    public Report(String date, double incomeAmount, double expenseAmount, double currentSold, String description) {
        this.date = date;
        this.incomeAmount = incomeAmount;
        this.expenseAmount = expenseAmount;
        this.currentSold = currentSold;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public double getCurrentSold() {
        return currentSold;
    }

    public void setCurrentSold(double currentSold) {
        this.currentSold = currentSold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", incomeAmount=" + incomeAmount +
                ", expenseAmount=" + expenseAmount +
                ", currentSold=" + currentSold +
                ", description='" + description + '\'' +
                '}';
    }
}
