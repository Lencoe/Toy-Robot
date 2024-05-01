package de.wethinkco.robotworlds;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.wethinkco.robotworlds.protocol.world.IObstacle;
import de.wethinkco.robotworlds.protocol.world.ServerConfig;
import de.wethinkco.robotworlds.protocol.world.SquareObstacle;
import de.wethinkco.robotworlds.protocol.world.World;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Server commands:
 * Quit - Ends the game and closes all sockets
 * Robots - How many robots are playing
 * Dump - Can see everything that's in the World.
 */
public class Server implements Runnable {

    private static final int PORT = 5000;
    private final ServerSocket serverSocket;
    private World world;

    public void setWorld(World world) {

    this.world = world;
    }

    public World getWorld(){
        return world;
    }

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        Server server = new Server(serverSocket);
        Thread serverThread = new Thread(server);
        serverThread.start();
        Scanner in = new Scanner(System.in);
        System.out.println("Enter your command:");
        String command = in.nextLine();
        while(!command.equalsIgnoreCase("quit")){

            //handle command here
            if(command.equalsIgnoreCase("robots")){
                for (ClientHandler client :
                        ClientHandler.clientHandlers) {
                    System.out.println(client.toString());
                }
            }
            if(command.equalsIgnoreCase("dump")){
                System.out.println(server.getWorld().toString());
            }

            if (command.equalsIgnoreCase("save")){

                System.out.println("Name of your world:");
                String name = in.nextLine();

                DbConnect.saveWorldWithName(name,5, 5);


            }

            if (command.equalsIgnoreCase("restore")){
                System.out.println("Name of your world:");
                String name = in.nextLine();
                DbConnect dbConnect = new DbConnect();
                DbConnect.restoreWorld(name);
                String obstacles = dbConnect.getObstacles();
                int width = dbConnect.getWidth();
                int height = dbConnect.getHeight();
//                setWorld(new World(obstacles, width, height));

            }
            System.out.println("Enter your command:");
            command = in.nextLine();
        }
        server.closeServerSocket();
    }

    public void startServer() {
        try {
            List<IObstacle> list = new ArrayList<IObstacle>();

            // Listen for connections (clients to connect) on port 1234.
            IObstacle obstacle = new SquareObstacle(1,1);
            list.add(obstacle);
            getConfig();
            world = new World(list);

            while (!serverSocket.isClosed()) {
                // Will be closed in the RobotClient Handler.
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket, world);
                Thread thread = new Thread(clientHandler);
                // The start method begins the execution of a thread.
                // When you call start() the run method is called.
                // The operating system schedules the threads.
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    // Close the server socket gracefully.
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startServer();
    }

    public static ServerConfig getConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//            String projectRootPath = Paths.get("").toAbsolutePath().toString();
            String projectRootPath = System.getProperty("user.dir");
            String configFilePath = projectRootPath.concat("/Configuration.json");
            ServerConfig serverConfig = mapper.readValue(Paths.get(configFilePath).toFile(), ServerConfig.class);
            return serverConfig;
        } catch (Exception ex) {

            System.out.println("Failed to read from Server Config: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}