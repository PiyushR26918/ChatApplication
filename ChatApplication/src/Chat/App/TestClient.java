package Chat.App;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.awt.*;
import javax.swing.*;

public class TestClient {
    public static void main(String[] args) {
        JFrame frame = new JFrame("CLIENT");
        frame.setSize(800,800);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton b1 = new JButton("SEND");
        b1.setBounds(350,700,70,30);

        TextField tf = new TextField();
        tf.setBounds(250,600,300,40);

        Label label = new Label();
        //label.setBounds(50,50,400,400);

        JTextArea ta = new JTextArea();
        ta.setBounds(50,50,400,400);

        frame.add(b1);
        frame.add(tf);
        frame.add(ta);
        try {
            Socket socket = new Socket("127.0.0.1", 5555);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ta.append("Client :"+tf.getText());
                    ta.append("\n");
                    out.println(tf.getText());

                }
            });

            while (true){
                if (tf.getText().equalsIgnoreCase("exit") ){
                    System.out.println("Chat ended");
                    socket.close();
                    break;
                }
                String serverMessage = in.readLine();
                if (serverMessage.equalsIgnoreCase("exit") ){
                    System.out.println("Chat ended");
                    socket.close();
                    break;
                }
                ta.append("Server: "+serverMessage);
                ta.append("\n");
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
