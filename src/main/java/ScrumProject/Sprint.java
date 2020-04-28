package ScrumProject;

import java.util.ArrayList;
import java.util.Date;

public class Sprint {
    private Date startDate;
    private Date endDate;
    private ArrayList<Task> tasks;

    public Sprint(Date startDate, Date endDate, ArrayList<Task> tasks){
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = tasks;
    }

    public static String Help() {
        return "To create an Sprint we need a start date in the format of: dd-MM-yyyy and an end date with the same format \n";
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
