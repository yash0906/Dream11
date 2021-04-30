import java.rmi.Remote; 
import java.rmi.RemoteException;  

// Creating Remote interface for our application 
public interface UtilsServer extends Remote {  
   String Query(String query) throws RemoteException;
   void AddNode(String nodeName) throws RemoteException;
   void DeleteNode(String nodeName) throws RemoteException;
} 