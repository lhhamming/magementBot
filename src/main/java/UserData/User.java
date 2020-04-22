package UserData;

import net.dv8tion.jda.api.entities.MessageChannel;
import scrumProjects.Scrum;
import scrumProjects.Team;

import java.util.ArrayList;

public class User {
    private Long id;
    private String name;
    private Scrum currentProject;
    private ArrayList<MessageChannel> channels;
    private ArrayList<Team> inTeams = new ArrayList<>();

    public User(Long id, String name){
        this.id = id;
        this.name = name;
        this.currentProject = null;
        channels = new ArrayList<>();
    }


    public void setCurrentProject(Scrum currentProject) {
        this.currentProject = currentProject;
    }

    public Scrum getCurrentProject() {
        return currentProject;
    }

    public Long getId() {
        return id;
    }

    public void addToTeam(Team t){
        inTeams.add(t);
    }

    public String getName() {
        return name;
    }
}
