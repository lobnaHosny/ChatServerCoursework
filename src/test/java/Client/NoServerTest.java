package Client;

//import Client.Client;
//import ChatServer.src.Client.ClientGUI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoServerTest {


    @Test
    void noServerCLI(){ //initializing client without running server
        Client client = new Client(65000);
        assertNull(client.s); //socket should be null, since no connection will be established
    }

   /* @Test
    void noServerGUI(){ //initializing client gui without running server
        ClientGUI clientGUI = new ClientGUI(65000);
        assertNull(clientGUI.s); //socket should be null, since no connection will be established
    }*/

}