package de.wethinkco.robotworlds;



public interface DataAccessInterface {
    void saveWorld(World world);
    void updateWorld(World world);
    World restoreWorld(String worldName);
}
