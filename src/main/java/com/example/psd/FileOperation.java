package com.example.psd;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileOperation {

    //temp list
    private List<String> list ;

    //separator depending on system
    private final String separator = File.separator;

    /**
     * method to load all data into the file
     * @return an object data
     */
    public Data readFile() {
        ArrayList<String> dataLines = new ArrayList<>();
        try {
            File file = new File("files" + separator + "firstFile.txt");
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

    /**
     * method to write into the saving file
     * @param root is the root of tree
     */
    public void writeFile(Node root) {
        list = new ArrayList<>();

        if (root == null){
            System.out.println("No record on tree");
            return;
        }
        preOrder(root);
        try {
            FileWriter myWriter = new FileWriter("files" + separator + "savingFile.txt");
            myWriter.write(String.join("",list));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * method to extract decimal value into the file using regular expression
     * @param stringToSearch string that will be extracting
     * @return a list of string
     */
    public static List<String> findDecimalNums(String stringToSearch) {

        Pattern decimalNumPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Matcher matcher = decimalNumPattern.matcher(stringToSearch);
        List<String> decimalNumList = new ArrayList<>();

        while (matcher.find()) {
            decimalNumList.add(matcher.group());
        }

        return decimalNumList;
    }


    /**
     * method a print all node for a root to insert into the saving file
     * @param root defines the tree's root
     */
    private void preOrder(Node root) {
        if (root == null){
            return;
        }
        list.add(String.format("%s \n", root));
        preOrder(root.getLeftChild());
        preOrder(root.getRightChild());

    }
}
