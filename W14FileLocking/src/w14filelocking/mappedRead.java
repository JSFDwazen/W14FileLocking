/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package w14filelocking;

import GUI.JSF31KochFractalFX;
import TimeStamp.TimeStamp;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.paint.Color;

/**
 *
 * @author jsf3
 */
public class mappedRead {

    private final File fileMapped;
    private int level;
    private final List<Edge> edges;
    private final TimeStamp timeStamp;

    public static void main(String[] args) throws IOException {
        new mappedRead();
    }

    public mappedRead() throws IOException {
        this.edges = new ArrayList<>();
        this.fileMapped = new File("/media/Fractal/fileMapped.tmp");
        timeStamp = new TimeStamp();
        this.readFileMapped();
        System.out.println(timeStamp.toString());
        JSF31KochFractalFX.currentLevel = level;
        JSF31KochFractalFX.edges = this.edges;
        JSF31KochFractalFX.main(new String[0]);
    }

    public void readFileMapped() throws IOException {
        this.edges.clear();
        this.timeStamp.setBegin("begin readFileMapped");
        try (RandomAccessFile aFile = new RandomAccessFile(this.fileMapped.getAbsolutePath(), "r"); FileChannel inChannel = aFile.getChannel()) {
            MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            buffer.load();
            this.level = buffer.getInt();
            for (int i = 0; i < (int) (3 * Math.pow(4, level - 1)); i++) {
                double X1 = buffer.getDouble();
                double Y1 = buffer.getDouble();
                double X2 = buffer.getDouble();
                double Y2 = buffer.getDouble();
                String color = new Color(buffer.getDouble(), buffer.getDouble(), buffer.getDouble(), 1).toString();

                //create edge
                Edge edge = new Edge(X1, Y1, X2, Y2, color);
                this.edges.add(edge);
            }
            this.timeStamp.setEnd("eind readFileMapped");
            System.out.println("" + edges.size());
            //print read edges
            Scanner scanner = new Scanner(System.in);
            System.out.print("Wil je de edges tekenen? (y/n): ");
            if (scanner.next().equals("y")) {
                for (Edge edge : edges) {
                    System.out.println("-------");
                    System.out.println(edge.X1);
                    System.out.println(edge.Y1);
                    System.out.println(edge.X2);
                    System.out.println(edge.Y2);
                    System.out.println(edge.color);
                }
            }
            buffer.clear(); // do something with the data and clear/compact it.
        }
    }
}
