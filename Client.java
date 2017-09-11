//David Cho
//UIC
//CS 342 Spring 2016
//Network Chat

//Class: Client


import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

public class Client extends JFrame implements ActionListener {
    // GUI items
    JButton sendButton, connectButton;
    JTextField machineInfo, portInfo, message, username;
    JTextArea history;
    JList<String> nameList;
    String userName;

    // Network Items
    boolean connected;
    Socket clientSocket;
    ObjectOutputStream out;
    ObjectInputStream in;

    // set up GUI
    public Client() {
        super("Chat Client");

        // get content pane and set its layout
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // set up the North panel
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new GridLayout(5, 2));
        container.add(upperPanel, BorderLayout.NORTH);

        // create buttons
        connected = false;

        upperPanel.add(new JLabel("Message: ", JLabel.RIGHT));
        message = new JTextField("");
        message.addActionListener(this);
        upperPanel.add(message);

        sendButton = new JButton("Send Message");
        sendButton.addActionListener(this);
        sendButton.setEnabled(false);
        upperPanel.add(sendButton);

        connectButton = new JButton("Connect to Server");
        connectButton.addActionListener(this);
        upperPanel.add(connectButton);

        upperPanel.add(new JLabel("Username: ", JLabel.RIGHT));
        username = new JTextField("");
        upperPanel.add(username);

        upperPanel.add(new JLabel("Server Address: ", JLabel.RIGHT));
        machineInfo = new JTextField("");
        upperPanel.add(machineInfo);

        upperPanel.add(new JLabel("Server Port: ", JLabel.RIGHT));
        portInfo = new JTextField("");
        upperPanel.add(portInfo);

        //TextBox Display
        history = new JTextArea(10, 40);
        history.setEditable(false);
        container.add(new JScrollPane(history), BorderLayout.WEST);

        //List of users
        nameList = new JList();
        nameList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        nameList.setLayoutOrientation(JList.VERTICAL);
        container.add(new JScrollPane(nameList));

        setSize(600, 300);
        setVisible(true);

    } // end Client constructor

    public static void main(String args[]) {
        Client app = new Client();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // handle button event
    public void actionPerformed(ActionEvent event) {
        if (connected && (event.getSource() == sendButton || event.getSource() == message)) {  //If connected and send button/message box is invoked
            doSendMessage();
        } else if (event.getSource() == connectButton) {  //If not connected and connect button is pressed
            doManageConnection();
        }
    }

    //Send message to server in the form {<message>, "1", "0", "1"}  0/1 let the server know which people to send message to
    public void doSendMessage() {
        String outText = userName + ": " + message.getText();
        Vector<String> output = new Vector<>();
        output.add(outText);
        for (int i = 1; i < nameList.getModel().getSize(); i++){

            if (nameList.isSelectedIndex(0)){                    //if "Send all" is selected
                output.add("1");
            }
            else {
                if (nameList.isSelectedIndex(i)) {
                    output.add("1");
                } else if (nameList.getModel().getElementAt(i).equals(userName)) {  //always send to yourself
                    output.add("1");
                } else {
                    output.add("0");
                }
            }
        }
        try {
            out.writeObject(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        message.setText("");
    }

    public void doManageConnection() {
        if (connected == false) {
            String machineName = null;
            int portNum = -1;
            try {
                machineName = machineInfo.getText();
                userName = username.getText();
                Vector<String> name = new Vector<String>();
                name.add(userName);
                if (userName.equals("")) {
                    history.append("Please enter a username.\n");
                    return;
                }
                portNum = Integer.parseInt(portInfo.getText());
                clientSocket = new Socket(machineName, portNum);
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());
                out.writeObject(name);
                sendButton.setEnabled(true);
                connected = true;
                connectButton.setText("Disconnect from Server");
                machineInfo.setEnabled(false);
                username.setEnabled(false);
                portInfo.setEnabled(false);
                new ClientReceiveThread(this);
            } catch (NumberFormatException e) {
                history.append("Server Port must be an integer\n");
            } catch (UnknownHostException e) {
                history.append("Don't know about host: " + machineName + "\n");
            } catch (IOException e) {
                history.append("Couldn't get I/O for "
                        + "the connection to: " + machineName + "\n");
            }
        } else {   //If connected, then disconnect and reset everything
            try {
                out.writeObject(null);
                out.close();
                in.close();
                clientSocket.close();
                sendButton.setEnabled(false);
                machineInfo.setEnabled(true);
                username.setEnabled(true);
                portInfo.setEnabled(true);
                connected = false;
                connectButton.setText("Connect to Server");
            } catch (IOException e) {
                history.append("Error in closing down Socket \n");
            }
        }
    }
}// end class Client


class ClientReceiveThread extends Thread
{
    private Client clientGui;

    public ClientReceiveThread (Client x)
    {
        clientGui = x;
        start();
    }

    public void run()
    {
        try {
            Vector<String> inputObject;

            while ((inputObject = (Vector<String>)clientGui.in.readObject()) != null)
            {
                if (inputObject.size() == 1) {                             //If size=1, it's a message
                    clientGui.history.append(inputObject.get(0) + "\n");
                }
                else {                                                     //else it's an updated list of names
                    clientGui.nameList.setListData(inputObject);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}


