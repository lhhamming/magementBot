import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import scrumProjects.Scrum;
import scrumProjects.Task;
import scrumProjects.Team;
import scrumProjects.UserStory;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends ListenerAdapter {

    private MessageReceivedEvent event;

    public static void main(String[] args) throws LoginException, FileNotFoundException {
        JDABuilder.createDefault(getBotKey())
                .addEventListeners(new Main())
                .build();
    }

    private static String getBotKey() throws FileNotFoundException {
        String key = "";
        String filePath = new File(System.getProperty("user.dir")).getParent();
        File botkey = new File(filePath + File.separator + "extras" + File.separator + "bot_info.txt");
        Scanner fileScanner = new Scanner(botkey);
        while(fileScanner.hasNextLine()){
            String[] separatedstring = fileScanner.nextLine().split(" ");
            key = separatedstring[2];
        }
        return key;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()){
            return;
        }
        this.event = event;
        System.out.println("We hebben een bericht gekregen van: " + event.getAuthor().getName() + " Id: " + event.getAuthor().getId() + "\n"
           +  " : " + event.getMessage().getContentDisplay());
        String commandString = event.getMessage().getContentRaw();
        if(checkCommand(commandString)){
            checkCommandList(commandString);
        }
    }

    private boolean checkCommand(String command) {
        //lets make our bot respond to commands that contain a ; in the beginning
        String[] splittedCommandString = command.split("");
        if(splittedCommandString[0].equals(";")){
            //Its our command!
            return true;
        }
        return false;
    }

    private void checkCommandList(String command) {
        String completeCommand = command;
        boolean isMultiCommand = isMultiCommand(command);
        //Conver to lower case
        if(isMultiCommand){
            String[] multiCommand = command.split(" ");
            command = multiCommand[0];
        }
        command = command.toLowerCase().trim();
        String uId = event.getAuthor().getId();
        Long userId = Long.parseLong(uId);
        switch(command){
            case ";ping":
                sendMessage("Pong!");
                break;

            case ";scrum":
                if(!DataProvider.getInstance().userExists(userId)){
                    DataProvider.getInstance().createUser(event.getAuthor().getName(), userId);
                }
                if(isMultiCommand){
                    scrumCommands(completeCommand);
                }
                break;

            default:
                sendMessage("Not a command!");
                break;
        }
    }

    private boolean isMultiCommand(String command) {
        String[] splittedCommand = command.split( " ");
        if(splittedCommand.length > 1){
            return true;
        }
        return false;
    }

    private void scrumCommands(String command) {
        String[] splittedCommand = command.split(" ");
        String firstMod = splittedCommand[1].trim();
        UserData.User user = DataProvider.getInstance().getUser(event.getAuthor().getIdLong());
        switch(firstMod){
            case "create":
                if(splittedCommand[2].trim().equalsIgnoreCase("help")){
                    createHelpEmbed("Scrumproject create help", "Usage of the command is as follows: \n" + ";scrum create @[user] ProductOwnerName teamName \n " +
                            "So it would ga as follows: ;scrum create @lhhamming Luuk Hamming team_1");
                }else{
                    User Scrummaster = event.getMessage().getMentionedUsers().get(0);
                    Team t = DataProvider.getInstance().getTeam(splittedCommand[4]);
                    if(t == null){
                        createErrorEmbed("There is no team with such a name.");
                    }else{
                        Scrum scrumpoject = DataProvider.getInstance().createScrum(Scrummaster.getIdLong(),splittedCommand[3], splittedCommand[4]);
                        createSuccesEmbed("Created Scrumteam", "Succes!", "Scrummaster: " + Scrummaster.getName() +
                                " With the product owner: " + splittedCommand[3] + " Assigned Team: " + splittedCommand[4]);
                        user.setCurrentProject(scrumpoject);
                    }
                }
                break;

            case "delete":
                //TODO: create an deletion method
                break;

            case "show":
                sendMessage(DataProvider.getInstance().getScrumProjects().build());
                break;

            case "addUserToTeam":
                if(splittedCommand[2].equalsIgnoreCase("help")){
                    createHelpEmbed("How to add users to a team!", "To add a user or users to a team you would need to do the following: \n" +
                            ";scrum addUserToTeam [teamName] [@User]. Example: \n" +
                            ";scrum addUserToTeam team @lhhamming");
                }
                if(DataProvider.getInstance().teamExists(splittedCommand[2])){
                    Team t = DataProvider.getInstance().getTeam(splittedCommand[2]);
                    if(splittedCommand.length > 4){
                        ArrayList<UserData.User> addedUsers;
                        addedUsers = addMultipleUsers(splittedCommand,t);
                        String addedUserStrings = "";
                        for(UserData.User u : addedUsers){
                            addedUserStrings = addedUserStrings + u.getName() + "\n";
                        }
                        createSuccesEmbed("Success","Added users.", addedUserStrings);
                        } else{
                        User member = event.getMessage().getMentionedUsers().get(0);
                            if(DataProvider.getInstance().userExists(member.getIdLong())){
                                //the member exists lets add him to a team
                            }else{
                                //Add the user to the collection
                                DataProvider.getInstance().createUser(member.getName(), member.getIdLong());

                            }
                        UserData.User managementBotUser = DataProvider.getInstance().getUser(member.getIdLong());
                        managementBotUser.addToTeam(t);
                        DataProvider.getInstance().addMemberToTeam(t.getTeamName(),managementBotUser.getId());
                        createSuccesEmbed("Succes!","Added user.", managementBotUser.getName());
                    }
                    }else{
                        sendMessage("Team doesnt exist!");
                    }
                break;


            case "createTeam":
                String teamName = splittedCommand[2];
                System.out.println("Team name: " + teamName);
                DataProvider.getInstance().createTeam(teamName);
                createSuccesEmbed("Succes!", "Team creation","Team creation of team: " + teamName + " is successfull!");
            break;

            case "getTeams":
                sendMessage(DataProvider.getInstance().getTeams().build());
                break;

            case "currentproject":
                if(userHasProject(user)){
                    sendMessage(user.getCurrentProject().toString());
                }else{
                    sendMessage("you dont have a current project! Ask a scrum master to add you to one of their projects. \n Or create your own!");
                }
                break;

            case "addUserstory":
                if(splittedCommand[2].trim().equalsIgnoreCase("help")){
                    createHelpEmbed("How to add a Userstory", "To create a user story all you need to type is:\n" +
                            ";scrum addUserstory [priority in a number such as 1 leave it blank to auto assign] [Write down the task that needs to happen] \n" +
                            ";scrum addUserstory 1 This is a task");
                }else{
                    if(userHasProject(user)){
                        Scrum userProject = user.getCurrentProject();
                        String userStoryPrionumberString = splittedCommand[2].trim();
                        int userStoryPrionumber =0;
                        boolean parsed = true;
                        try{
                            userStoryPrionumber = Integer.parseInt(userStoryPrionumberString);
                        }catch (Exception e){
                            parsed = false;
                            System.out.println(e);
                        }
                        String userStoryTask = getSentenceFromStringArray(splittedCommand,3);
                        UserStory userStory;
                        if(parsed){
                            /*If the userstory prioritynumber has already been selected (Such as 1)
                              We are going to make every other Userstory priority number shift downwards one
                              This is so we create an correct order of the userstorys
                            */
                            ArrayList<UserStory> userStories = user.getCurrentProject().getProductBacklog();
                            boolean shiftUserStoryNumbers = userStoryNumberExists(userStoryPrionumber, userStories);
                            if(shiftUserStoryNumbers){
                                shiftUserStories(userStoryPrionumber,userStories);
                            }
                            userStory= new UserStory(userStoryPrionumber, userStoryTask);
                        }else{
                            //The userstory doesnt have a priority number
                            userStory = new UserStory(userProject.getProductBacklog().size() + 1, userStoryTask);
                        }
                        userProject.getProductBacklog().add(userStory);
                        createSuccesEmbed("User story creation", "User story created","The user story was created succesfully!");
                    }else{
                        sendMessage("You dont have a scrumproject you are currently working on");
                    }
                }
                break;

            case "addTask":
                if(splittedCommand[2].equalsIgnoreCase("help")){
                    createHelpEmbed("Adding a task",
                                    "To create a task you would enter the following: \n" +
                                            ";scrum addTask [UserStory by weight] [Text for the task] \n" +
                                            "So it would go as follows: ;scrum addTask 1 Creating a Login screen");
                }else{
                    if(userHasProject(user) && userProjectHasUserStory(user)){
                        int selectedUserStory = 0;
                        //TODO: Crate an method for parsing and giving back the parsed integer (With an boolean)
                        boolean parsed = false;
                        try{
                            selectedUserStory = Integer.parseInt(splittedCommand[2]);
                            parsed = true;
                        }catch (Exception e){};
                        if(parsed){
                            String todoTaskString = getSentenceFromStringArray(splittedCommand, 3);
                            Task t = new Task(todoTaskString);
                            user.getCurrentProject().getProductBacklog().get(selectedUserStory - 1).addTask(t);
                            createSuccesEmbed("Task creaction", "Task create","The task was added succesfully!");
                        }
                    }else{
                        sendMessage("Please create a project or create an user story! \n " +
                                    ";scrum create help or ;scrum addUserstory help");
                    }
                }
                break;

            case "assignUserstory":
                if(splittedCommand[2].equalsIgnoreCase("help")){
                    createHelpEmbed("Assign a user story", "To assign a user story to a user you would need to do the following: \n" +
                            ";scrum assignUserStory [Priority number from userstory]\n" +
                            "So use it as such: ;scrum asssignUserstory 1");
                }else{
                    if(userHasProject(user)){
                        if(user.getCurrentProject().getProductBacklog().size() > 0){
                            //Goes to the current project.
                            int selectedUserStory = 0;
                            boolean parsed = false;
                            try{
                                selectedUserStory = Integer.parseInt(splittedCommand[2]) - 1;
                                parsed = true;
                            }catch (Exception e){};
                            if(parsed){
                                sendMessage("Select a task");
                                String toSendMessage = "";
                                int iterator = 0;
                                for(Task t : user.getCurrentProject().getProductBacklog().get(selectedUserStory).getTasks()){
                                    iterator++;
                                    toSendMessage = toSendMessage + "Task: " + iterator + "\n **Task: **" +t.getTodoTask() + "\n **Assigned member: **" + t.getAssignedMember() + "\n";
                                }
                                toSendMessage = toSendMessage + "\n Please make a selection like so: \n ;scrum assignTask @[Username] [Task number]";
                                createContinueMessage(user.getCurrentProject().getId(),user.getId(),"Assigning a task","Task assigning",toSendMessage);
                            }else{
                                sendMessage("Please type in a number!");
                            }
                        }else{
                            sendMessage("You need to create a user story first");
                        }
                    }else{
                        sendMessage("You need to create a project first!");
                    }
                }
                break;


            case "assignTask":
                //Filter through all messages to find the one that was send by the user
                Message selectedTaskMessage = findUserstoryMessage(user);
                if(selectedTaskMessage != null){
                    int selectedTask = 0;
                    boolean parsed = false;
                    try{
                        selectedTask = Integer.parseInt(splittedCommand[3]);
                        parsed = true;
                    }catch (Exception e){};
                    if(parsed){
                        //selectTask is the integer that the user chooses from the list of tasks.
                        int selectedTaskId = getTaskFromEmbedMessage(selectedTaskMessage, selectedTask) - 1;
                        int scrumId = getScrumIdFromEmbedMessage(selectedTaskMessage) - 1;
                        user.getCurrentProject().getProductBacklog().get(scrumId)
                                .getTasks()
                                .get(selectedTaskId)
                                .setAssignedMember(DataProvider.getInstance().getUser(event.getMessage().
                                                   getMentionedUsers().get(0).getIdLong()));

                        createSuccesEmbed("Assigned succesfull!", "Assigning a task", "The task was assigned to the user succesfully!");

                    }else{
                        createErrorEmbed("Please user numbers! such as: ;scrum selectTask @[Username] 1");
                    }
                }else{
                    createErrorEmbed("No message was found with your usedId and task.");
                }

                break;

            case "getUserStorys":
                String toSendMessage = "";
                if(user.getCurrentProject().getProductBacklog().size() > 0){
                    for (UserStory s : user.getCurrentProject().getProductBacklog()){
                        //now we loop through the tasks for this user story.
                        toSendMessage = toSendMessage + "User story with priotity: " + s.getPriority() + " \n Has the following tasks: \n";
                        for (Task t : s.getTasks()){
                            toSendMessage = toSendMessage + "**Todo: **" + t.getTodoTask() + "\n **Assigned member: ** " + t.getAssignedMember() + "\n**Status: **" + t.getStatus();
                        }
                        toSendMessage = toSendMessage + "\n -----------------------------------------------------\n";
                    }
                    sendMessage(toSendMessage);
                }else{
                    sendMessage("You dont have any userstory's!");
                }
                break;
            case "help":
                createHelpEmbed();
                break;
        }
    }

    private String getSentenceFromStringArray(String[] sentenceArray, int startingInt) {
        String retVal = "";
        for (int i = startingInt; i < sentenceArray.length; i++) {
            retVal = retVal + sentenceArray[i] + " ";
        }

        return retVal;
    }

    private boolean userProjectHasUserStory(UserData.User user) {
        if(user.getCurrentProject().getProductBacklog().size() > 0){
            return true;
        }
        return false;
    }

    private int getScrumIdFromEmbedMessage(Message selectedTaskMessage) {
        int retVal = -1;
        for (MessageEmbed m : selectedTaskMessage.getEmbeds()){
            String[] splittedEmbedDescription = m.getDescription().split(" ");
            int scrumId = Integer.parseInt(splittedEmbedDescription[2]);
            return scrumId;
        }
        return retVal;
    }

    private Message findUserstoryMessage(UserData.User user) {
        Message retVal = null;
        //Get all messages from the server
        MessageHistory history = new MessageHistory(event.getTextChannel());
        for (int j = 0; j < history.retrievePast(10).complete().size(); j++) {
            Message m = history.getRetrievedHistory().get(j);
            if(m.getEmbeds().size() > 0){
                //the message is embedded
                for (MessageEmbed embed : m.getEmbeds()){
                    if(embed.getDescription().length() > 0){
                        String[] splittedEmbedDescription = embed.getDescription().split(" ");
                        if(Long.parseLong(splittedEmbedDescription[6]) == user.getId()){
                            //It is the correct message
                            return m;
                        }
                    }
                }
            }
        }
        return retVal;
    }

    private int getTaskFromEmbedMessage(Message selectedTaskMessage, int selectedTaskNumber) {
        int retVal = -1;
        for (MessageEmbed embed : selectedTaskMessage.getEmbeds()){
            for (MessageEmbed.Field field : embed.getFields()){
                System.out.println("Field values: " + field.getValue());
                String[] splittedField = field.getValue().split("\n");
                for (int i = 0; i < splittedField.length; i++) {
                    if(splittedField[i].trim().equalsIgnoreCase("task: " +selectedTaskNumber)){
                        retVal = selectedTaskNumber;
                    }
                }
                System.out.println("selected task number: " + retVal);
                if(selectedTaskNumber == retVal){
                    return retVal;
                }
            }
        }
        return retVal;
    }


    private void shiftUserStories(int userStoryPrionumber, ArrayList<UserStory> userStories) {
        boolean shiftingPointFound = false;
        for (UserStory u : userStories){
            if(u.priority == userStoryPrionumber || shiftingPointFound){
                shiftingPointFound = true;
                u.priority = u.priority + 1;
            }
        }
    }

    private boolean userStoryNumberExists(int userStoryPrionumber, ArrayList<UserStory> productBacklog) {
        for (UserStory u : productBacklog){
            if(u.priority == userStoryPrionumber){
                return true;
            }
        }
        return false;
    }

    private void createErrorEmbed(String lineValue) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Error!");
        eb.setColor(Color.RED);
        eb.addField("Whoops!", lineValue, false);
        sendMessage(eb.build());
    }

    private boolean userHasProject(UserData.User user) {
        if(user.getCurrentProject() != null){
            return true;
        }else{
            return false;
        }
    }

    private ArrayList<UserData.User> addMultipleUsers(String[] splittedCommand ,Team toAddToTeam) {
        ArrayList<UserData.User> returnList = new ArrayList<>();
        int iteration = 0;
        for (int i = 3; i < splittedCommand.length ; i++) {
            User member = event.getMessage().getMentionedUsers().get(iteration);
            if(!DataProvider.getInstance().userExists(Long.parseLong(member.getId()))) {
                DataProvider.getInstance().createUser(member.getName(), member.getIdLong());
            }
            UserData.User managementBotUser = DataProvider.getInstance().getUser(member.getIdLong());
            managementBotUser.addToTeam(toAddToTeam);
            DataProvider.getInstance().addMemberToTeam(toAddToTeam.getTeamName(),managementBotUser.getId());
            returnList.add(managementBotUser);
            iteration++;
        }
        return returnList;
    }

    private void createHelpEmbed(String Title, String LineValue) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(Title);
        eb.setColor(Color.BLUE);
        eb.addField("Usage", LineValue, false);
        sendMessage(eb.build());
    }

    private void createSuccesEmbed(String title, String LineName, String LineValue) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title, null);
        eb.setColor(Color.GREEN);
        eb.addField(LineName, LineValue ,false);
        sendMessage(eb.build());
    }

    private void createHelpEmbed() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Scrum help", null);
        eb.setColor(Color.RED);
        eb.setDescription("How to create a scrum project!");
        eb.addField("creation", "for a scrum project you need to atleast have \n Scrum master (A user), A product Owner (Also a user) and a team of users", false);
        sendMessage(eb.build());
    }

    private void createContinueMessage(int id, Long userId,String title,String lineName,String lineValue) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(title, null);
        eb.setColor(Color.YELLOW);
        eb.setDescription("Scrum Id: " + id + " Send by userId: " + userId);
        eb.addField(lineName,lineValue,false);
        sendMessage(eb.build());
    }

    private void sendMessage(String toSendMessage) {
        event.getChannel().sendMessage(toSendMessage).queue();
    }

    private void sendMessage(MessageEmbed embedMessage){
        event.getChannel().sendMessage(embedMessage).queue();
    }
}
