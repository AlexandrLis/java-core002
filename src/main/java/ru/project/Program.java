package ru.project;

import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static final int WIN_COUNT = 4;
    private static final char  DOT_HUMAN = 'x';
    private static final char  DOT_AT = 'o';
    private static final char  DOT_EMPTY = '*';
    private  static int fieldSizeX;
    private  static int fieldSizeY;
    private static char[][] field;

    public static void main(String[] args) {

        while (true){
            initialize();
            printField();
            while (true) {
                int[] arr = humanTurn();
                int x = arr[0];
                int y = arr[1];
                printField();
                if(checkState(x, y, DOT_HUMAN, "Вы победили!!!")){
                    break;
                }
                aiTurn();
                printField();
                if(checkState(x, y, DOT_AT, "Победил компьютер")){
                    break;
                }
            }
            System.out.println("Желаете сыграть еще раз? (Y - да)");
            if(!scanner.next().equalsIgnoreCase("Y")){
                break;
            }
        }
    }

    //  создаем игровое поле
    static void initialize(){
        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeX; y++){
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    //  печатаем игровое поле
    static void printField(){
        System.out.print("+");
        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print("-" + (x + 1));
        }
        System.out.println("-");

        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeX; y++){
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for (int x = 0; x < fieldSizeX * 2 + 2; x++) {
            System.out.print("-");
        }
        System.out.println();
    }

//    ход игрока(человека)
    static  int[] humanTurn(){
        int x;
        int y;
        int[] array = new int[2];
        do {
            System.out.println("Введите координаты хода X и Y\n(от 1 до 5) через пробел");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
            array[0] = x;
            array[1] = y;
        }
        while (!isCellValid(x, y) || !(isCellEmpty(x, y)));
        field[x][y] = DOT_HUMAN;
        return array;
    }

//  является ли ячейка поля пустрой?
    static boolean isCellEmpty(int x, int y){

        return field[x][y] == DOT_EMPTY;
    }

//    проверка валидности хода
    static boolean isCellValid(int x, int y){

        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

//  ход компьютера
    static  void aiTurn(){
        int x;
        int y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!(isCellEmpty(x, y)));
        field[x][y] = DOT_AT;
    }

//  проверка на ничью
    static  boolean checkDraw(){
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeX; y++){
                if(isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

//    метод проверки победы
    static  boolean checkWin(int x, int y, char dot){

        if(checkGorizont(dot)) return true;
        if(checkVertical(dot)) return true;
        if(checkDiagLowRight(dot)) return true;
        if(checkDiagLowLeft(dot)) return true;

        return false;
    }

    //  проверка по горизонтали
    static boolean checkGorizont(char dot){
        int count = 0;
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY - 1; j++) {
                if(field[i][j] == dot && field[i][j] == field[i][j + 1]){
                    count++;
                }
                else {
                    count = 0;
                }
                if(count == WIN_COUNT - 1) return true;
            }
        }
        return false;
    }

    //  проверка по вертикали
    static boolean checkVertical(char dot){
        int count = 0;
        for (int j = 0; j < fieldSizeY; j++) {
            for (int i = 0; i < fieldSizeX - 1; i++) {
                if(field[i][j] == dot && field[i][j] == field[i + 1][j]){
                    count++;
                }
                else {
                    count = 0;
                }
                if(count == WIN_COUNT - 1) return true;
            }
        }
        return false;
    }



    //  проверка по диагонали вниз-вправо
    static boolean checkDiagLowRight(char dot){

        int count = 0;
        for (int k = 0; k < fieldSizeX; k++) {
            for (int q = 0; q < fieldSizeY; q++) {
                for (int i = k, j = q; i < fieldSizeX - 1 && j < fieldSizeY - 1; i++, j++) {
                    if(field[i][j] == dot && field[i][j] == field[i + 1][j + 1]){
                        count++;
                    }
                    else {
                        count = 0;
                    }
                    if(count == WIN_COUNT - 1) return true;
                }
            }
        }

        return false;
    }

    //  проверка по диагонали вниз-влево
    static boolean checkDiagLowLeft(char dot){

        int count = 0;
        for (int k = 0; k < fieldSizeX - 1; k++) {
            for (int q = fieldSizeY - 1; q > 0; q--) {
                for (int i = k, j = q; i < fieldSizeX - 1 && j > 0; i++, j--) {
                    if(field[i][j] == dot && field[i][j] == field[i + 1][j - 1]){
                        count++;
                    }
                    else {
                        count = 0;
                    }
                    if(count == WIN_COUNT - 1) return true;
                }
            }
        }

        return false;
    }




//    проверка состояния игры
    static boolean checkState(int x, int y, char dot, String s){

        if(checkWin(x, y, dot)){
            System.out.println(s);
            return true;
        }
        else if(checkDraw()){
            System.out.println("Ничья!!!");
            return true;
        }
        else return false;
    }

}


