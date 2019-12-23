package Server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionPrimitivesTest {
    Connection c;
    Server server;
    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;


    @Test
    void getUserNameTest( ){

        c = new Connection(socket, server,dis,dos);
        c.username = "User1";
        assertEquals("User1", c.getUserName());
    }

    @Test
    void isRunningTest(){
        c = new Connection(socket, server,dis,dos);
        assertFalse(c.isRunning());
    }


    @Test
    void getStatusTest(){
        c = new Connection(socket, server,dis,dos);
        assertEquals(Connection.STATE_UNREGISTERED,c.getState());

        c.state = Connection.STATE_REGISTERED;
        assertEquals(Connection.STATE_REGISTERED, c.getState());
    }

    @Test()
    void sendOverConnectionTest(){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        dos = new DataOutputStream(ba);
        c = new Connection(socket, server,dis,dos);
        c.sendOverConnection("This is a test message");
        String output = ba.toString();

        assertEquals("This is a test message", output.trim());
        assertThrows(NullPointerException.class, ()->c.sendOverConnection(null));

    }

    @Test
    void messageForConnectionTest(){
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        dos = new DataOutputStream(ba);
        c = new Connection(socket, server,dis,dos);
        c.messageForConnection("This is a test message");
        String output = ba.toString();

        assertEquals("This is a test message", output.trim());
    }



}