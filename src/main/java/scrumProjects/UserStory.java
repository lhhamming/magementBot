package scrumProjects;

import java.util.ArrayList;

public class UserStory {
    public int priority;
    private ArrayList<Task> tasks = new ArrayList<>();

    public UserStory(int priority, String task){
        this.priority = priority;
        tasks.add(new Task(task));
    }

    public static String Help() {
        return "To create a Userstory you will need to create a task and give it a priority and a text which explains the task. \n";
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "\n **Priority: **" + this.priority +
                "\n **Tasks: **" + this.tasks;
    }
}
