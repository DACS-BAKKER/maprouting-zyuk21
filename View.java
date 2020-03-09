/*
 File: View.java
 Names: Alex Yuk
 Date: March 6th, 2020
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

public class View extends JPanel implements Runnable {

    private final String IMAGE_DIRECTORY = "usamap.png";
    private static final int APPLICATION_WIDTH = 1000;
    private static final int APPLICATION_HEIGHT = 423;
    private static final int RATIO = 10;

    private static JFrame f;
    private BufferedImage image;

    // 2D array containing information for graphics
    private Node[][] arr = new Node[10000 + 1][4235 + 1];

    // See if first location has been selected
    private boolean selected = false;
    // See if second location has been selected
    private boolean pathFound = false;

    // Keeps track of first selected location
    private Location firstLocation;
    // Keeps track of path taken
    private LinkedList<WeightedGraph.Vertex> path;

    private static Solver S;

    public static void main(String[]args) {
        System.out.println("Pathfinder for the US Map");
        System.out.println("Please read bottom left of window for instructions");
        S = new Solver();
        SwingUtilities.invokeLater(View::start);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws image of US Map
        g.drawImage(image, -23, -20, 1040, 454, null);

        // Prints instructions
        if (pathFound) {
            g.drawString("Click to reset", 50, 330);
            g.drawString(path.get(0).index + " to " + path.get(path.size()- 1).index, 50, 350);
            g.drawString("Distance: " + path.get(0).weight, 50, 370);
            g.drawString("Vertices: " + path.size(), 50, 390);
            g.drawString("See path in console", 50, 410);
        }
        else if (!selected) {
            g.drawString("Select First Location", 50, 350);
        } else {
            g.drawString("Select Second Location", 50, 350);
        }

        System.out.println("Repaint");

        // Paints all locations
        g.setColor(Color.BLACK);
        for (int i = 0; i < 10001; i++) {
            for (int j = 0; j < 4236; j++) {
                if (arr[i][j] == null) continue;
                if (arr[i][j].color == 1)
                    g.drawRect(i / RATIO, j / RATIO, 1, 1);
            }
        }

        // Paints selected locations
        for (int i = 0; i < 10001; i++) {
            for (int j = 0; j < 4236; j++) {
                if (arr[i][j] == null) continue;
                if (arr[i][j].color == 2) {
                    g.setColor(Color.RED);
                    g.fillOval(i  / RATIO - 4, j / RATIO - 4 , 9, 9);
                } else if (arr[i][j].color == 3) {
                    g.setColor(Color.GREEN);
                    g.fillOval(i / RATIO - 4, j / RATIO - 4, 9, 9);
                }

            }
        }

        // Paints path if path is found
        if (pathFound) {
            g.setColor(Color.PINK);
            WeightedGraph.Vertex temp = path.get(0);
            for (int i = 0; i < path.size(); i++) {
                g.drawLine(temp.x / RATIO, flipY(temp.y) / RATIO, path.get(i).x / RATIO, flipY(path.get(i).y) / RATIO);
                g.drawLine(temp.x / RATIO, flipY(temp.y) / RATIO - 1, path.get(i).x / RATIO, flipY(path.get(i).y) / RATIO - 1);
                g.drawLine(temp.x / RATIO - 1, flipY(temp.y) / RATIO, path.get(i).x / RATIO - 1, flipY(path.get(i).y) / RATIO);
                temp = path.get(i);
            }
        }
    }

    // Flips Y coordinate as JSwing starts (0,0) from top left and file does it from bottom left
    private int flipY(int num) {
        return 4235 - num;
    }

    // Constructor
    private View() {
        f.addMouseListener(ML);
        loadArr();
        loadImage();
        setPreferredSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));
        setFocusable(true);

    }

    // Loads image used in GUI
    private void loadImage() {
        try {
            image = ImageIO.read(new File(IMAGE_DIRECTORY));
        } catch (IOException ignored) {
        }
    }

    // JFrame settings
    private static void start() {
        f = new JFrame();
        f.setTitle("Pathfinder");
        f.setResizable(true);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        f.add(new View(), BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }

    // Loads information form read graph into 2D array for graphics
    private void loadArr() {
        for (int i = 0; i < 87575; i++) {
            arr[S.graph.adjList[i].x][flipY(S.graph.adjList[i].y)] = new Node(i, 1);
        }
    }

    MouseListener ML = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            // Resets selection and colors when path as been found
            if (pathFound && selected) {
                reset();
            }
            else {
                int x = mouseEvent.getX() * RATIO;
                int y = mouseEvent.getY() * RATIO - 250;
                System.out.println("XY: " + x + " " + y);
                // Two for loops used to check a 10 by 10 box around the click so users click doesn't have to be pixel perfect
                for (int i = 1; i < 10; i++) {
                    x++;
                    for (int j = 1; j < 10; j++) {
                        y++;
                        // If click is in bounds
                        if (x >= 0 && x <= 10000 && y >= 0 && y <= 4235 && arr[x][y] != null) {
                            if (!selected) {
                                arr[x][y].color = 2;
                                firstLocation = new Location(x, y);
                                selected = !selected;
                            }
                            else {
                                arr[x][y].color = 3;
                                path = S.graph.dijkstra(arr[firstLocation.x][firstLocation.y].index, arr[x][y].index);
                                pathFound = true;
                            }
                            i = 10;
                            break;
                        }
                    }
                    y -= 9;
                }
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
        }
    };

    // Resets selection and counters
    private void reset() {
        for (int i = 0; i < 10001; i++) {
            for (int j = 0; j < 4236; j++) {
                if (arr[i][j] == null) continue;
                if (arr[i][j].color == 2 || arr[i][j].color == 3)
                    arr[i][j].color = 1;
            }
        }
        selected = !selected;
        pathFound = !pathFound;
    }

    // Location class containing x and y coordinate
    private static class Location {
        int x;
        int y;
        Location(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Node class containing
    private static class Node {
        int index;
        int color;

        Node(int index, int color) {
          this.index = index;
          this.color = color;
        }
    }

    @Override
    public void run() {
    }

}