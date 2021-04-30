import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class MasterServer{ 
   public MasterServer() {} 
   public static void main(String args[]) { 
      Registry registry = null;
      Scanner sc = new Scanner(System.in);

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
         // Getting the registry 
         
         MasterServerImpl obj = new MasterServerImpl();
         UtilsServer stub = (UtilsServer) UnicastRemoteObject.exportObject(obj, 0);
         registry.rebind("Dream11-master", stub);
         System.err.println("Master Server ready"); 

         String command="";
         while(sc.hasNextLine()) {
            command = sc.next();

            switch(command) {
               case "exit":
               case "quit":
               case "q": 
                  stub.Query("exit 0");
                  System.exit(0);
                  break;
               
               default:
                  System.err.println(command);
            }
         }
      } catch (Exception e) { 
         System.err.println("Master Server exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 
} 
