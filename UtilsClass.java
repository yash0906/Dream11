import java.rmi.Remote; 
import java.rmi.RemoteException;  

// Creating Remote interface for our application 
public interface UtilsClass extends Remote {  
   void UpdatePlayerScore(int matchId, int playerId, int score, boolean update) throws RemoteException;
   int UserCreateTeam(int matchId, int[] playerIds, boolean update) throws RemoteException;
   int GetScore(int matchId, int userId) throws RemoteException;
   int GetRank(int matchId, int userId) throws RemoteException;
   String GetLeaderBoard(int matchId) throws RemoteException;
   int AddMatch(int v,String players, boolean update, String db_addr) throws RemoteException;
   void LoadMatch(int matchId) throws RemoteException;
   void startMatch(int matchId) throws RemoteException;
   String ShowPlayers(int matchId) throws RemoteException;
   void DeleteMatch(int matchId) throws RemoteException;
} 
