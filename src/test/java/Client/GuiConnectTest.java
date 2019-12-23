package Client;

//import Client.ClientGUI;
import Server.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GuiConnectTest{

/*    static Server server;
    ClientGUI clientGUI;
    static Thread thread;


    @BeforeAll
    static void runServer(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                server = new Server(8080);
                server.listenClient();
            }
        });
        thread.start();

    }


    @Test
    void connectClient() throws IOException {
        //runServer();
        clientGUI = new ClientGUI(8080);
        assertEquals(8080, clientGUI.s.getPort());
        //server.server.close();

    }


    @Test
    void wrongPortClient(){
        clientGUI = new ClientGUI(5000);
        assertNull(clientGUI.s);
    }*/

}