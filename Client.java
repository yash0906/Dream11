import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  
import java.util.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

public class Client {  
   private Client() {}  
   public static void main(String[] args) {  
      Scanner sc = new Scanner(System.in);  
      try {  
         // Getting the registry 
         Registry registry = LocateRegistry.getRegistry(null); 
         // Looking up the registry for the remote object 
         UtilsClass stub = (UtilsClass) registry.lookup("Dream11"); 
         String str;
            while(sc.hasNext()){
               try{
                  str = sc.nextLine();
                  // System.out.print("You have entered: "+str + "\n");
                  String[] splited = str.split(" ");
                  if(splited[0].equals("add_match")){
                     String playerNames = splited[2];
                     for(int i=3;i<splited.length;i++){
                        playerNames  = playerNames + " " + splited[i];
                     }
                     System.out.printf("Match ID %d\n" , stub.AddMatch(Integer.parseInt(splited[1]), playerNames));
                  }
                  else if(splited[0].equals("update_player_score")){
                     stub.UpdatePlayerScore(Integer.parseInt(splited[1]), Integer.parseInt(splited[2]), Integer.parseInt(splited[3]));
                  }
                  else if(splited[0].equals("create_team")){
                     int[] playerIds = new int[splited.length-2];
                     for(int i=0;i<splited.length-2;i++){
                        playerIds[i] = Integer.parseInt(splited[i+2]);
                     }
                     System.out.printf("Your generated userId is %d\n",stub.UserCreateTeam(Integer.parseInt(splited[1]), playerIds));
                  }
                  else if(splited[0].equals("get_score")){
                     int userId = Integer.parseInt(splited[2]);
                     System.out.printf("Your score with userId %d is %d\n", userId, stub.GetScore(Integer.parseInt(splited[1]), userId));
                  }
                  else if(splited[0].equals("get_rank")){
                     int userId = Integer.parseInt(splited[2]);
                     System.out.printf("Your rank with userId %d is %d\n", userId, stub.GetRank(Integer.parseInt(splited[1]), userId));
                  }
                  else if(splited[0].equals("get_leaderboard")){
                     stub.GetLeaderBoard(Integer.parseInt(splited[1]));
                  }
                  else if(splited[0].equals("start_match")){
                     stub.startMatch(Integer.parseInt(splited[1]));
                  }
                  else if(splited[0].equals("show_players")){
                     stub.ShowPlayers(Integer.parseInt(splited[1]));
                  }
                  else{
                     System.out.println("Please provide a valid input\n");
                  }

               }
               catch(Exception e){
                  System.out.println(e);
                  break;
               }
            }
      } catch (Exception e) {
         System.err.println("Client exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 
}
