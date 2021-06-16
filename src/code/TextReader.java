package code;

import java.io.*;
import java.util.ArrayList;

public class TextReader {
    private String fileName;

    public TextReader(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<String[]> readTextFile() {
        ArrayList<String[]> ticketInfo = new ArrayList<>();

        try {
//            System.out.println(fileName);
//            System.out.println(this.getClass().getResourceAsStream("/testCases/"+fileName));
            BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/testCases/"+fileName)));

            String curLine;

            while ((curLine = br.readLine()) != null) {
                String[] parts = curLine.split("[:,]");
                for (int i = 0; i < parts.length; i++) {
//                    System.out.print(parts[i] + " ");
                }
//                System.out.println();
                ticketInfo.add(parts);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ticketInfo;
    }
}
