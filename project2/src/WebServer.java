import java.net.*;

public final class WebServer {
public static void main(String args[]) throws Exception {

        // Set the port number.
        int port = 8083;

        // Establish the listen socket.
        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println("Starting to listen on port " + port + "...");
        // Process HTTP service requests in an infinite loop.
        while (true) {
            // Listen for a TCP connection request.
            Socket socket = serverSocket.accept();

            //will be socket for each client
            Socket clientSocket = socket;

            // Construct an object to process the HTTP request message.
            HttpRequest request = new HttpRequest(clientSocket);

            // Create a new thread to process the request.
            Thread thread = new Thread(request);

            // Start the thread.
            thread.start();
        } //while
    } //main

} //WebServer