import java.util.*;
import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry;

public class MasterServerImpl implements UtilsServer {
    List<UtilsClass> nodeList;
    List<String> nodeNames;
    Registry registry;

    public MasterServerImpl() {
        System.out.printf("Succes in master");
        try{
            registry = LocateRegistry.getRegistry(null);
            // Looking up the registry for the remote object 
            UtilsClass stub1 = (UtilsClass) registry.lookup("Dream11-node1"); 
            UtilsClass stub2 = (UtilsClass) registry.lookup("Dream11-node2"); 
            UtilsClass stub3 = (UtilsClass) registry.lookup("Dream11-node3"); 
            
            List<UtilsClass> list = new ArrayList<>();
            List<String> names = new ArrayList<>();
            list.add(stub1);
            names.add("Dream11-node1");
            list.add(stub2);
            names.add("Dream11-node2");
            list.add(stub3);
            names.add("Dream11-node3");
            nodeList = list;
            nodeNames = names;
        }
        catch (Exception e) { 
            System.err.println("Master exception: " + e.toString()); 
            e.printStackTrace(); 
         }
    }

    public void AddNode(String nodeName) {
        int index = nodeNames.indexOf(nodeName);
        if (index >= 0) {
            System.out.println("Node already exists!");
        }
        else {
            try {
                nodeNames.add(nodeName);
                UtilsClass stub = (UtilsClass) registry.lookup(nodeName);
                nodeList.add(stub);
                System.out.println("Node " + nodeName + " added!");
            } catch (Exception e) {
                System.err.println("Error occured while adding: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public void DeleteNode(String nodeName) {
        int index = nodeNames.indexOf(nodeName);
        try {
            if(index >= 0) {
                nodeNames.remove(index);
                nodeList.remove(index);
            }
            System.out.println("Node " + nodeName + " removed!");
        } catch (Exception e) {
            System.err.println("Error occured while deleting: " + e.toString());
            e.printStackTrace();
        }
    }

    public String Query(String query) {
        try{
            System.out.println(query);
            String[] splited = query.split(" ");
            UtilsClass stub = nodeList.get(0); // here stub will depend on the user id, for now just using the first node
            if (splited[0].equals("add_match")) {
                try{
                    String playerNames = splited[2];
                    for (int i = 3; i < splited.length; i++) {
                        playerNames = playerNames + " " + splited[i];
                    }
                    
                    int matchID = nodeList.get(0).AddMatch(Integer.parseInt(splited[1]), playerNames, true);
                    System.out.printf("Match ID %d\n", matchID);
                    for(int i=1; i<nodeList.size(); i++) 
                        System.out.printf("Match ID %d\n", nodeList.get(i).AddMatch(Integer.parseInt(splited[1]), playerNames, false));
                   
                    return "Match ID " + matchID + " created!";
                } catch (Exception ee) {
                    System.err.println("Client exception: " + ee.toString()); 
                    ee.printStackTrace();
                    return "Invalid syntax: Try: add_match <num players per team> <all player names>";
                }
            } 
            else if (splited[0].equals("update_player_score")) {
                try {
                    nodeList.get(0).UpdatePlayerScore(Integer.parseInt(splited[1]), Integer.parseInt(splited[2]), Integer.parseInt(splited[3]), true);
                    for(int i=1; i<nodeList.size(); i++)
                        nodeList.get(i).UpdatePlayerScore(Integer.parseInt(splited[1]), Integer.parseInt(splited[2]), Integer.parseInt(splited[3]), false);
                    
                    return "Player " + splited[2] + " score increased by " + splited[3];
                } catch (Exception ee) {
                    System.err.println("Client exception: " + ee.toString()); 
                    ee.printStackTrace();
                    return "Invalid syntax: Try: update_player_score <match id> <player id> <score>";
                }
            } 
            else if (splited[0].equals("create_team")) {
                try{
                    int[] playerIds = new int[splited.length - 2];
                    for (int i = 0; i < splited.length - 2; i++) {
                        playerIds[i] = Integer.parseInt(splited[i + 2]);
                    }
                    
                    int userid = nodeList.get(0).UserCreateTeam(Integer.parseInt(splited[1]), playerIds, true);
                    // System.out.printf("Your generated userId is %d\n", userid);
                    // for(int i=1; i<nodeList.size(); i++)
                    //     System.out.printf("Your generated userId is %d\n", nodeList.get(i).UserCreateTeam(Integer.parseInt(splited[1]), playerIds, false));
                                    
                    return "Team created! Your userID is " + userid;
                } catch (Exception ee) {
                    System.err.println("Client exception: " + ee.toString()); 
                    ee.printStackTrace();
                    return "Invalid syntax: Try: create_team <match id> <player ids>";
                }
            } 
            else if (splited[0].equals("get_score")) {
                try{
                    int userId = Integer.parseInt(splited[2]);
                    stub = nodeList.get(userId%nodeList.size());
                    int score = stub.GetScore(Integer.parseInt(splited[1]), userId);
                    // System.out.printf("Your score with userId %d is %d\n", userId, score);
                    return "Your score with userId " + userId + " is " + score;
                } catch (Exception ee) {
                    System.err.println("Client exception: " + ee.toString()); 
                    ee.printStackTrace();
                    return "Invalid syntax: Try: get_score <match id> <user id>";
                }
            } 
            else if (splited[0].equals("get_rank")) {
                try{
                    int userId = Integer.parseInt(splited[2]);
                    stub = nodeList.get(userId%nodeList.size());
                    int rank = stub.GetRank(Integer.parseInt(splited[1]), userId);
                    // System.out.printf("Your rank with userId %d is %d\n", userId, rank);
                    return "Your rank with userId " + userId + " is " + rank;
                } catch (Exception ee) {
                    System.err.println("Client exception: " + ee.toString()); 
                    ee.printStackTrace();
                    return "Invalid syntax: Try: get_rank <match id> <user id>";
                }
            } 
            else if (splited[0].equals("get_leaderboard")) {
                try {
                    return stub.GetLeaderBoard(Integer.parseInt(splited[1]));
                } catch (Exception ee) {
                    System.err.println("Client exception: " + ee.toString()); 
                    ee.printStackTrace();
                    return "Invalid syntax: Try: get_leaderboard <match id>";
                }
            } 
            else if (splited[0].equals("start_match")) {
                try{
                    for(UtilsClass node:nodeList)
                        node.startMatch(Integer.parseInt(splited[1]));
                    return "Match " + splited[1] + " started";
                } catch (Exception ee) {
                    System.err.println("Client exception: " + ee.toString()); 
                    ee.printStackTrace();
                    return "Invalid syntax: Try: start_match <match id>";
                }
            } 
            else if (splited[0].equals("show_players")) {
                try{
                    return stub.ShowPlayers(Integer.parseInt(splited[1]));
                } catch (Exception ee) {
                    System.err.println("Client exception: " + ee.toString()); 
                    ee.printStackTrace();
                    return "Invalid syntax: Try: show_players <match id>";
                }
            } 
            else if (splited[0].equals("exit")) {
                try{
                    nodeList.get(0).DeleteMatch(Integer.parseInt(splited[1]));
                    return "Match Ended";
                } catch (Exception ee) {
                    System.err.println("Client exception: " + ee.toString()); 
                    ee.printStackTrace();
                    return "Invalid syntax: Try: exit <match id>";
                }
            }
            else {
                return "Please provide a valid input";
            }
        }
        catch (Exception e) {
            System.err.println("Client exception: " + e.toString()); 
            e.printStackTrace(); 
            return "A server error occured! Please Try again";
         }
    }
}