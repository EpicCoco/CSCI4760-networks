import java.io.*;
import java.net.*;
import java.util.*;

final class HttpRequest implements Runnable {

    final static String CRLF = "\r\n";
    Socket socket;
    // Constructor
    public HttpRequest(Socket socket) throws Exception {
        this.socket = socket;
    } //HttpRequest

    // Implement the run() method of the Runnable interface.
    @Override
    public void run() {

        //try to process request
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        } //try

    } //run

    private void processRequest() throws Exception {
        System.out.println("Starting to process request...");
        
        // Get a reference to the socket's input and output streams.
        InputStream is = socket.getInputStream();
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());

        // Set up input stream filters.
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        // Get the request line of the HTTP request message.
        String requestLine = br.readLine();

        // Display the request line.
        System.out.println();
        System.out.println(requestLine);

        // Get and display the header lines.
        String headerLine = null;
        while ((headerLine = br.readLine()).length() != 0) {
            System.out.println(headerLine);
        } //while

        os.close();
        br.close();

    } //processRequest

} //HttpRequest