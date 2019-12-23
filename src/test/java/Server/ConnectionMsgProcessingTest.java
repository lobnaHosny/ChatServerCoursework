package Server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ConnectionMsgProcessingTest {

    Connection c;
    Server server;
    DataInputStream dis;
    //DataOutputStream dos;
    Socket socket;
    ByteArrayOutputStream ba;


    @Nested
    class statTest{

        @BeforeEach
        void setUpConnection(){
            ba = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(ba);
            c = new Connection(socket, server,dis,dos);
        }

        @Test
        void unRegisteredTest(){

            Connection.serverReference = new Server(8000);
            c.state = Connection.STATE_UNREGISTERED;
            c.stat();
            String output = ba.toString();
            //System.out.println(output);
            int startIndex = output.indexOf("O");
            //assert that the correct message is output, meaning that stat() was indeed called by correct switch case
            assertEquals("OK There are currently 0 user(s) on the server\n" +
                    "You have not logged in yet", output.substring(startIndex));

        }


        @Test
        void registeredTest(){
            Connection.serverReference = new Server(8001);
            c.state = Connection.STATE_REGISTERED;
            c.stat();
            String output = ba.toString();
            int startIndex = output.indexOf("O");
            assertEquals("OK There are currently 0 user(s) on the server\n" +
                    "You are logged im and have sent 0 message(s)\n", output.substring(startIndex));
        }


    }


    @Nested
    class idenTest{

        @BeforeEach
        void setUpConnection(){
            ba = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(ba);
            c = new Connection(socket, server,dis,dos);
        }

        @Test
        void alreadyRegisteredTest(){
            c.state = Connection.STATE_REGISTERED;
            c.username = "myUserName";
            c.iden("name");
            String output = ba.toString();
            int startIndex = output.indexOf("B");
            assertEquals("BAD you are already registered with username myUserName", output.substring(startIndex));
        }


        @Test
        void unregisteredUnusedTest(){
            Connection.serverReference = new Server(8002);
            c.iden("someone");
            String output = ba.toString();
            int startIndex = output.indexOf("O");
            assertEquals("OK Welcome to the chat server someone", output.substring(startIndex));
        }

        @Test
        void unregisteredUsedTest(){


            DataOutputStream dos1 = new DataOutputStream(new ByteArrayOutputStream());
            Connection c1 = new Connection(socket, server,dis,dos1);
            c1.state = Connection.STATE_REGISTERED;

            c1.username = "user123";
            Connection.serverReference = new Server(8003);
            Connection.serverReference.list.add(c1);
            c.iden("user123");
            String output = ba.toString();
            int startIndex = output.indexOf("B");
            assertEquals("BAD username is already taken", output.substring(startIndex));


        }

    }



    @Nested
    class listTest{

        @BeforeEach
        void setUpConnection(){
            ba = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(ba);
            c = new Connection(socket, server,dis,dos);
        }

        @Test
        void registeredTest(){

            c.state = Connection.STATE_REGISTERED;
            c.username = "UserXYZ";

            DataOutputStream dos1 = new DataOutputStream(new ByteArrayOutputStream());
            Connection c1 = new Connection(socket, server,dis,dos1);
            c1.state = Connection.STATE_REGISTERED;
            c1.username = "UserABC";

            DataOutputStream dos2 = new DataOutputStream(new ByteArrayOutputStream());
            Connection c2 = new Connection(socket, server,dis,dos2);
            c2.state = Connection.STATE_REGISTERED;
            c2.username = "UserDEF";

            Connection.serverReference = new Server(8004);
            Connection.serverReference.list.add(c);
            Connection.serverReference.list.add(c1);
            Connection.serverReference.list.add(c2);

            c.list();
            String output = ba.toString();
            //System.out.println(output);

            int startIndex = output.indexOf("U");
            assertEquals("UserXYZ\nUserABC\nUserDEF\n", output.substring(startIndex));

        }


        @Test
        void unRegisteredTest(){

            c.state = Connection.STATE_UNREGISTERED;
            c.list();
            String output = ba.toString();
            //System.out.println(output);

            int startIndex = output.indexOf("B");
            assertEquals("BAD You have not logged in yet", output.substring(startIndex));
        }

    }



    @Nested
    class hailTest{

        @BeforeEach
        void setUpConnection(){
            ba = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(ba);
            c = new Connection(socket, server,dis,dos);
        }

        @Test
        void registeredTet(){
            Connection.serverReference = new Server(8004);

            //setting up 'environment'
            c.state = Connection.STATE_REGISTERED; //mimic a registered user
            c.username = "someUser"; //username given to this 'connected' user
            assertEquals(0, c.messageCount); //asserting that the message count is 0 before sending the broadcast

            c.hail("HAIL hey there"); //broadcast message

            //asserting the number of messages sent increased, meaning that hail() was indeed called by the correct switch case
            assertEquals(1,c.messageCount);

        }


        @Test
        void unRegisteredTest(){

            c.state = Connection.STATE_UNREGISTERED;
            c.hail("This is a message");
            String output = ba.toString();
            //System.out.println(output);

            int startIndex = output.indexOf("B");
            assertEquals("BAD You have not logged in yet", output.substring(startIndex));
        }


    }


    @Nested
    class msgTest{
        @BeforeEach
        void setUpConnection(){
            ba = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(ba);
            c = new Connection(socket, server,dis,dos);
        }

        @Test
        void registeredBadFormatTest(){
            Connection.serverReference = new Server(8005);
            c.state = Connection.STATE_REGISTERED;
            c.mesg("message");
            String output = ba.toString(); //get the message sent over connection
            int startIndex = output.indexOf("B");

            assertEquals("BAD Your message is badly formatted", output.substring(startIndex));

        }

        @Test
        void registeredToNonExistantUser(){
            Connection.serverReference = new Server(8006);
            assumeFalse(Connection.serverReference.doesUserExist("User")); //to make sure that there are no users registered with this username

            c.state = Connection.STATE_REGISTERED;
            c.mesg("User Hey there");
            String output = ba.toString();
            //System.out.println(output);
            int startIndex = output.indexOf("B");

            assertEquals("BAD the user does not exist", output.substring(startIndex));

        }


        @Test
        void registeredToExistantUser(){
            //Connection.serverReference = new Server(8007);
            c.state = Connection.STATE_REGISTERED;

            DataOutputStream dos1 = new DataOutputStream(new ByteArrayOutputStream());
            Connection c2 = new Connection(socket, server,dis,dos1);
            c2.state = Connection.STATE_REGISTERED;
            c2.username = "UserDEF";

            Connection.serverReference = new Server(8007);
            Connection.serverReference.list.add(c2);

            c.mesg("UserDEF How was your day?");
            String output = ba.toString();
            //System.out.println(output);

            int startIndex = output.indexOf("O");
            assertEquals("OK your message has been sent", output.substring(startIndex));

        }

        @Test
        void unRegisteredBadFormatTest(){
            c.state = Connection.STATE_UNREGISTERED;
            c.mesg("This is a message");
            String output = ba.toString();
            //System.out.println(output);

            int startIndex = output.indexOf("B");
            assertEquals("BAD You have not logged in yet", output.substring(startIndex));
        }


        @Test
        void unRegisteredToNonExistantUserTest(){

            Connection.serverReference = new Server(8008);
            assumeFalse(Connection.serverReference.doesUserExist("User")); //to make sure that there are no users registered with this username

            c.state = Connection.STATE_UNREGISTERED;
            c.mesg("User This is a message");
            String output = ba.toString();
            //System.out.println(output);

            int startIndex = output.indexOf("B");
            assertEquals("BAD You have not logged in yet", output.substring(startIndex));
        }


        @Test
        void unRegisteredToExistantUserTest(){
            c.state = Connection.STATE_UNREGISTERED;
            DataOutputStream dos1 = new DataOutputStream(new ByteArrayOutputStream());
            Connection c2 = new Connection(socket, server,dis,dos1);
            c2.state = Connection.STATE_REGISTERED;
            c2.username = "UserDEF";

            Connection.serverReference = new Server(8009);
            Connection.serverReference.list.add(c2);

            c.mesg("UserDEF How was your day?");
            String output = ba.toString();
            //System.out.println(output);

            int startIndex = output.indexOf("B");
            assertEquals("BAD You have not logged in yet", output.substring(startIndex));
        }


    }


    @Nested
    class quitTest{
        @BeforeEach
        void setUpConnection(){
            ba = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(ba);
            c = new Connection(socket, server,dis,dos);
        }

        @Test
        void registeredTest(){
            c.state = Connection.STATE_REGISTERED;
            c.client = new Socket();
            Connection.serverReference = new Server(8010);
            c.quit();
            String output = ba.toString();
            //System.out.println(output);

            int startIndex = output.indexOf("O");
            assertEquals("OK thank you for sending 0 message(s) with the chat service, goodbye. ", output.substring(startIndex));
        }


        @Test
        void unRegisteredTest(){
            c.client = new Socket();
            Connection.serverReference = new Server(8011);
            c.quit();
            String output = ba.toString();
            System.out.println(output);

            int startIndex = output.indexOf("O");
            assertEquals("OK goodbye", output.substring(startIndex));

        }

    }


}