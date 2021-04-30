import java.util.*;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

// users, teams are part of match 
class Match {
    int numPlayers;
    int numUsers; // keeps track of number of users who have register team
    int numTeams; // number of teams created by users in the match
    int playerScore[] ; // keep trackes of player's score 
    int sortedTeams[]; // teams in sorted order
    int teamRank[]; // maps teamId with team rank
    Map<Integer,String> playerNames=new HashMap<Integer,String>(); // maps player id with their names
    Map<Integer,Integer> userTeam=new HashMap<Integer,Integer>(); // maps user to team id
    Map<int[], Integer> teams=new HashMap<int[],Integer>(); // maps teams to their ids
    Map<Integer, int[]> teamsId=new HashMap<Integer, int[]>(); // maps teamId to their team

    Cluster cluster;
    Session session;
    ResultSet result;


    Match(int v, String players, boolean update){
        numPlayers = v;
        String[] splited = players.split(" ");
        playerScore = new int[2*v]; // each team has v player, so in total there are 2*v players
        // add players to database here and create a match, which would generate a unique match id
        for(int i=0;i<2*v;i++){
            playerNames.put(i,splited[i]); // players are given ids as their names entered in order
            playerScore[i] = 0;
        }
        numUsers = 0;
        numTeams = 0;
        
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        if (update) {
            session = cluster.connect();
            session.execute("USE Dream11");
            String query = "BEGIN BATCH ";
            for(int i=0; i<2*v; i++) {
                query += "INSERT INTO players (matchID, playerID, playerName, score) " +
                                "VALUES ( 0, " + i +", '" + playerNames.get(i) + "', 0); ";
                
            }
            query += "APPLY BATCH";
            
            session.execute(query);
            session.close();
        }
    }
    public void LoadMatch(int matchId) {
        session = cluster.connect();
        session.execute("USE Dream11");

        result = session.execute("SELECT DISTINCT playerID FROM players;");
        numPlayers = result.all().size()/2;

        result = session.execute("SELECT DISTINCT userID,matchID FROM teams;");
        numUsers = result.all().size();
     
        playerScore = new int [2*numPlayers];
        result = session.execute("SELECT playerID, score, playerName FROM players;");
        List<Row> rows = result.all();
    
        for(Row row:rows) {
            playerScore[row.getInt(0)] = row.getInt(1);
            playerNames.put(row.getInt(0), row.getString(2));
        }

        result = session.execute("SELECT userID, teamID, playerIDs FROM teams;");
        List<Row> users = result.all();
        
        int max = 0;
        for(Row row:users) {
            if (max < row.getInt(1)) max = row.getInt(1);
            userTeam.put(row.getInt(0), row.getInt(1));
            List<Integer> list = row.getList(2, Integer.class);
            int[] arr = new int[list.size()];
            for(int i=0; i<list.size(); i++) arr[i] = list.get(i);
            teams.put(arr, row.getInt(1));
            teamsId.put(row.getInt(1), arr);
        }
        
        numTeams = max+1;

        startMatch();
        sortTeams();
        session.close();
    }
    public void UpdatePlayerScore(int playerId, int score, boolean update){
        playerScore[playerId] += score;
        if (update) {
            session = cluster.connect();
            session.execute("USE Dream11");
            session.execute("UPDATE players SET score=" + playerScore[playerId] + " WHERE playerID=" + playerId + " AND matchID=0;");
            session.close();
        }

        sortTeams();
    }
    public int addTeam(int[] playerIds, boolean update){
        // first we need to check if that particular team is already created
        // here we are considering that playerIds are in sorted order
        boolean isKeyPresent = teams.containsKey(playerIds);
        if(isKeyPresent){
            userTeam.put(numUsers, teams.get(playerIds));
        }
        else{
            teams.put(playerIds, numTeams);
            teamsId.put(numTeams, playerIds);
            userTeam.put(numUsers,numTeams);
            numTeams++;
        }

        if (update) {
            session = cluster.connect();
            session.execute("USE Dream11");
            String query = "INSERT INTO teams (userID, teamID, matchID, playerIDs)" + 
                            " VALUES (" + 
                            numUsers + ", " + 
                            (numTeams-1) + ", " +
                            0 + ", [";
            for(int i=0; i<playerIds.length; i++) {
                query += playerIds[i];
                if(i < playerIds.length-1) query += ", ";
            } 
            query += "]);";
            session.execute(query);
            session.close();
        }

        return numUsers++; // increase number of users who create a team, this is user id generated by us

    }
    public int getUserScore(int userId){
        int userTeamId = userTeam.get(userId);
        int score = getTeamScore(userTeamId);
        // //System.out.printf("User Score: %d\n" , score);
        return score; 
    }
    public int getUserRank(int userId){
        int userTeamId = userTeam.get(userId);
        int rank = teamRank[userTeamId];
        // //System.out.printf("User Rank: %d\n" , rank);
        return rank;    
    }
    public String getLeaderBoard(){
        String leaderBoard = "";
        int rank = 0;
 
        for(int i=0;i<numUsers;i++){
            rank = teamRank[userTeam.get(i)];
            // //System.out.printf("User %d rank is %d\n" , i,rank);
            leaderBoard += "User " + i + " rank is " + rank + "\n";
        }
        return leaderBoard;
    }
    public void startMatch(){
        sortedTeams = new int[numTeams];
        teamRank = new int[numTeams];
        for(int i=0;i<numTeams;i++){
            sortedTeams[i] = i;
            teamRank[i]=0;
        }
    }
    public void sortTeams(){
        for(int i=1;i<numTeams;i++){
            if(getTeamScore(sortedTeams[i-1])<getTeamScore(sortedTeams[i])){
                int j = i-1;
                int curTeamId = sortedTeams[i];
                while(j>=0 && getTeamScore(sortedTeams[j])<getTeamScore(curTeamId)){
                    sortedTeams[j+1] = sortedTeams[j];
                    j--;
                }
                j++;
                sortedTeams[j] = curTeamId;
            }
        }
        //System.out.printf("CALLED IN SORT\n");
        // for(int i=0;i<numTeams;i++){
            //System.out.printf("User %d rank is %d\n" , sortedTeams[i],i);
        // }
        teamRank[sortedTeams[0]] = 0;
        for(int i=1;i<numTeams;i++){
            if(getTeamScore(sortedTeams[i])<getTeamScore(sortedTeams[i-1])){
                teamRank[sortedTeams[i]] = teamRank[sortedTeams[i-1]]+1;
            }
            else{
                teamRank[sortedTeams[i]] = teamRank[sortedTeams[i-1]];
            }
        }
        //System.out.printf("TEAM RANK CALLED IN SORT\n");
        // for(int i=0;i<numTeams;i++){
            // System.out.printf("User %d rank is %d\n" , i,teamRank[i]);
        // }

    }
    public int getTeamScore(int teamId){
        int score=0;
        int[] players = teamsId.get(teamId);
        for(int i=0;i<players.length;i++){
            score+=playerScore[players[i]];
        }
        return score;
    }
    public String showPlayers(){

        String players = "Player Name - Player ID - Score\n";
        for(int i=0;i<2*numPlayers;i++){
            //System.out.printf("%s %d\n", playerNames.get(i),i);
            players += playerNames.get(i) + " - " + i + " - " + playerScore[i] + "\n";
        }
        return players;
    }

    public void DeleteMatch() {
        session = cluster.connect();
        session.execute("USE Dream11;");
        session.execute("TRUNCATE teams;");
        session.execute("TRUNCATE players;");
        session.close();
    }
    
}