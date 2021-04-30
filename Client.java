import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  
import java.util.*;

public class Client {  
   private Client() {}  
   public static void main(String[] args) {  
      Scanner sc = new Scanner(System.in); 
      String result = ""; 
      try {  
         // Getting the registry 
         Registry registry = LocateRegistry.getRegistry(null); 
         // Looking up the registry for the remote object 
         UtilsServer stub = (UtilsServer) registry.lookup("Dream11-master"); 
         String str;
            while(sc.hasNext()){
               try{
                  str = sc.nextLine();
                  // System.out.print("You have entered: "+str + "\n");
                  result = stub.Query(str);
                  System.out.println(result);
                  if (str.equals("exit")) {
                     System.exit(0);
                  }
               }
               catch(Exception e){
                  System.out.println(e);
                  break;
               }
            }
      } catch (Exception e) {
         System.err.println("Client exception: " + e.toString()); 
         e.printStackTrace(); 
      } 
   } 
}
