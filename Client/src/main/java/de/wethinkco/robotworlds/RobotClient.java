package de.wethinkco.robotworlds;

import de.wethinkco.robotworlds.protocol.*;
import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.helpers.JsonHelper;
import de.wethinkco.robotworlds.protocol.world.ServerConfig;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static de.wethinkco.robotworlds.InputValidation.checkIpAndPort;

// A client sends messages to the server, the server spawns a thread to communicate with the client.
// Each communication with a client is added to an array list that the server maintains


public class RobotClient {

    public boolean launch = false;

    // A client has a socket to connect to the server and a reader and writer to receive and send messages respectively.
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private final JsonHelper jsonHelper = new JsonHelper();
    private Robot state;

    public RobotClient(){
    }

    public RobotClient(Socket socket) {
        try {
            this.socket = socket;
            this.username = "";
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

//            String serverConfigJson = bufferedReader.readLine();
//            ServerConfig serverConfig = jsonHelper.JsonToServerConfig(serverConfigJson);

        } catch (IOException e) {
            // Gracefully close everything.
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Sending a message isn't blocking and can be done without spawning a thread, unlike waiting for a message.
    public void sendMessage() {
        new Thread(() -> {
        try {
//            // Initially send the username of the client.
//            bufferedWriter.write(username + " " + make);
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
            // Create a scanner for user input.
            Scanner scanner = new Scanner(System.in);
            String commandString = null;
            // While there is still a connection with the server, continue to scan the terminal and then send the message.

                System.out.print("Welcome to Robots! \n");

                while (socket.isConnected()) {

                if (!launch){
                    System.out.println("Make sure to launch your robot before you do anything else!"
                            + " (use format 'launch (make) (robot_name)'");
                    //Robots
                }

                System.out.print("Enter Command: ");
                commandString = scanner.nextLine();

                RequestMessage requestMessage = HandleCommand(commandString);

                //Checks if username is blank and requestMessage is a launch message
                // Then assigns username if conditions meet
                checkUsername(requestMessage);

                    String jsonToSend = jsonHelper.RequestMessageToJson(requestMessage);
                    System.out.println(jsonToSend);
                    bufferedWriter.write(jsonToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    listenForMessage();
                }
            } catch (IOException e) {
                // Gracefully close everything.
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }).start();
    }

    // Listening for a message is blocking so need a separate thread for that.
    public void listenForMessage() {

            String jsonResponseFromServer;
            // While there is still a connection with the server, continue to listen for messages on a separate thread.
            if (socket.isConnected()) {
                try {
                    // Get the json response sent from the server.
                    jsonResponseFromServer = bufferedReader.readLine();
                    System.out.println("From server: "+jsonResponseFromServer);
                    ResponseMessage message = jsonHelper.JsonToResponseMessage(jsonResponseFromServer);


                    if(message.getResult().equalsIgnoreCase("ok") &&
                            ((SuccessResponseMessage)message).getState() != null){
                        this.state = ((SuccessResponseMessage)message).getState();
                    }
                    handleResponse(message);
                } catch (IOException e) {
                    // Close everything gracefully.
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            } else {
                throw new RuntimeException("You are no longer connected to the server.");
            }
    }

    private RequestMessage HandleCommand(String commandString) {

        return RequestMessage.createRequest(this.username, commandString);
    }

    private void handleResponse(ResponseMessage responseMessage){

        if (responseMessage.getData().containsKey("message")){
            switch( (String) responseMessage.getData().get("message")){
                case ("Invalid make provided"):
                    this.username = "";
                    this.launch = false;
                    System.out.println("Invalid make provided. Please make sure your formatting is correct.");
                    break;

                case ("Robot name already in use"):
                    this.username = "";
                    this.launch = false;
                    System.out.println("Another player has launched a robot with the same name.");
                    System.out.println("Please launch with a different name!");
                    break;

                case ("Robot already launched"):
                    System.out.println("You are only allowed a maximum of 1 robot at a time!");
                    break;

                case "shutdown":
                case  "off":
                case  "quit":
                    System.out.println("Shutting down...");
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    System.out.println("Thank you for playing Robot World!");
                    System.exit(0);
                    break;

                case "state":
                    printState();

                case "No robot launched":
                    System.out.println("\nInvalid launch command\n");
                    System.out.print("\nE.G launch spy Hal\n");
                    break;
                case "Reloaded":
                    doReload();
                    break;

                case "Turned left":
                    System.out.println("\nTurned left\n");
                    break;
                case "Turned right":
                    System.out.println("\nTurned right\n");
                    break;
                case "Your robot is dead.":
                    System.out.println("\\033[H\\033[2J");
                    System.out.println("\\u001B[1m You died!");
                    System.out.println("Thank you for playing Robot Worlds!");
                    System.out.println("If you'd like to play again, relaunch the program :)");
                    break;
                case "0 steps":
                    System.out.println("No movement done, 0 steps provided.");
                    break;
                case "Not enough arguments provided.":
                    System.out.println("Not enough arguments provided");
                    break;
                case "Too many arguments provided.":
                    System.out.println("Too many arguments provided.");
                    break;
                case "There is an obstacle in the way.":
                    System.out.println("Could not move the whole way, there is a obstacle in the way.");
                    break;
                case "There is a robot in the way.":
                    System.out.println("Could not move the whole way, there is a robot in the way.");
                    System.out.println(".\n.\n.\n");
                    System.out.println("WATCH OUT!");
                    break;
                case "Sorry, I cannot go outside my safe zone.":
                    System.out.println("Sorry, I cannot move outside of my safe zone");
                    System.out.println("We've got limits too you know...");
                    break;
                case "Invalid Steps":
                    System.out.println("Could not do any movement.");
                    System.out.println("Please make sure you use a valid number...");
                    break;
            }

            if (((String) responseMessage.getData().get("message")).toLowerCase().contains("moved forward")){
                System.out.println(((String) responseMessage.getData().get("message")));
            } else if (((String) responseMessage.getData().get("message")).toLowerCase().contains("moved back")) {
                System.out.println(((String) responseMessage.getData().get("message")));
            }

        } else if (responseMessage.getData().containsKey("position") &&
                    responseMessage.getData().containsKey("shield") &&
                    responseMessage.getData().containsKey("visibility")){

            Map<String, Object> responseData = responseMessage.getData();
            System.out.println("\nRobot launched! " +

                    "\n  Position: " + responseData.get("position") +
                    "\n  Direction: " + responseData.get("direction") +
                    "\n  Shots: " + responseData.get("shots") +
                    "\n  Shields: " + responseData.get("shield") +
                    "\n  Visibility: " + responseData.get("visibility") +
                    "\n  Position: " + responseData.get("position"));
            return;
        }

        // look response
        if (responseMessage.getData().containsKey("objects")){
            List<ObstacleResponse> lookList = (List<ObstacleResponse>) responseMessage.getData().get("objects");
            if(lookList.size() > 0){
                System.out.println("Robot look result: ");
                for (ObstacleResponse obstacle: lookList) {
                    System.out.println("\n  Direction: " + obstacle.getDirection()+
                            "\n  Distance: " + obstacle.getSteps() +
                            "\n  Type: " + obstacle.getTypeOfObject());
                }
            }else {
                System.out.println("Nothing around in visibility constraint");
            }
        }

        if (responseMessage instanceof SuccessResponseMessage &&
                ((SuccessResponseMessage)responseMessage).getState() != null){
            System.out.println();
            printState();
        }
    }

    private void printState() {

        System.out.println("This is the Robot's current state: ");
        System.out.println("Position: " + this.state.getPosition());
        System.out.println("Direction: " + this.state.getDirection());
        System.out.println("Shields: " + this.state.getShields());
        System.out.println("Shots: " + this.state.getShots());
        System.out.println("Status: " + this.state.getStatus());
        System.out.println();
    }

    public void printLaunch(){

    }

    private void repair(){

    }

    // Helper method to close everything so you don't have to repeat yourself.
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        // Note you only need to close the outer wrapper as the underlying streams are closed when you close the wrapper.
        // Note you want to close the outermost wrapper so that everything gets flushed.
        // Note that closing a socket will also close the socket's InputStream and OutputStream.
        // Closing the input stream closes the socket. You need to use shutdownInput() on socket to just close the input stream.
        // Closing the socket will also close the socket's input stream and output stream.
        // Close the socket after closing the streams.
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doReload(){
        System.out.println("RELOADING!");

        try
        {
            for (int i = 0; i < 7; i++) {
                System.out.print(".");
                Thread.sleep(500);
            }
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        System.out.println("\nDONE RELOADING!\n");
    }

    public void checkUsername(RequestMessage launchMessage){
        if (this.username.equalsIgnoreCase("") && launchMessage instanceof LaunchRequestMessage){
            this.username = (String) launchMessage.getArguments().get(1);
            this.launch = true;
        }
    }

    // Run the program.
    public static void main(String[] args) throws IOException {

        // Get a username for the user and a socket connection.
        Scanner scanner = new Scanner(System.in);



        System.out.print("Enter IP: ");
        String ip = scanner.nextLine();
        System.out.print("Enter port: ");
        String port = scanner.nextLine();

        while (!checkIpAndPort(ip, port)){
            System.out.println("Enter IP: ");
            ip = scanner.nextLine();
            System.out.println("Enter port: ");
            port = scanner.nextLine();
        }

        // Create a socket to connect to the server.
        int portn = Integer.parseInt(port);
        Socket socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(ip, portn);
        try {
            socket.connect(socketAddress, 0);
        } catch (SocketTimeoutException e) {
            System.out.println("Invalid IP, please contact Server host for IP and Port");
            RobotClient.main(new String[]{});
        }

        // Pass the socket and give the robotClient a username.
        RobotClient robotClient = new RobotClient(socket);
        // Infinite loop to read and send messages from the server.
        robotClient.sendMessage();
    }

}