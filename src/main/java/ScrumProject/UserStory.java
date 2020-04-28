package ScrumProject;

import java.util.ArrayList;

public class UserStory {
    public int priority;
    public String story;
    private ArrayList<Task> tasks = new ArrayList<>();

    public UserStory(int priority, String story){
        this.priority = priority;
        this.story = story;
    }

    public static String Help() {
        return "To create a Userstory you will need to create a task and give it a priority and a text which explains the task. \n";
    }

    public void addTask(Task t){
        this.tasks.add(t);
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
                "\n **Story: **" + this.story +
                "\n **Tasks: **" + this.tasks;
    }
}
