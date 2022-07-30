package com.example.psd;


import java.awt.geom.Point2D;
import java.util.Scanner;

import static java.lang.System.*;

public class HelloApplication {

    public static void main(String[] args) {
        Scanner input = new Scanner(in);
        int option;
        FileOperation fileOperation = new FileOperation();
        Data data = fileOperation.readFile();
        KDTree kdTree = new KDTree();
        kdTree.buildTree(data);


        title();
        do{
            menu(data);
            option = input.nextInt();

            switch (option){

                case 1:
                    fileOperation.writeFile(kdTree.getRoot());
                    break;

                case 2:
                    kdTree.insert(pointToInsert());
                    kdTree.displayTree();
                    break;

                case 3:
                    kdTree.displayTree();
                    break;

                case 4:
                    out.printf("There is %s Node into the tree%n",kdTree.getRoot().count());
                    break;
                case 5:
                    out.println(kdTree.findMin(menuMinMax(data) - 1));
                    break;

                case 6:
                    out.println(kdTree.findMax(menuMinMax(data) - 1));
                    break;

                case 7:

                    break;

                default :
                    out.println("invalid input");
                    break;
            }
        }while (option != 0);


    }

    private static void title(){
        out.println("""
                
                
                \tWelcome your KD TREE LOADER
                
                Choose one operation in the menu :
                
                """);
    }

    private static void menu(Data data){
        out.printf("""
                1.Save the Tree
                2.Insert new point into Three
                3.Print the Tree
                4.Count all Nodes into the Tree
                5.Find the min by criteria (1 for %s , 2 for %s)
                6.Find the max by criteria (1 for %s , 2 for %s)
                7.Find range of point by criteria
                0.exit
                %n""",data.getC1(),data.getC2(),data.getC1(),data.getC2());
    }

    private static int menuMinMax(Data data){
        Scanner input = new Scanner(in);

       out.printf("""
               1.%s
               2.%s
               %n""",data.getC1(),data.getC2());
       return input.nextInt();
    }

    private static Point2D pointToInsert(){
        Scanner input = new Scanner(in);
        String x , y;

        out.println("X:");
        x = input.nextLine();
        out.println("Y:");
        y = input.nextLine();

        return new Point2D.Double(Double.parseDouble(x),Double.parseDouble(y));

    }

}