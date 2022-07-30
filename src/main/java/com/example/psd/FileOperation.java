package com.example.psd;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileOperation {

    private List<String> list ;

    public Data readFile() {
        ArrayList<String> dataLines = new ArrayList<>();
        try {

            File file = new File("files/firstFile.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                dataLines.add(line);
            }
            fr.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Data data = new Data();
        List<Point2D> tempPoints = new ArrayList<>();
        int i = 0;
        for (String dataLine : dataLines) {
            if (i == 0) {
                data.setC1(dataLine);
            } else if (i == 1) {
                data.setC2(dataLine);
            } else if (i == 2) {
                data.setN(Integer.parseInt(dataLine));
            } else {
                List<Double> tempSegments;
                tempSegments = findDecimalNums(dataLine)
                        .stream().mapToDouble(Double::valueOf).boxed().toList();
                tempPoints.add(new Point2D.Double(tempSegments.get(0), tempSegments.get(1)));
            }
            i++;

        }
        data.setPoints(tempPoints);

        return data;
    }

    public void writeFile(Node node) {
        list = new ArrayList<>();

        if (node == null){
            System.out.println("No record on tree");
            return;
        }
        preOrder(node);
        try {
            FileWriter myWriter = new FileWriter("/Users/lionnel/Downloads/PSD/files/recorder.txt");
            myWriter.write(String.join("",list));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static List<String> findDecimalNums(String stringToSearch) {

        Pattern decimalNumPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Matcher matcher = decimalNumPattern.matcher(stringToSearch);
        List<String> decimalNumList = new ArrayList<>();

        while (matcher.find()) {
            decimalNumList.add(matcher.group());
        }

        return decimalNumList;
    }

    private void preOrder(Node node) {
        if (node == null){
            return;
        }
        list.add(String.format("%s \n", node));
        preOrder(node.getLeftChild());
        preOrder(node.getRightChild());

    }
}
