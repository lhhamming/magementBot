package scrumProjects;

import UserData.User;

import java.util.ArrayList;

public class Team {
    private String teamName;
    private ArrayList<User> teamMembers = new ArrayList<>();

    public Team(String teamName) {
        this.teamName = teamName;
    }

    public void addUserToTeam(User u){
        teamMembers.add(u);
    }

    public ArrayList<User> getTeamMembers() {
        return teamMembers;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public String toString() {
        String teamList = "";
        for (User u : teamMembers){
            teamList = teamList += "\t ->" + u.getName() + "\n";
        }
        return teamList;
    }
}
