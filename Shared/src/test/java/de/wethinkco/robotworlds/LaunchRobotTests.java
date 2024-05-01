package de.wethinkco.robotworlds;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

/**
 * As a player
 * I want to launch my robot in the online robot world
 * So that I can break the record for the most robot kills
 */
class LaunchRobotTests {
    private final static int DEFAULT_PORT = 5000;
    private final static String DEFAULT_IP = "localhost";
    private final RobotWorldClient serverClient = new RobotWorldJsonClient();

    @BeforeEach
    void connectToServer(){
        serverClient.connect(DEFAULT_IP, DEFAULT_PORT);
    }

    @AfterEach
    void disconnectFromServer(){
        serverClient.disconnect();
    }
    @Test
    void validLaunchShouldSucceed(){
        // Given that I am connected to a running Robot Worlds server
        // And the world is of size 1x1 (The world is configured or hardcoded to this size)
        assertTrue(serverClient.isConnected());

        // When I send a valid launch request to the server
        String request = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"spy\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get a valid response from the server
        assertNotNull(response.get("result"));
        System.out.println(response);
        assertEquals("OK", response.get("result").asText());

        // And the position should be (x:0, y:0)
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("position"));
        assertEquals(0, response.get("data").get("position").get(0).asInt());
        assertEquals(0, response.get("data").get("position").get(1).asInt());

        // And I should also get the state of the robot
        assertNotNull(response.get("state"));
    }
    @Test
    void invalidLaunchShouldFail(){
        // Given that I am connected to a running Robot Worlds server
        assertTrue(serverClient.isConnected());

        // When I send a invalid launch request with the command "luanch" instead of "launch"
        String request = "{" +
                "\"robot\": \"HAL\"," +
                "\"command\": \"luanch\"," +
                "\"arguments\": [\"spy\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(request);

        // Then I should get an error response
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        // And the message "Unsupported command"
        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertTrue(response.get("data").get("message").asText().contains("Unsupported command"));
    }
    @Test
    void worldWithObstacleIsFull() {
        for (int i = 0; i <= 8; i++) {
            String launchRequest = "{" +
                    "  \"robot\": \"HAL"+ i+"\"," +
                    "  \"command\": \"launch\"," +
                    "  \"arguments\": [\"spy\",\"5\",\"5\"]" +
                    "}";
            System.out.println(launchRequest);

            JsonNode response1 =  serverClient.sendRequest(launchRequest);
            System.out.println(response1);

        }

        // When I launch one more robot
        String invalidLaunchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"spy\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(invalidLaunchRequest);

        // Then I should get an error response back with the message "No more space in this world"
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        System.out.println(response);
        System.out.flush();
        assertEquals("No more space in this world", response.get("data").get("message").asText());
    }
    @Test
    void worldWithoutObstacleIsFull() {
        // Given a world of size 2x2
       // And I have successfully launched 9 robots into the world
        for (int i = 0; i <= 9; i++) {
            String launchRequest = "{" +
                    "  \"robot\": \"HAL" + i + "\"," +
                    "  \"command\": \"launch\"," +
                    "  \"arguments\": [\"spy\",\"5\",\"5\"]" +
                    "}";
            serverClient.sendRequest(launchRequest);
        }

        // When I launch one more robot
        String invalidLaunchRequest = "{" +
                "  \"robot\": \"HAL\"," +
                "  \"command\": \"launch\"," +
                "  \"arguments\": [\"spy\",\"5\",\"5\"]" +
                "}";
        JsonNode response = serverClient.sendRequest(invalidLaunchRequest);

        // Then I should get an error response back with the message "No more space in this world"
        assertNotNull(response.get("result"));
        assertEquals("ERROR", response.get("result").asText());

        assertNotNull(response.get("data"));
        assertNotNull(response.get("data").get("message"));
        assertEquals("No more space in this world", response.get("data").get("message").asText());
    }



}