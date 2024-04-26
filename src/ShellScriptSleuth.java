import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ShellScriptSleuth {

    /**
     * The main method that starts the program loop.
     */
    public static void main(String[] args) {
        runProgramLoop();
    }

    /**
     * This method runs the main loop of the program, asking the user if they want to parse another file after each iteration.
     */
    private static void runProgramLoop() {
        Scanner sc = new Scanner(System.in);
        boolean programState = true;

        while(programState) {
            runShellScriptSleuth();
            System.out.print("\n" + "WOULD YOU LIKE TO PARSE ANOTHER FILE? (y/n) : ");
            String userResponse = sc.nextLine();

            if(userResponse.equalsIgnoreCase("y")) {
                System.out.println("**RESTARTING SHELL-SCRIPT-SLEUTH**" + "\n");
            }
            else {
                System.out.println("\n" + "**SHELL-SCRIPT-SLEUTH TERMINATED**");
                programState = false;
            }
        }
    }

    /**
     * This method runs the main functionality of the ShellScriptSleuth, asking for a file to parse and an optional file to write to.
     */
    private static void runShellScriptSleuth() {
        Scanner sc = new Scanner(System.in);
        System.out.print("""


                PLEASE ENTER PATH TO YOUR UNIX FILE:\s""");
        String path = sc.nextLine();
        displayCommands(parseFileCommands(path), path);

        System.out.print("\n" + "WOULD YOU LIKE TO WRITE THIS TO A TEXT FILE? (y/n) : ");
        String userResponse = sc.nextLine();
        if(userResponse.equalsIgnoreCase("y")) {
            System.out.print("ENTER THE PATH OF THE DIRECTORY AND NAME OF FILE YOU WANT TO WRITE TO: ");
            String usersFilePath = sc.nextLine();
            writeCommandsToFile(parseFileCommands(path), usersFilePath);
            System.out.println
                    ("""
                    .
                    .
                    .""");
            System.out.println("** CONTENTS TO FILE HAVE BEEN WRITTEN SUCCESSFULLY :) **");
        }
    }

    /**
     * This method parses the file name from a given path.
     * @param userPath The path to parse the file name from.
     * @return The file name parsed from the path.
     */
    private static String parseFileName(String userPath) {
        String[] fileName = userPath.split("/");
        return fileName[fileName.length - 1];
    }

    /**
     * This method parses the commands from a given Unix shell script file.
     * @param fileName The name of the file to parse commands from.
     * @return A map of command numbers to commands.
     */
    private static Map<Integer, String> parseFileCommands(String fileName) {
        BufferedReader reader = null;
        Map<Integer, String> outputs = new HashMap<>();
        int commandCounter = 1;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split("\\$\\s+");
                if (parts.length > 1) {
                    outputs.put(commandCounter++, parts[parts.length - 1]);
                }
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outputs;
    }

    /**
     * This method writes the parsed commands to a specified file.
     * @param outputs The parsed commands to write.
     * @param desiredDirectoryPath The path of the file to write to.
     */
    private static void writeCommandsToFile(Map<Integer, String> outputs, String desiredDirectoryPath) {
        BufferedWriter writer = null;

        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(desiredDirectoryPath), StandardCharsets.UTF_8));

            for(Map.Entry<Integer, String> entry : outputs.entrySet()) {
                String currentCommand = entry.getValue();
                currentCommand = currentCommand.replaceAll("\\e\\[[\\d;]*[^\\d;]", "");
                currentCommand = currentCommand.replaceAll("[^\\x20-\\x7E]", "");

                writer.write(entry.getKey() + ":  $ " + currentCommand + "\n");
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method displays the parsed commands to the console.
     * @param inputs The parsed commands to display.
     * @param path Given path of file utilized to parse the name of the file
     */
    private static void displayCommands(Map<Integer, String> inputs, String path) {
        System.out.println("PARSED UNIX COMMANDS FOR FILE: " + parseFileName(path));
        for(Map.Entry<Integer, String> entry : inputs.entrySet()) {
            System.out.println(entry.getKey() + ":  $ " + entry.getValue());
        }
    }

}
