package Client;

//import ChatServer.src.Client.Client;
import Server.Server;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;

class CliRegisterTest {

    Server server;
    Client client;
    Thread thread;
    private ExecutorService es;

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

/*
    @AfterEach
    void closeServer(){


        try {
            client.s.close();
            server.server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    //@Test
   synchronized Client registerClient(int port, String name){

       Client client = new Client(port);
        //String input = "IDEN c1" + username;
        String input = "IDEN " + name;
        //.concat(username);
        InputStream i = new ByteArrayInputStream(input.getBytes());
        System.setIn(i);
        Scanner scn = new Scanner(System.in);
        client.sendMessage(scn);
        //client.readMessage();
        return client;
        //assertTrue(server.doesUserExist("c1"));
    }

   @Test
    void testRegister(){
       runServer(52100);
        String[] usernameList = {"abcd", "c1d2e3", "3658", "CLIclient", "ABCD", "NAME", "$home!", "B@r>?e^%"};

        for (int i=0; i<usernameList.length; i++){
           registerClient(52100, usernameList[i]);
            //int finalI = i;
   /*         Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    assertTrue(server.doesUserExist(usernameList[finalI]));
                }
            });

            try {
                th.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            th.start();

*/
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertTrue(server.doesUserExist(usernameList[i]));

        }

        //assertEquals(usernameList,server.getUserList());

    }


   /* //@Test
    synchronized void usedNameTest(){
        Client client1, client2;

        *//*client1 = registerClient();
        client2 = registerClient();*//*
        //registerClient("myName");


       *//* Thread t = new Thread(new Runnable() {
        @Override
        public void run() {

            registerClient("myName");
        }
    });
        try {
            t.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.start();*//*


        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                String m = client2.readMessage();
                System.out.println("This is final message: " + m);
                assertEquals("BAD username is already taken", m);
                //registerClient("myName");
                //assertTrue(server.doesUserExist(usernameList[finalI]));
            }
        });

        try {
            th.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        th.start();



    }*/



    @Test
    synchronized void usedNames() {
        runServer(6000);
        //registerClient(6000);
//        int j;
//        String[] userNames = {"myName","oooo"};
        try {
            Thread.sleep(600);

        String m = "";
        int j;
        for (j = 0; j < 2; j++) {
            int finalJ = j;
            System.out.println(finalJ);
            //client = registerClient(6000);
            try {
                client = registerClient(6000, "myName");

                Thread.sleep(500);
                m = client.readMessage();
                /*System.out.println("Registered " + server.getNumberOfUsers());
                System.out.println("Connected " + server.connectedNumberOfUsers());
*/
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        *///assertEquals("BAD username is already taken", m);
        assertEquals(1, server.getNumberOfUsers());
        assertEquals(2, server.connectedNumberOfUsers());

    }
}