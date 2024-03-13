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
            System.out.println("Request processed.");
        } catch (Exception e) {
            e.printStackTrace();
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

        /* old code to simply print header lines
        // Get and display the header lines.
        String headerLine = null;
        while ((headerLine = br.readLine()).length() != 0) {
            System.out.println(headerLine);
        } //while
        */

        // Extract the filename from the request line.
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken(); // skip over the method, which should be "GET"
        String fileName = tokens.nextToken();
        // Prepend a "." so that file request is within the current directory.
        fileName = "html/" + fileName;
        System.out.println("fileName: " + fileName);

        // Open the requested file.
        FileInputStream fis = null;
        boolean fileExists = true;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            fileExists = false;
        } //try

        // Construct the response message.
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        if (fileExists) {
            statusLine = "200 OK";
            contentTypeLine = "Content-type: " + contentType(fileName) + CRLF;
        } else {
            statusLine = "404 Not Found";
            contentTypeLine = "Content-type: text/html" + CRLF;
            entityBody = "<HTML>" +
                "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
                "<BODY>Not Found</BODY></HTML>";
        } //if

        // Send HTTP version 
        os.writeBytes("HTTP/1.1 ");
        // Send the status line.
        os.writeBytes(statusLine);
        // Send the content type line.
        os.writeBytes(contentTypeLine);

        // Send a blank line to indicate the end of the header lines.
        os.writeBytes(CRLF);
        
        // Send the entity body.
        if (fileExists) {
            sendBytes(fis, os);
            fis.close();
        } else {
            os.writeBytes(entityBody);
        } //if

        os.close();
        br.close();
        socket.close();

    } //processRequest

    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception {
        // Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;
        // Copy requested file into the socket's output stream.
        while((bytes = fis.read(buffer)) != -1 ) {
            os.write(buffer, 0, bytes);
        } //while
    } //sendBytes

    private static String contentType(String fileName) {
        if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return "text/html";
        } //if
        if(fileName.endsWith(".gif")) {
            return "image/gif";
        } //if
        if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } //if
        return "application/octet-stream";
    } //contentType

} //HttpRequest