package client.tasks;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

public class IsOnlineCall implements Callable<Boolean> {
    @Override
    public Boolean call()  {

        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 6789);
            return !socket.isClosed();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
