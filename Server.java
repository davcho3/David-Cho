//David Cho
//UIC
//CS 342 Spring 2016
//Network Chat

//Class: Server

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

public class Server extends JFrame implements ActionListener{

    // GUI items
    JButton ssButton;
    JLabel machineInfo, portInfo;
    JTextArea history;
    private boolean running;
    Vector<String> nameList;
    Vector<ObjectOutputStream> nameOut;

    // Network Items
    boolean serverContinue;
    ServerSocket serverSocket;

    // set up GUI
    public Server()
    {
        super( "Chat Server" );
        nameList = new Vector<>();
        nameOut = new Vector<>();
        nameList.add("Send All");

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout( new FlowLayout() );

        // create buttons
        running = false;
        ssButton = new JButton( "Start Listening" );
        ssButton.addActionListener( this );
        container.add( ssButton );

        String machineAddress = null;
        try
        {
            InetAddress addr = InetAddress.getLocalHost();
            machineAddress = addr.getHostAddress();
        }
        catch (UnknownHostException e)
        {
            machineAddress = "127.0.0.1";
        }
        machineInfo = new JLabel (machineAddress);
        container.add( machineInfo );
        portInfo = new JLabel (" Not Listening ");
        container.add( portInfo );

        history = new JTextArea ( 10, 40 );
        history.setEditable(false);
        container.add( new JScrollPane(history) );

        setSize( 500, 250 );
        setVisible( true );

    } // end Server constructor

    public static void main( String args[] )
    {
        Server app = new Server();
        app.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }

    // handle button event
    public void actionPerformed( ActionEvent event )
    {
        if (running == false)
        {
            running = true;
            new ConnectionThread (this);
        }
        else
        {
            running = false;
            serverContinue = false;
            ssButton.setText ("Start Listening");
            portInfo.setText (" Not Listening ");
        }
    }


} // end class Server


class ConnectionThread extends Thread
{
    Server gui;

    public ConnectionThread (Server es3)
    {
        gui = es3;
        start();
    }

    public void run()
    {
        gui.serverContinue = true;

        try
        {
            gui.serverSocket = new ServerSocket(0);
            gui.portInfo.setText("Listening on Port: " + gui.serverSocket.getLocalPort());
            System.out.println ("Connection Socket Created");
            try {
                while (gui.serverContinue)
                {
                    System.out.println ("Waiting for Connection");
                    gui.ssButton.setText("Stop Listening");
                    new CommunicationThread (gui.serverSocket.accept(), gui);
                }
            }
            catch (IOException e)
            {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: 10008.");
            System.exit(1);
        }
        finally
        {
            try {
                gui.serverSocket.close();
            }
            catch (IOException e)
            {
                System.err.println("Could not close port: 10008.");
                System.exit(1);
            }
        }
    }
}


class CommunicationThread extends Thread
{
    private Socket clientSocket;
    private Server gui;
    String name;
    ObjectOutputStream out;
    ObjectInputStream in;

    public CommunicationThread (Socket clientSoc, Server ec3)
    {
        clientSocket = clientSoc;
        gui = ec3;
        start();
    }

    public void run()
    {
        System.out.println ("New Communication Thread Started");

        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            Vector<String> inputObject = (Vector<String>)in.readObject();
            name = inputObject.get(0);
            gui.nameList.add(name);          //Add name to list of names
            gui.nameOut.add(out);            //Add ObjectOutputStream to list of OOS

            for (ObjectOutputStream o: gui.nameOut) {  //Everytime someone new joins, send updated list of names to everyone
                o.writeObject(gui.nameList);
                o.reset();
            }

            while ((inputObject = (Vector<String>)in.readObject()) != null)
            {
                System.out.println ("Server: " + inputObject.get(0));
                gui.history.append (inputObject.get(0)+"\n");
                Vector<String> outputObject = new Vector<>();
                outputObject.add(inputObject.get(0));

                int j = inputObject.size();          //Send only to people who line up with a 1 in the nameCheck
                for (int i = 1; i<j; i++) {
                    if (inputObject.get(i).equals("1")) {
                        gui.nameOut.get(i-1).writeObject(outputObject);
                    }
                }
            }
            gui.nameList.remove(name);      //After sends clicks disconnect, remove their name and OOS, then send out updated list of names
            gui.nameOut.remove(out);
            for (ObjectOutputStream o : gui.nameOut) {
                o.writeObject(gui.nameList);
                o.reset();
            }
            out.close();
            in.close();
            clientSocket.close();
        }
        catch (IOException e)
        {
            try {
                gui.nameList.remove(name);              //If someone just exits client, remove name/OOS, send out updated list of names
                gui.nameOut.remove(out);
                for (ObjectOutputStream o : gui.nameOut) {
                    o.writeObject(gui.nameList);
                    o.reset();
                }
            }catch (IOException j) {}
            System.err.println("Problem with Communication Server");
        } catch (ClassNotFoundException e) {

        }
    }
}
