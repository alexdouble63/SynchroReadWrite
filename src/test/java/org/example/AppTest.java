package org.example;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest
{
    @Mock
    private FileWriter fileWriter;

    @InjectMocks
    private App app;

    private static final String PATH_TO_OUTPUT = "c:/test/";
    private static final String OUTPUT_FILE_NAME = "out.txt";

    @Test
    public void testWriteToFile() throws IOException {
        Integer intValue = 123;
        String expectedContent = "123";

        app.writeToFile(intValue);

        File outputFile = new File(PATH_TO_OUTPUT + OUTPUT_FILE_NAME);
        assertTrue(outputFile.exists());

        String actualContent = Files.readString(outputFile.toPath());
        assertTrue(actualContent.equals(expectedContent));
    }

    @Test
    public void testReadFromFile(){
        Integer intValue = 123;
        String expectedContent = "123";

        app.writeToFile(intValue);

        String actualContent = app.readFromFile();
        assertTrue(actualContent.equals(expectedContent));
    }

    @Test
    public void testWorkerRun() throws IOException {
        int inputInt = 11;
        App.Worker worker = new App.Worker("TestThread");
        App.inputInt = inputInt;
        Files.write(Paths.get(PATH_TO_OUTPUT + OUTPUT_FILE_NAME), "0".getBytes());

        worker.run();

        String content = new String(Files.readAllBytes(Paths.get(PATH_TO_OUTPUT + OUTPUT_FILE_NAME)));
        int valueFromFile = Integer.parseInt(content);
        assertEquals(inputInt, valueFromFile);
    }
}
