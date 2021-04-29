import java.util.*;
import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
public class MasterServerImpl implements UtilsServer {
    List<UtilsClass> nodeList;
    public MasterServerImpl() {
        System.out.printf("Succes in master");
        try{
            Registry registry = LocateRegistry.getRegistry(null);
            // Looking up the registry for the remote object 
            UtilsClass stub1 = (UtilsClass) registry.lookup("Dream11-node1"); 
            UtilsClass stub2 = (UtilsClass) registry.lookup("Dream11-node2"); 
            UtilsClass stub3 = (UtilsClass) registry.lookup("Dream11-node3"); 
            List<UtilsClass> list = new ArrayList<UtilsClass>();
            list.add(stub1);
            list.add(stub2);
            list.add(stub3);
            nodeList = list;
        }
        catch (Exception e) { 
            System.err.println("Master exception: " + e.toString()); 
            e.printStackTrace(); 
         }
    }

    public void Query(String query) {
        try{
            System.out.println(query);
            String[] splited = query.split(" ");
            UtilsClass stub = nodeList.get(0); // here stub will depend on the user id, for now just using the first node
            if (splited[0].equals("add_match")) {
                String playerNames = splited[2];
                for (int i = 3; i < splited.length; i++) {
                    playerNames = playerNames + " " + splited[i];
                }
                for(UtilsClass node:nodeList)
                    System.out.printf("Match ID %d\n", node.AddMatch(Integer.parseInt(splited[1]), playerNames));
            } 
            else if (splited[0].equals("update_player_score")) {
                for(UtilsClass node:nodeList)
                    node.UpdatePlayerScore(Integer.parseInt(splited[1]), Integer.parseInt(splited[2]), Integer.parseInt(splited[3]));
            } 
            else if (splited[0].equals("create_team")) {
                int[] playerIds = new int[splited.length - 2];
                for (int i = 0; i < splited.length - 2; i++) {
                    playerIds[i] = Integer.parseInt(splited[i + 2]);
                }
                for(UtilsClass node:nodeList)
                    System.out.printf("Your generated userId is %d\n", node.UserCreateTeam(Integer.parseInt(splited[1]), playerIds));
            } 
            else if (splited[0].equals("get_score")) {
                int userId = Integer.parseInt(splited[2]);
                System.out.printf("Your score with userId %d is %d\n", userId,
                        stub.GetScore(Integer.parseInt(splited[1]), userId));
            } 
            else if (splited[0].equals("get_rank")) {
                int userId = Integer.parseInt(splited[2]);
                System.out.printf("Your rank with userId %d is %d\n", userId,
                        stub.GetRank(Integer.parseInt(splited[1]), userId));
            } 
            else if (splited[0].equals("get_leaderboard")) {
                stub.GetLeaderBoard(Integer.parseInt(splited[1]));
            } 
            else if (splited[0].equals("start_match")) {
                for(UtilsClass node:nodeList)
                    node.startMatch(Integer.parseInt(splited[1]));
            } 
            else if (splited[0].equals("show_players")) {
                stub.ShowPlayers(Integer.parseInt(splited[1]));
            } 
            else {
                System.out.println("Please provide a valid input\n");
            }
        }
        catch (Exception e) {
            System.err.println("Client exception: " + e.toString()); 
            e.printStackTrace(); 
         }
    }
}