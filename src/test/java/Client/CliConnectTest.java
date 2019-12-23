package Client;

//import ChatServer.src.Client.Client;
import Server.Server;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CliConnectTest {

    Server server;
   // Client client;
    Thread thread;

   //@BeforeEach
    void runServer(int port){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                server = new Server(port);
                server.listenClient();
            }
        });
        thread.start();

    }

   /* @AfterEach
     void closeServer(){
        try {
            //server.c.g
            //thread.stop();
            server.server.close();
            client.s.close();
           // client.dis.close();
            //client.dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    @Test
    void connectClient() throws IOException {
        runServer(8080);
        Client client = new Client(8080);
        assertEquals(8080, client.s.getPort());
        //server.server.close();

    }

    @Test
    void wrongPortClient(){
        Client client = new Client (5000);
        assertNull(client.s);
    }


}