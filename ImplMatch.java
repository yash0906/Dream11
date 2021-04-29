import java.util.*;
// Implementing the remote interface 
public class ImplMatch implements UtilsClass {  
   
   Map<Integer,Match> map=new HashMap<Integer,Match>(); 
   int numMatches = 0;// keep track of number of matches
   // Implementing the interface method 
   public int AddMatch(int v,String players, boolean update){
      // add this to database
      map.put(numMatches, new Match(v,players, update));
      return numMatches++; 
   }  
   public void LoadMatch(int matchId) {
      Match match = map.get(matchId);
      match.LoadMatch(matchId);
   }
   public void UpdatePlayerScore(int matchId, int playerId, int score, boolean update){
      Match match = map.get(matchId);
      match.UpdatePlayerScore(playerId,score, update);
   }
   public int UserCreateTeam(int matchId, int[] playerIds, boolean update){
      Match match = map.get(matchId);
      return match.addTeam( playerIds, update);
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
