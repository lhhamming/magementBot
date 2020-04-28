import UserData.User;
import net.dv8tion.jda.api.EmbedBuilder;
import ScrumProject.Scrum;
import ScrumProject.Team;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DataProvider {
    private static DataProvider instance;

    //The Long is the user Id
    private HashMap<Long, User> userList;
    //The Long saves the id of a project
    private HashMap<Integer, Scrum> scrumProjects;
    private ArrayList<Team> teams = new ArrayList<>();

    /*
    Scrum is een framework
    Scrum is een manier om dingen te doen
    Agile scrum werken werk je dus met sprints

    Een scrum heeft een product backlog
    Een backlog bestaat uit user stories.
    De user stories hebben prioriteiten. De bovenste is het meest belangrijkst daarna de 2de user story etc.
    Deze kan ondertussen veranderd worden. Dit betekent dat de prioriteiten niet vast zijn.
    Elke scrum project heeft een product owner een scrum master en een team.
    Dit zijn ook de rollen.
    Met elk scrum project heb je sprints. in deze sprints worden user stories gebruikt met de hoogste prioriteit
    Elke user story heeft taken.
    Taken hebben de status: todo, doing , done.
    Taken kunnen ook geassigned worden aan iemand. zodra dit gedaan word word de took op doing gezet.
    Sprints hebben een start datum en een eind datum.
     */

    private DataProvider() {
        initialize();
    }

    private void initialize() {
        scrumProjects = new HashMap<>();
        userList = new HashMap<>();
    }

    public static DataProvider getInstance() {
        if (instance == null) {
            instance = new DataProvider();
        }
        return instance;
    }

    public boolean userExists(Long id){
        if(userList.containsKey(id)){
            return true;
        }else{
            return false;
        }
    }

    public void addMemberToTeam(String teamName, Long uId){
        User u = getUser(uId);
        Team t = getTeam(teamName);
        if(t == null){
            System.out.println("invalid team name");
        }else{
            for(Team team : teams){
                if(team.getTeamName().equalsIgnoreCase(teamName)){
                    team.addUserToTeam(u);
                }
            }
        }
    }

    public Scrum createScrum(Long userId, String productOwner, String teamName){
        Team t = getTeam(teamName);
        User u = getUser(userId);
        Scrum s = new Scrum(u,productOwner,t);
        scrumProjects.put(s.getId(), s);
        return s;
    }

    public void createTeam(String teamName){
        teams.add(new Team(teamName));
    }

    public Team getTeam(String teamName){
        for (Team t : teams){
            if(t.getTeamName().equalsIgnoreCase(teamName)){
                return t;
            }
        }
        return null;
    }

    public boolean teamExists(String teamName){
        for (Team t : teams){
            if(t.getTeamName().equalsIgnoreCase(teamName)){
                return true;
            }
        }
        return false;
    }

    public User getUser(Long id){
        return userList.get(id);
    }

    public void createUser(String name, Long userId) {
        User u = new User(userId,name);
        userList.put(userId, u);
    }


    public EmbedBuilder getTeams() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Teams");
        eb.setColor(Color.MAGENTA);
        for(Team t : teams){
            eb.addField("Team: " + t.getTeamName(), t.toString() , false);
        }


        return eb;
    }

    public EmbedBuilder getScrumProjects() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Scrum projects");
        eb.setColor(Color.MAGENTA);
        for (Scrum s : scrumProjects.values()){
            eb.addField("Scrumproject", s.toString(), false);
        }
        return eb;
    }
}
