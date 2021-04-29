import java.rmi.Remote; 
import java.rmi.RemoteException;  

// Creating Remote interface for our application 
public interface UtilsServer extends Remote {  
   void Query(String query) throws RemoteException;
} 