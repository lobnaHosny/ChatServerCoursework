package Server;


import org.junit.jupiter.api.Test;

import java.io.IOException;

import java.net.ServerSocket;


import static org.junit.jupiter.api.Assertions.*;

class ServerInitTest {

    Server server; //declaring server

    void runServer(int port){
        server = new Server(port); //initializing new Server on given port
    }


    @Test
    void validPortTest(){

    int[] validPorts = new int[]{ 1, 32768, 65535}; //array of valid ports that server should be able to connect to

    for (int i=0; i<validPorts.length; i++) {
        runServer(validPorts[i]);
        assertEquals(validPorts[i], server.server.getLocalPort());

    }
}


    @Test
    void invalidPortTest(){
        int[] invalidPorts = new int[] {-1, 65536};

        for (int j=0; j<invalidPorts.length; j++){
            runServer(invalidPorts[j]);
            assertNull(server.server);
        }
    }


    @Test
        void busyPortTest() {
        try {
            ServerSocket ss = new ServerSocket(8080);
            runServer(8080);
            assertNull(server.server);
        } catch (IOException e) {
            //e.printStackTrace();
        }



    }


}