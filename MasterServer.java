import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List; 

public class MasterServer{ 
   public MasterServer() {} 
   public static void main(String args[]) { 
      try {   
         // Getting the registry 
         Registry registry = LocateRegistry.getRegistry(null); 
         
         MasterServerImpl obj = new MasterServerImpl();
         UtilsServer stub = (UtilsServer) UnicastRemoteObject.exportObject(obj, 0);
         registry.bind("Dream11-master", stub);
         System.err.println("Master Server ready"); 
      } catch (Exception e) { 
         System.err.println("Master Server exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 
} 
