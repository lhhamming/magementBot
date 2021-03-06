package ScrumProject;

import UserData.User;

import java.util.ArrayList;

public class Scrum {

    private static int id = 0;
    private User scrumMaster;
    private String productOwner;
    private Team team;
    private ArrayList<UserStory> productBacklog = new ArrayList<>();
    private ArrayList<Sprint> sprints = new ArrayList<>();

    public Scrum(User creator, String productOwner, Team team){
        this.scrumMaster = creator;
        this.productOwner = productOwner;
        this.team = team;
        this.id = id;
        id++;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setScrumMaster(User scrumMaster) {
        this.scrumMaster = scrumMaster;
    }

    public User getScrumMaster() {
        return scrumMaster;
    }

    public static int getId() {
        return id;
    }

    public ArrayList<UserStory> getProductBacklog() {
        return productBacklog;
    }

    public ArrayList<Sprint> getSprints() {
        return sprints;
    }

    @Override
    public String toString() {
        return "Scrum Id:" + getId() + "\n " +
                "Scrummaster: " + scrumMaster.getName()+"\n"+
                "Prodcutownerr: " + productOwner + "\n" +
                "Team: " + "\n" +team.toString() + "\n" +
                "UserStorry backlog: " + printBacklog();
    }

    private String printBacklog() {
        String[] backlog = new String[productBacklog.size()];
        String retVal = "";
        for (UserStory u : productBacklog){
            backlog[u.getPriority() - 1] = u.toString();
        }
        for (int i = 0; i < backlog.length; i++) {
            retVal = retVal + "\n" + backlog[i];
        }
        return retVal;
    }
}
