package scrumProjects;

import UserData.User;

import java.util.ArrayList;

public class Scrum {

    private static int id = 0;
    private User scrumMaster;
    private String productOwner;
    private Team team;
    private ArrayList<UserStory> productBacklog = new ArrayList<>();

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

    @Override
    public String toString() {
        return "Scrum Id:" + getId() + "\n " +
                "Scrummaster: " + scrumMaster.getName()+"\n"+
                "Prodcutownerr: " + productOwner + "\n" +
                "Team: " + "\n" +team.toString() + "\n" +
                "UserStorry backlog: " + productBacklog.toString();
    }
}
