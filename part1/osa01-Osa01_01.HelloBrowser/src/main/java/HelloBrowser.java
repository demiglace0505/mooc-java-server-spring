
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class HelloBrowser {

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("=====Where to?=====");
        String host = input.nextLine();
        int port = 80;
        
        Socket socket = new Socket(host, port);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println("GET / HTTP/1.1");
        writer.println("Host: " + host);
        writer.println();
        writer.flush();
        
        System.out.println("=====Response=====");
        
        Scanner output = new Scanner(socket.getInputStream());
        while (output.hasNextLine()) {
            System.out.println(output.nextLine());
        }
    }
}
