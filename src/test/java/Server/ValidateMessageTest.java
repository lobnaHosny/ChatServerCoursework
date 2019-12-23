package Server;


import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.Socket;


import static org.junit.jupiter.api.Assertions.*;

class ValidateMessageTest {

    Connection c;
    Server server;
    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;



     @Test
     void invalidMessageTest() throws IOException {
        //list of incorrect key words that should be filtered out by the validateMessage method
        String[] inCorrectMessages = new String[]{"IDENNN", "iden", "IdEn", "LIIST", "list", "LisT", "SSTATT", "Stat", "stat", "$tat",
         "HAIIIL", "hail", "HaIl", "MESGGGG", "MeSg", "mesg", "QUITTTT", "quit", "quiT", "IDE", "I", "H", "Hai", "York", "Send", "MESS", "HELLO"};



         for (int i=0; i<inCorrectMessages.length; i++) { //for each incorrect message

             //set up connection
             ByteArrayOutputStream ba = new ByteArrayOutputStream();
             dos = new DataOutputStream(ba);
             c = new Connection(socket, server, dis, dos);

             c.validateMessage(inCorrectMessages[i]); //pass message to validateMessage

             String output = ba.toString();


             if (inCorrectMessages[i].length() <4){ //if word is too short to be key word, show the correct message

                 //assert the output messages is correct
                 assertEquals("BAD invalid command to server", output.trim()); }
             else  //else assert that the output message says that the command is not recognized
             assertEquals("BAD command not recognised", output.trim());

         }

     }

     @Test
     void validateMessageCorrectIden(){

             //setting up connection
             ByteArrayOutputStream ba = new ByteArrayOutputStream();
             dos = new DataOutputStream(ba);
             c = new Connection(socket, server, dis, dos);
             Connection.serverReference = new Server(8000);

             String msg = "IDEN User1"; //message to be validated
             c.validateMessage(msg); //validate message

             String output = ba.toString(); //get the message sent over connection
             int startIndex = output.indexOf("O");

            //assert that the correct message is output, meaning that iden() was indeed called by correct switch case
             assertEquals("OK Welcome to the chat server User1", output.substring(startIndex));

     }


    @Test
    void validateMessageCorrectStat(){
        //setting up connection
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        dos = new DataOutputStream(ba);
        c = new Connection(socket, server, dis, dos);
        Connection.serverReference = new Server(6000);

        String msg = "STAT"; //message to be validated
        c.validateMessage(msg); //validate message

        String output = ba.toString(); //get the message sent over connection
        int startIndex = output.indexOf("O");
        //assert that the correct message is output, meaning that stat() was indeed called by correct switch case
        assertEquals("OK There are currently 0 user(s) on the server\n" +
                "You have not logged in yet", output.substring(startIndex));

    }


    @Test
    void validateMessageCorrectList(){
        //setting up connection
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        dos = new DataOutputStream(ba);
        c = new Connection(socket, server, dis, dos);
        Connection.serverReference = new Server(7000);

        //setting up 'environment', so that we can verify that list() was ran
        c.state = Connection.STATE_REGISTERED; //mimic a registered user
        c.username = "someUser"; //username given to this 'connected' user
        Connection.serverReference.list.add(c); //add our mock connection to the list of connections in server

        String msg = "LIST"; //message to be validated
        c.validateMessage(msg); //validate message

        String output = ba.toString(); //get the message sent over connection
        int startIndex = output.indexOf("s"); //look for 's' (i.e. someUser), to remove any junk that comes before message
        //assert that the correct message is output, meaning that list() was indeed called by correct switch case
        assertEquals("someUser\n", output.substring(startIndex));


    }

    @Test
    void validateMessageCorrectHail(){
        //setting up connection
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        dos = new DataOutputStream(ba);
        c = new Connection(socket, server, dis, dos);
        Connection.serverReference = new Server(4000);

        //setting up 'environment', so that we can verify that list() was ran
        c.state = Connection.STATE_REGISTERED; //mimic a registered user
        c.username = "someUser"; //username given to this 'connected' user
        assertEquals(0, c.messageCount); //asserting that the message count is 0 before sending the broadcast

        String msg = "HAIL hey there"; //message to be validated
        c.validateMessage(msg); //validate message

        //asserting the number of messages sent increased, meaning that hail() was indeed called by the correct switch case
        assertEquals(1,c.messageCount);

    }


    @Test
    void validateMessageCorrectMesg(){
        //setting up connection
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        dos = new DataOutputStream(ba);
        c = new Connection(socket, server, dis, dos);
        Connection.serverReference = new Server(55000);

        //setting up 'environment', so that we can verify that list() was ran
        c.state = Connection.STATE_REGISTERED; //mimic a registered user

        String msg = "MESG J"; //message to be validated
        c.validateMessage(msg); //validate message

        String output = ba.toString(); //get the message sent over connection
        int startIndex = output.indexOf("B");

        //assert that the correct message is output, meaning that mesg() was indeed called by correct switch case
        assertEquals("BAD Your message is badly formatted", output.substring(startIndex));


    }


    @Test
    void validateMessageCorrectQuit(){
        //setting up connection
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        dos = new DataOutputStream(ba);
        c = new Connection(socket, server, dis, dos);
        c.client = new Socket();


        Connection.serverReference = new Server(51000);


        String msg = "QUIT"; //message to be validated
        c.validateMessage(msg); //validate message

        String output = ba.toString(); //get the message sent over connection
        int startIndex = output.indexOf("O");

        System.out.println(output);
        //assert that the correct message is output, meaning that quit() was indeed called by correct switch case
        assertEquals("OK goodbye", output.substring(startIndex));


    }

}