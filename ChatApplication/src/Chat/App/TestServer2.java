package Chat.App;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
public class TestServer2 {
    static  String JDBC_URL = "jdbc:mysql://localhost:3306/CHAT_DATABASE";
    static  String DB_USER = "root";
    static  String DB_PASSWORD = "sql";
    public static void main(String[] args) {
        try (
                ServerSocket serverSocket = new ServerSocket(5555);
                Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)
        ) {
            System.out.println("Waiting for connection...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                String clientMessage = in.readLine();
                System.out.println("Client: " + clientMessage);

                System.out.print("Server: ");
                String serverMessage = userInput.readLine();
                out.println(serverMessage);

                // Save to database
                saveChatHistory(conn, clientMessage, serverMessage);

                if (clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Client left the chat.");
                    break;
                }

                if (serverMessage.equalsIgnoreCase("exit")) {
                    System.out.println("Chat ended.");
                    break;
                }
            }
            socket.close();
            System.out.println("Socket closed.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
    // Save chat history
    private static void saveChatHistory(Connection conn, String clientMsg, String serverMsg) {
        String query = "INSERT INTO chat_history (client, server) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, clientMsg);
            stmt.setString(2, serverMsg);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
