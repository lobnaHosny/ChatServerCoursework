package Server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ServerPrimitivesTest {
    Server server;
    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;
    Connection c, c1, c2;
    ByteArrayOutputStream ba;


    @Test
    void getUserListTest(){

        Connection c = new Connection(socket, server, dis, dos);
        server = new Server(8011);
        c.state=Connection.STATE_REGISTERED;
        c.username="myName";
        assumeTrue(server.list.isEmpty());
        server.list.add(c);

        ArrayList<String> userList = server.getUserList();

        //System.out.println(userList.get(0));
        assertEquals("myName", userList.get(0));


    }




    @Test
    void doesUserExistTest(){
        Connection c = new Connection(socket, server, dis, dos);
        server = new Server(8012);
        c.state=Connection.STATE_REGISTERED;
        c.username="ABCD";
        server.list.add(c);
        assertTrue(server.doesUserExist("ABCD"));
        assertFalse(server.doesUserExist("Didi"));
        }



     @Test
     void broadcastMessageTest(){
         ByteArrayOutputStream ba = new ByteArrayOutputStream();
         dos = new DataOutputStream(ba);
         Connection c = new Connection(socket, server, dis, dos);
         server = new Server(8013);
         c.state=Connection.STATE_REGISTERED;
         server.list.add(c);
         server.broadcastMessage("This is a broadcast message");
         String output = ba.toString();
         //System.out.println(output);
         int startIndex = output.indexOf("T"); //remove any junk before "This" in "This is a broadcast message"

         assertEquals("This is a broadcast message", output.substring(startIndex));

     }


     @Nested
     class privateMessageTest{

        @BeforeEach
         void setUp(){
            ba = new ByteArrayOutputStream();
            dos = new DataOutputStream(ba);
            c = new Connection(socket, server,dis,dos);
            c.state = Connection.STATE_REGISTERED;
            c.username = "UserXYZ";

            DataOutputStream dos1 = new DataOutputStream(new ByteArrayOutputStream());
            c1 = new Connection(socket, server,dis,dos1);
            c1.state = Connection.STATE_REGISTERED;
            c1.username = "UserABC";

        }


         @Test
         void validPrivateMessageTest(){

             server = new Server(8014);

             server.list.add(c);
             server.list.add(c1);

             boolean sent = server.sendPrivateMessage("This is a private message to UserXYZ", "UserXYZ");
             String output = ba.toString();
             //System.out.println(output);
             int startIndex = output.indexOf("T"); //remove any junk before "This" in "This is a private message..."

             assertEquals("This is a private message to UserXYZ", output.substring(startIndex));
             assertTrue(sent);

         }


         @Test
         void pmToNonExistentUserTest(){
             server = new Server(8015);

             server.list.add(c);
             assumeFalse(server.doesUserExist("Vicky"));
             boolean sent = server.sendPrivateMessage("Long time no see", "Vicky");
             assertFalse(sent);

         }


     }


     @Test
    void getNumberOfUsersTest(){
         c = new Connection(socket, server,dis,dos);
         c.state = Connection.STATE_REGISTERED;

         DataOutputStream dos1 = new DataOutputStream(new ByteArrayOutputStream());
         c1 = new Connection(socket, server,dis,dos1);
         c1.state = Connection.STATE_REGISTERED;

         server = new Server(8016);
         assumeTrue(server.list.isEmpty());

         server.list.add(c);
         server.list.add(c1);
         assertEquals(2, server.getNumberOfUsers());

     }



        @Test
        void connectedNumberOfUsersTest(){
            c = new Connection(socket, server,dis,dos);
            DataOutputStream dos1 = new DataOutputStream(new ByteArrayOutputStream());
            c1 = new Connection(socket, server,dis,dos1);
            DataOutputStream dos2 = new DataOutputStream(new ByteArrayOutputStream());
            c2 = new Connection(socket, server,dis,dos2);

            c.state = Connection.STATE_REGISTERED;
            c1.state = Connection.STATE_UNREGISTERED;
            c2.state = Connection.STATE_UNREGISTERED;
            server = new Server(8016);
            assumeTrue(server.list.isEmpty());

            server.list.add(c);
            server.list.add(c1);
            server.list.add(c2);
            assertEquals(3, server.connectedNumberOfUsers()); //should count both registered and unregistered users

        }



       













}
