import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject; 

public class Server3 extends ImplMatch { 
   public Server3() {} 
   public static void main(String args[]) { 
      Registry registry = null;

      try {
         registry = LocateRegistry.createRegistry(1099); 
      } catch(ExportException ee) {
         try{
            registry = LocateRegistry.getRegistry(1099); 
         } catch(Exception e) {
            System.err.println("Server exception: " + e.toString()); 
            e.printStackTrace(); 
         }
         System.err.println("Port already in use, Attempting to use the open registry"); 
      } catch(Exception e) {
         System.err.println("Server exception: " + e.toString()); 
         e.printStackTrace();
      }

      try { 
         // Instantiating the implementation class 
         ImplMatch obj = new ImplMatch(); 
    
         // Exporting the object of implementation class  
         // (here we are exporting the remote object to the stub) 
         UtilsClass stub = (UtilsClass) UnicastRemoteObject.exportObject(obj, 0);  
         
         // Binding the remote object (stub) in the registry 
         
         registry.rebind("Dream11-node3", stub);  
         System.err.println("Server3 ready"); 
      } catch (Exception e) { 
         System.err.println("Server3 exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 
} 
