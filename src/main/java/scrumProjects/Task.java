package scrumProjects;

import UserData.User;

public class Task {

    private Status status;
    private User assignedMember;
    private String todoTask;

    public Task(String task){
        this.todoTask = task;
        this.status = Status.todo;
    }

    public static String Help() {
        return "To create a task we need to give the task a: \n" +
               "Text explaing what the task is. We can assign an member later.";
    }


    public void setAssignedMember(User assignedMember) {
        this.assignedMember = assignedMember;
        this.status = Status.doing;
    }

    public void setDone(){
        this.status = Status.done;
    }

    public User getAssignedMember() {
        return assignedMember;
    }

    public Status getStatus() {
        return status;
    }

    public String getTodoTask() {
        return todoTask;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTodoTask(String todoTask) {
        this.todoTask = todoTask;
    }

    @Override
    public String toString() {
        return "\n **Status: **" + this.status + "\n" +
                "**Task: **" + this.todoTask + "\n" +
                "**Assigned member**" + this.assignedMember;
    }
}
