import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject; 

public class Server1 extends ImplMatch { 
   public Server1() {} 
   public static void main(String args[]) { 
      try { 
         // Instantiating the implementation class 
         ImplMatch obj = new ImplMatch(); 
    
         // Exporting the object of implementation class  
         // (here we are exporting the remote object to the stub) 
         UtilsClass stub = (UtilsClass) UnicastRemoteObject.exportObject(obj, 0);  
         
         // Binding the remote object (stub) in the registry 
         Registry registry = LocateRegistry.getRegistry(); 
         
         registry.bind("Dream11-node1", stub);  
         System.err.println("Server1 ready"); 
      } catch (Exception e) { 
         System.err.println("Server1 exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 
} 
