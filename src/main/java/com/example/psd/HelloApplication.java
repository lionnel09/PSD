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

            switch (option) {


                case 1 -> fileOperation.writeFile(kdTree.getRoot());

                case 2 -> {
                    kdTree.insert(pointToInsert());
                    kdTree.draw();
                }

                case 3 -> kdTree.draw();

                case 4 -> out.printf("There is %s Node into the tree \n", kdTree.getRoot().count());

                case 5 -> out.println(kdTree.findMin(menuMinMax(data) - 1) + "\n");

                case 6 -> out.println(kdTree.findMax(menuMinMax(data) - 1)+ "\n" );

                case 7 -> {
                    RectangularHalfPlane rectangularHalfPlane = comparatorMenu(optionComparingMenu(data));
                    if (rectangularHalfPlane != null)
                        kdTree.printRange(rectangularHalfPlane);
                }

                default -> out.println("Good bye");
            }
        }while (option != 0);

    }

    /**
     * application title
     */
    private static void title(){
        out.println("""
                
                
                \tWelcome your 2D TREE LOADER
                
                Choose one operation in the menu :
                
                """);
    }

    /**
     * contains three options first option to range on X ; second option to range on Y , third option to range on X an Y
     * @param input user input to specify with will be opened
     * @return a RectangularHalfPlane object containing all searches range search
     */
    private static RectangularHalfPlane comparatorMenu(int input){
        MyComparatorRangeValue comparatorValue;
        switch (input) {
            case 1 -> {
                comparatorValue = compareValue();
                if (comparatorValue.isValid())
                    return new RectangularHalfPlane(comparatorValue.getLower(),-Double.MAX_VALUE, comparatorValue.getUpper(),Double.MAX_VALUE);
                out.println("invalid input");
            }
            case 2 -> {
                comparatorValue = compareValue();
                if (comparatorValue.isValid())
                    return new RectangularHalfPlane(-Double.MAX_VALUE, comparatorValue.getLower(), Double.MAX_VALUE, comparatorValue.upper);
                out.println("invalid input");
            }
            case 3 -> {
                out.println("X range : ");
                comparatorValue = compareValue();
                out.println("Y range");
                MyComparatorRangeValue comparatorValue1;
                comparatorValue1 = compareValue();
                return new RectangularHalfPlane(comparatorValue.getLower(), comparatorValue1.getLower(), comparatorValue.getUpper(), comparatorValue1.upper);
            }
            default -> throw new IllegalStateException("Unexpected value: " + input);
        }
        return null;
    }

    /**
     * to print the main menu
     * @param data object received after loading the file
     */
    private static void menu(Data data) {
        out.printf("""
                1.Save the Tree
                2.Insert new point into Three
                3.Draw the Tree
                4.Count all Nodes into the Tree
                5.Find the min by criteria (1 for %s , 2 for %s)
                6.Find the max by criteria (1 for %s , 2 for %s)
                7.Compare by criteria (1 for %s , 2 for %s)
                0.exit
                %n""",data.getC1(),data.getC2(),data.getC1(),data.getC2(),data.getC1(),data.getC2());
    }

    /**
     * method select in which criteria we want to compare
     * @param data object received after loading the file
     * @return the selected option
     */
    private static int optionComparingMenu(Data data){
        Scanner input = new Scanner(in);
        out.printf("""
               1.%s
               2.%s
               3.%s and %s
               %n""",data.getC1(),data.getC2(),data.getC1(),data.getC2());

        return input.nextInt();
    }


    /**
     * method get all point of range we want to search
     * @return a MyComparatorRangeValue record
     */
    private static MyComparatorRangeValue compareValue(){
        Scanner input = new Scanner(in);
        String upperStr,lowerStr;
        double upper , lower;
        out.println("Lower: (press enter to pass)");
        lowerStr = input.nextLine();
        if(lowerStr.equals("")) lower = - Double.MAX_VALUE;
        else lower = Double.parseDouble(lowerStr);

        out.println("Upper: (press enter to pass)");
        upperStr = input.nextLine();
        if(upperStr.equals("")) upper = Double.MAX_VALUE;
        else upper = Double.parseDouble(upperStr);

        return new MyComparatorRangeValue(upper,lower);
    }

    /**
     * get on which criteria we want min or max 1 for first criteria , 2 for second criteria
     * @param data object received after loading the file
     * @return user selection
     */
    private static int menuMinMax(Data data){
        Scanner input = new Scanner(in);

       out.printf("""
               1.%s
               2.%s
               %n""",data.getC1(),data.getC2());
       return input.nextInt();
    }

    /**
     * to get value x and y of the point to insert
     * @return the point to insert
     */
    private static Point2D pointToInsert(){
        Scanner input = new Scanner(in);
        String x , y;

        out.println("X:");
        x = input.nextLine();
        out.println("Y:");
        y = input.nextLine();

        return new Point2D.Double(Double.parseDouble(x),Double.parseDouble(y));
    }

    /**
     * a method to get min and max range
     * @param upper max range
     * @param lower min range
     */
    record MyComparatorRangeValue(Double upper, Double lower) {

        public Double getUpper() {
            return upper;
        }

        public Double getLower() {
            return lower;
        }

        public boolean isValid(){
            return lower <= upper;
        }
    }

}