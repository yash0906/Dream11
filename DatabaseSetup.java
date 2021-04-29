import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class DatabaseSetup {
    

    public static void main(String[] args) {
        Cluster cluster;
        Session session;
        
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect();

        try{
            session.execute("CREATE KEYSPACE Dream11 with replication = {'class': 'SimpleStrategy', 'replication_factor': 3};");
            session.execute("USE Dream11;");
        } catch (Exception e) {
            session.execute("USE Dream11;");
        }

        try{
            session.execute(
                "CREATE TABLE teams ( " +
                                    "userID int, " +
                                    "teamID int, " +
                                    "matchID int, " +
                                    "playerIDs list<int>, " + 
                                    "PRIMARY KEY((userID, matchID), teamID)" +
                                    ");"
            );
        } catch (Exception e) {
            session.execute("TRUNCATE TABLE teams;");
        }

        try{
            session.execute(
                "CREATE TABLE players ( " +
                                    "matchID int, " + 
                                    "playerID int, " +
                                    "playerName text, " +
                                    "score int, " +
                                    "PRIMARY KEY(playerID, matchID)" +
                                    ");"
            );
        } catch (Exception e) {
            session.execute("TRUNCATE TABLE players;");
        }


    }
}
