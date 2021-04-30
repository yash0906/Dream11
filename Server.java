import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner; 

public class Server extends ImplMatch { 
   public Server() {} 
   public static void main(String args[]) { 

      if (args.length < 1) {
         System.out.println("Please provide a valid server number!");
         System.exit(1);
      }
      Scanner sc = new Scanner(System.in);
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
         
         registry.rebind("Dream11-node" + args[0], stub);  
         System.err.println("Dream11-node" + args[0] +" ready!"); 

         String command="";
         boolean flag=true;
         while(sc.hasNextLine() && flag) {
            command = sc.next();

            switch(command) {
               case "exit":
               case "quit":
               case "q": 
                  System.exit(0);
                  break;
               
               case "load":
                  stub.AddMatch(0, "", false);
                  stub.LoadMatch(0);
                  break;
               
               default:
                  System.err.println(command);
            }
         }

      } catch (Exception e) { 
         System.err.println("Server exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 
} 
