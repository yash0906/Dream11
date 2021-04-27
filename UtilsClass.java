import java.rmi.Remote; 
import java.rmi.RemoteException;  

// Creating Remote interface for our application 
public interface UtilsClass extends Remote {  
   void UpdatePlayerScore(int matchId, int playerId, int score) throws RemoteException;
   int UserCreateTeam(int matchId, int[] playerIds) throws RemoteException;
   int GetScore(int matchId, int userId) throws RemoteException;
   int GetRank(int matchId, int userId) throws RemoteException;
   void GetLeaderBoard(int matchId) throws RemoteException;
   int AddMatch(int v,String players) throws RemoteException;
   void startMatch(int matchId) throws RemoteException;
   void ShowPlayers(int matchId) throws RemoteException;
} 
