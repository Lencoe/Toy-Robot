package de.wethinkco.robotworlds;

import de.wethinkco.robotworlds.helpers.JsonHelper;
import de.wethinkco.robotworlds.protocol.*;
import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.ServerConfig;
import de.wethinkco.robotworlds.protocol.world.World;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private String clientMake;
    private final JsonHelper jsonHelper = new JsonHelper();
    private final World world;
    private final Socket clientSocket;

    public ClientHandler(Socket socket, World world) {
        this.clientSocket = socket;
        this.world = world;
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            clientHandlers.add(this);
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String jsonFromClient;
        while (socket.isConnected()) {
            try {
                jsonFromClient = bufferedReader.readLine();
                System.out.println("Request from " + this.socket + ": " + jsonFromClient);

                if (jsonFromClient.equals("null")) {
                    Map<String, Object> mapData = new HashMap<>();
                    mapData.put("message", "Unsupported command");
                    ResponseMessage responseMessage = new ErrorResponseMessage("ERROR", mapData);
                    handleUnsupportedCommand(responseMessage);
                } else {
                    RequestMessage requestMessage = jsonHelper.JsonToRequestMessage(jsonFromClient);
                    if (requestMessage == null) {
                        Map<String, Object> mapData = new HashMap<>();
                        mapData.put("message", "Unsupported command");
                        ResponseMessage responseMessage = new ErrorResponseMessage("ERROR", mapData);
                        handleUnsupportedCommand(responseMessage);
                    } else {
                        handleRequestMessage(requestMessage);
                    }
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    private void handleRequestMessage(RequestMessage requestMessage) {
        ResponseMessage responseMessage = null;
        Robot robot = this.world.getRobotByName(this.clientUsername);
        if (requestMessage.getRobot().equals("") && !(requestMessage instanceof LaunchRequestMessage)) {
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("message", "No robot launched");
            responseMessage = new ErrorResponseMessage("ERROR", mapData);
        } else if (!(robot == null) && robot.isDead()) {
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("message", "Your robot is dead.");
            responseMessage = new ErrorResponseMessage("ERROR", mapData);
            closeEverything(socket, bufferedReader, bufferedWriter);
        } else {
            if (!(robot == null) && robot.isReloading()) {
                robot.setStatus(RobotStatus.NORMAL);
            }
            responseMessage = requestMessage.execute(robot, this.world);

            if (requestMessage instanceof LaunchRequestMessage
                    && responseMessage instanceof SuccessResponseMessage) {
                this.clientUsername = (String) requestMessage.getArguments().get(1);
            }
        }

        String jsonToSend = jsonHelper.ResponseMessageToJson(responseMessage);

        try {
            this.bufferedWriter.write(jsonToSend);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

        if (requestMessage instanceof ShutdownQuitOffRequestMessage
                && responseMessage instanceof SuccessResponseMessage) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void handleUnsupportedCommand(ResponseMessage errorResponse) {
        String jsonToSend = jsonHelper.ResponseMessageToJson(errorResponse);

        try {
            this.bufferedWriter.write(jsonToSend);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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

    @Override
    public String toString() {
        return "ClientHandler{" +
                "clientUsername='" + clientUsername + '\'' +
                ", robots=" + world.getRobots() +
                '}';
    }
}
