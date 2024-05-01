package de.wethinkco.robotworlds;
import de.wethinkco.robotworlds.protocol.robots.Robot;
import de.wethinkco.robotworlds.protocol.world.ServerConfig;

import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnect {
    private static final String DATABASE_URL = "jdbc:sqlite:robot_world.db";

    // Table names

    private static final String TABLE_WORLDS = "Worlds";

    // Column names for WorldConfig
    private static final String COLUMN_WORLD_WIDTH = "width";
    private static final String COLUMN_WORLD_HEIGHT = "height";

    // Column names for Worlds
    private static final String COLUMN_WORLD_NAME = "name";
    private static final String COLUMN_OBSTACLES = "obstacles";
    private static final String COLUMN_PITS = "pits";
    private static final String COLUMN_MINES = "mines";

    private static  Connection connection = null;

    private static String obstacles = null;
    private static int height = 0;
    private  static int width = 0;

    public static  Connection connection(){

        try{
            if(connection == null || connection.isClosed()){
                connection = DriverManager.getConnection(DATABASE_URL);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }return  connection;
    }

    public static void disconnect(){

        try {
            if(connection()!= null && !connection().isClosed()){
                connection().close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }




    public static void saveWorldWithName(String worldName, int pits, int mines) {
        Robot robot = new Robot();
        String obstaclesList = robot.getObstacles().toString();
        boolean nameExists = true;
        Scanner scanner = new Scanner(System.in);
        ServerConfig serverConfig = Server.getConfig();
        int height = Integer.parseInt(serverConfig.getGridSize().toString().split(" ")[1].split(",")[0]);
        int width = Integer.parseInt(serverConfig.getGridSize().toString().split(" ")[3].split("}")[0]);

        while (nameExists) {
            // Check if the world with the given name already exists
            try (Connection conn = connection();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "SELECT * FROM " + TABLE_WORLDS + " WHERE " + COLUMN_WORLD_NAME + " = ?")) {
                pstmt.setString(1, worldName);
                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    System.out.println("A world with the name '" + worldName + "' already exists. Do you want to overwrite it? (Y/N)");
                    String userInput = scanner.nextLine().trim().toLowerCase();

                    if (userInput.equals("y")) {
                        updateWorld(worldName, obstaclesList, pits, mines);
                        nameExists = false;
                    }
                    if (userInput.equals("n")){
                        System.out.println("Name of your world");
                        worldName = scanner.nextLine().trim();
                        try (PreparedStatement insertStmt = conn.prepareStatement(
                                "INSERT INTO " + TABLE_WORLDS + " (" + COLUMN_WORLD_NAME + ", " + COLUMN_OBSTACLES + ", " + COLUMN_PITS + ", " + COLUMN_MINES + ", " + COLUMN_WORLD_HEIGHT+ ", " + COLUMN_WORLD_WIDTH + ") VALUES (?, ?, ?, ?, ?, ?)")) {
                            insertStmt.setString(1, worldName);
                            insertStmt.setString(2, obstaclesList);
                            insertStmt.setInt(3, pits);
                            insertStmt.setInt(4, mines);
                            insertStmt.setInt(5, height);
                            insertStmt.setInt(6, width);
                            insertStmt.executeUpdate();
                            System.out.println("World configuration saved with name: " + worldName);
                            nameExists = false;
                        }
//                        worldName = scanner.nextLine().trim();
                    }
                } else {
                    // Insert the new world
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO " + TABLE_WORLDS + " (" + COLUMN_WORLD_NAME + ", " + COLUMN_OBSTACLES + ", " + COLUMN_PITS + ", " + COLUMN_MINES + ", " + COLUMN_WORLD_HEIGHT+ ", " + COLUMN_WORLD_WIDTH + ") VALUES (?, ?, ?, ?, ?, ?)")) {
                        insertStmt.setString(1, worldName);
                        insertStmt.setString(2, obstaclesList);
                        insertStmt.setInt(3, pits);
                        insertStmt.setInt(4, mines);
                        insertStmt.setInt(5, height);
                        insertStmt.setInt(6, width);
                        insertStmt.executeUpdate();
                        System.out.println("World configuration saved with name: " + worldName);
                        nameExists = false;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                disconnect();
            }
        }
    }


    private static void updateWorld(String worldName, String obstacles, int pits, int mines) {
        // Update the existing world with the new values
        ServerConfig serverConfig = Server.getConfig();
        int height = Integer.parseInt(serverConfig.getGridSize().toString().split(" ")[1].split(",")[0]);
        int width = Integer.parseInt(serverConfig.getGridSize().toString().split(" ")[3].split("}")[0]);
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE " + TABLE_WORLDS + " SET " + COLUMN_OBSTACLES + " = ?, " + COLUMN_PITS + " = ?, " + COLUMN_MINES + " = ?," + COLUMN_WORLD_HEIGHT + " = ?," + COLUMN_WORLD_WIDTH + " = ?  WHERE " + COLUMN_WORLD_NAME + " = ?")) {
            pstmt.setString(1, obstacles);
            pstmt.setInt(2, pits);
            pstmt.setInt(3, mines);
            pstmt.setInt(4, height);
            pstmt.setInt(5, width);
            pstmt.setString(6, worldName);
            pstmt.executeUpdate();
            System.out.println("World configuration updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public String getObstacles() {
        return this.obstacles;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setObstacles(String obstacles) {
        this.obstacles = obstacles;
    }

    public static void restoreWorld(String worldName) {
        try (Connection conn = connection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT " + COLUMN_WORLD_WIDTH + ", " + COLUMN_WORLD_HEIGHT + ", " + COLUMN_OBSTACLES + ", " + COLUMN_PITS + ", " + COLUMN_MINES + " FROM " + TABLE_WORLDS  + " WHERE " + COLUMN_WORLD_NAME + " = ?")) {
            pstmt.setString(1, worldName);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                int width = resultSet.getInt(COLUMN_WORLD_WIDTH);
                int height = resultSet.getInt(COLUMN_WORLD_HEIGHT);
                String obstacles = resultSet.getString(COLUMN_OBSTACLES);
                int pits = resultSet.getInt(COLUMN_PITS);
                int mines = resultSet.getInt(COLUMN_MINES);
                DbConnect dbConnect = new DbConnect();
                dbConnect.setHeight(height);
                dbConnect.setWidth(width);
                dbConnect.setObstacles(obstacles);
                System.out.println("Restored world configuration for '" + worldName + "': width=" + width + ", height=" + height + ", obstacles=" + obstacles + ", pits=" + pits + ", mines=" + mines);
            } else {
                System.out.println("No world configuration found in the database for '" + worldName + "'. Please enter another name:");
                Scanner scanner = new Scanner(System.in);
                String newName = scanner.nextLine();
                restoreWorld(newName); // Recursively call the method with the new name provided by the user.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            disconnect();
        }
    }

}
