import java.util.*;
// Implementing the remote interface 
public class ImplMatch implements UtilsClass {  
   
   Map<Integer,Match> map=new HashMap<Integer,Match>(); 
   int numMatches = 0;// keep track of number of matches
   // Implementing the interface method 
   public int AddMatch(int v,String players){
      // add this to database
      map.put(numMatches, new Match(v,players));
      return numMatches++; 
   }
   public void UpdatePlayerScore(int matchId, int playerId, int score){
      Match match = map.get(matchId);
      match.UpdatePlayerScore(playerId,score);
   }
   public int UserCreateTeam(int matchId, int[] playerIds){
      Match match = map.get(matchId);
      return match.addTeam( playerIds);
   }
   public int GetScore(int matchId, int userId){
      Match match = map.get(matchId);
      return match.getUserScore(userId);
   }
   public int GetRank(int matchId, int userId){
      Match match = map.get(matchId);
      return match.getUserRank(userId);
   }
   public void GetLeaderBoard(int matchId){
      Match match = map.get(matchId);
      match.getLeaderBoard();
   }
   public void startMatch(int matchId){
      Match match = map.get(matchId);
      match.startMatch();
   }
   public void ShowPlayers(int matchId){
      Match match = map.get(matchId);
      match.showPlayers();
   }

} 
