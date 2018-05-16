package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class Main extends Application
{
    private ServerSocket server;
    private boolean run_server = true;
    private double red, green, blue;
    private String id = "Lumen_client";
    private Scene scene;

    public Main()
    {
        try
        {
            server = new ServerSocket(4242);
            System.out.println("..::Init Lumen Server, running on port 4242::..");
            listen();
        } catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }

    public void listen()
    {
        Thread tListen = new Thread(() ->
        {
            try
            {
                System.out.println("Starting server...");
                while(run_server)
                {
                    Socket client = server.accept();
                    System.out.println("Received connection..");
                    if(client!=null)
                    {
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        if(in!=null)
                        {
                            String line;
                            while ((line = in.readLine())!=null)
                            {
                                System.out.println("Request: " + line);
                                StringTokenizer tokenizer = new StringTokenizer(line, "\t");
                                String cmd = tokenizer.nextToken();
                                switch (cmd.toUpperCase())
                                {
                                    case "HELLO":
                                        sendMessage("HELLO\t" + id);
                                        break;
                                    case "SETR":
                                        if(tokenizer.hasMoreTokens())
                                        {
                                            try
                                            {
                                                red = Double.parseDouble(tokenizer.nextToken());
                                                scene.setFill(Color.color(red, green, blue));
                                                System.out.println("Updated red value.");
                                                sendMessage("ACK");
                                            }catch (NumberFormatException e)
                                            {
                                                System.err.println(e.getMessage() + "\nAre you fucking stupid?");
                                            }
                                        }else{
                                            System.err.println("Invalid token count!");
                                        }
                                        break;
                                    case "SETG":
                                        if(tokenizer.hasMoreTokens())
                                        {
                                            try
                                            {
                                                green = Double.parseDouble(tokenizer.nextToken());
                                                scene.setFill(Color.color(red, green, blue));
                                                System.out.println("Updated green value.");
                                                sendMessage("ACK");
                                            }catch (NumberFormatException e)
                                            {
                                                System.err.println(e.getMessage() + "\nAre you fucking stupid?");
                                            }
                                        }else{
                                            System.err.println("Invalid token count!");
                                        }
                                        break;
                                    case "SETB":
                                        if(tokenizer.hasMoreTokens())
                                        {
                                            try
                                            {
                                                blue = Double.parseDouble(tokenizer.nextToken());
                                                scene.setFill(Color.color(red, green, blue));
                                                System.out.println("Updated blue value.");
                                                sendMessage("ACK");
                                            }catch (NumberFormatException e)
                                            {
                                                System.err.println(e.getMessage() + "\nAre you fucking stupid?");
                                            }
                                        }else{
                                            System.err.println("Invalid token count!");
                                        }
                                        break;
                                        default:
                                            System.err.println("Unknown command '" + cmd + "'");
                                            break;
                                }
                            }
                        }
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
        tListen.start();
    }

    public boolean sendMessage(String message) throws IOException
    {
        //Log.d("sendMessage" ,">>>>" + InetAddress.getLocalHost().getHostAddress());
        /*Socket dest = new Socket(InetAddress.getByName("192.168.43.31"),4243);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(dest.getOutputStream()));
        if(out==null)
            return false;
        out.write(message);
        out.flush();
        out.close();*/
        return true;
    }

    /*public boolean sendMessage(String message, Socket dest) throws IOException
    {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(dest.getOutputStream()));
        if(out==null)
            return false;
        out.write(message);
        out.flush();
        out.close();
        return true;
    }*/

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        VBox vbox = new VBox();
        scene = new Scene(vbox, 300, 275);
        scene.setFill(Color.RED.brighter());
        primaryStage.setTitle("Lumen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
