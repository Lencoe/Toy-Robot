package de.wethinkco.robotworlds;


public class World {
    private String name;
    private String obstacles;
    private int pits;
    private int mines;
    private int height;
    private int width;

    public World() {
        // Default constructor
    }

    public World(String name, String obstacles, int pits, int mines, int height, int width) {
        this.name = name;
        this.obstacles = obstacles;
        this.pits = pits;
        this.mines = mines;
        this.height = height;
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObstacles() {
        return obstacles;
    }

    public void setObstacles(String obstacles) {
        this.obstacles = obstacles;
    }

    public int getPits() {
        return pits;
    }

    public void setPits(int pits) {
        this.pits = pits;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
