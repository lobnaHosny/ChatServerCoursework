package Server;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionRunTest {

    Connection c;
    Server server;
    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;

    @Test
    void runTest() throws IOException, InterruptedException {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        dos = new DataOutputStream(ba);
        dis = new DataInputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });
        c = new Connection(socket, server,dis,dos);

        Connection.serverReference = new Server(8000);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                c.run();
            }
        });
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(c.running);

        c.running = false;

    }

}