package lumen.afridigisolutions.com.lumen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity
{
    private boolean run_server = true;
    private int red, green, blue;
    private final int PORT = 4243;
    private ServerSocket server;
    private SeekBar sb_red, sb_green, sb_blue;
    private ArrayList<String> clients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clients.add("192.168.43.31");
        clients.add("192.168.43.7");
        clients.add("192.168.43.15");
        clients.add("192.168.43.249");

        sb_red = (SeekBar)findViewById(R.id.sb_red);
        sb_green = (SeekBar)findViewById(R.id.sb_green);
        sb_blue = (SeekBar)findViewById(R.id.sb_blue);

        sb_red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                final int pos = i;
                Thread tSend = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            int r = pos;// * 0.100;
                            sendMessage("SETR\t" + r);
                            System.out.println("Red: " + r);
                        } catch (IOException e)
                        {
                            System.err.println(e.getMessage());
                        }
                    }
                });
                tSend.start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        sb_green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                final int pos = i;
                Thread tSend = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            int g = pos;// * 0.100;
                            sendMessage("SETG\t" + g);
                            System.out.println("Green: " + g);
                        } catch (IOException e)
                        {
                            System.err.println(e.getMessage());
                        }
                    }
                });
                tSend.start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        sb_blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                final int pos = i;
                Thread tSend = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            int b = pos;// * 0.100;
                            sendMessage("SETB\t" + b);
                            System.out.println("Blue: " + b);
                        } catch (IOException e)
                        {
                            System.err.println(e.getMessage());
                        }
                    }
                });
                tSend.start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        try
        {
            server = new ServerSocket(PORT);
            System.out.println(String.format("..::Init Lumen Server, running on port %s::..",PORT));
            listen();
            Thread tSend = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        sendMessage("HELLO");
                    } catch (IOException e)
                    {
                       System.out.println(e.getMessage());
                    }
                }
            });
            tSend.start();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void listen()
    {
        Thread tListen = new Thread(new Runnable()
        {
            @Override
            public void run()
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
                                            System.out.println("Received new Client request");
                                            //sendMessage("HELLO " + id, client);
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
            }
        });
        tListen.start();
    }

    public boolean sendMessage(String message) throws IOException
    {
        //Log.d("sendMessage" ,">>>>" + InetAddress.getLocalHost().getHostAddress());
        for(String ip : clients) {
            Socket dest = new Socket(InetAddress.getByName(ip), 4242);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(dest.getOutputStream()));
            if (out == null)
                return false;
            out.write(message);
            out.flush();
            out.close();
        }
        return true;
    }
}
