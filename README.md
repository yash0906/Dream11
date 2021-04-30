## How to run
- Extract jars folder: `tar -xzvf jars.tar.gz`
- Add jars folder to java class path
  - Add this line to .bashrc: `export CLASSPATH=$CLASSPATH:<path_to_jars>/*:<path_to_jars>/lib/*`
- Ensure cassandra is insalled and a server is running on localhost
- javac *.java
- rmiregistry (Ubuntu)
- Setup database using `java DatabaseSetup`
- `java Server 1` (new terminal)
- `java Server 2` (new terminal)
- `java Server 3` (new terminal)
- `java MasterServer` (new terminal)
- `java Client` (new terminal)

## To setup cassandra with multiple nodes on local host:
- Install [ccm](!https://github.com/riptano/ccm): `pip3 install ccm`
- Create a cassandra instance: `ccm create Dream11 -v 3.11.10`
- Add nodes: `ccm populate -n <num_nodes>`
- Start the server: `ccm start`
- To verify it is working: `ccm node1 ring`.
  - <num_nodes> servers will be running on 127.0.0.1, 127.0.0.2, ...

## Verify Java version 8 is installed and selected:
- `sudo apt-get install openjdk-8-jdk openjdk-8-jre`
- To change java version: `sudo update-alternatives --config java`
- To change javac version: `sudo update-alternatives --config javac`

## All Client functions:
- `add_match <num players per team> <all player names>` : Adds a new match. (player names should be space separated)
- `update_player_score <match id> <player id> <score>`: Updates the score of player <player id>
- `create_team <match id> <player ids>`: Creates a new team for a user. (player ids should be space separated)
- `get_score <match id> <user id>`: Get the score of team for user <user id>
- `get_rank <match id> <user id>`: Get rank of user <user id>
- `get_leaderboard <match id>`: Get LeaderBoard of the match <match id>
- `start_match <match id>`: Start match <match id>. No new teams can be created after this
- `show_players <match id>`: Show player names and player Ids of all players playing in match <match id>
- `exit <match id>`: Stop the match simulation and exit the program

## Server functions:
- Master server:
  - `add_node <nodeName>`: Add a new slave node with name <nodeName>
  - `delete_node <nodeName>`: Delete an existing slave node with name <nodeName>
- Slave server:
  - `load`: Sync match data with the database. (Useful when bootstrapping a new slave server while the match is ongoing)