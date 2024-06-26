package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.locks.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Hello world!
 *
 */
public class App 
{

    public static final String PATH_TO_OUTPUT = "c:/test/";
    public static final String OUTPUT_FILE_NAME = "out.txt";
    public static final Lock LOCK = new ReentrantLock();
    public static Integer inputInt;
    public static void main( String[] args ) throws InterruptedException {
       if (checkedCreateFolderOrFile()) {
           Scanner scanner = new Scanner(System.in);
           do {
               System.out.println("Введите цело число больше 0 и кратное 2");
               inputInt = Integer.valueOf(scanner.nextLine());
           } while (inputInt % 2 !=0);

           Thread thread1 = new Thread(new Worker("Thread1"));
           Thread thread2 = new Thread(new Worker("Thread2"));

           thread1.start();
           thread2.start();
           thread1.join();
           thread2.join();

           System.out.println("File content: " + readFromFile());
       }

    }

    static class Worker implements Runnable {
        private String threadName;

        public Worker(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public void run() {
            while (true) {
                LOCK.lock();
                try {
                    String str = new String(Files.readAllBytes(Paths.get(PATH_TO_OUTPUT + OUTPUT_FILE_NAME)));
                    int valueFromFile = Integer.parseInt(str);
                    if (valueFromFile >= App.inputInt) {
                        break;
                    }
                    valueFromFile++;
                    System.out.println(str + "->" + valueFromFile + ", " + threadName);
                    Files.write(Paths.get(PATH_TO_OUTPUT + OUTPUT_FILE_NAME), String.valueOf(valueFromFile).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    LOCK.unlock();
                }
            }
        }
    }

    public static void writeToFile(Integer intValue){
        try( FileWriter fileWriter = new FileWriter(PATH_TO_OUTPUT + OUTPUT_FILE_NAME,false)){
            fileWriter.write(intValue.toString());
        }catch (IOException e){
            System.out.println("Ошибка записи в файл");
        }
    }

    public static String readFromFile(){
        try(FileReader fileReader = new FileReader(PATH_TO_OUTPUT + OUTPUT_FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader)){
            return bufferedReader.readLine();
        }catch (IOException e){
            return null;
        }
    }

    public static boolean checkedCreateFolderOrFile(){
        if (!Files.exists(Path.of(PATH_TO_OUTPUT))) {
            try {
                Files.createDirectory(Path.of(PATH_TO_OUTPUT));
            } catch (IOException e) {
                System.out.println("Ошибка создания каталога.");
                return false;
            }
        }
        try {
            Files.write(Path.of(PATH_TO_OUTPUT + OUTPUT_FILE_NAME),"0".getBytes());
        } catch (IOException e) {
            System.out.println("Ошибка создания файла");
            return false;
        }
        return true;
    }
}


