import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

public class TextReader {
    private String fileName;

    public TextReader(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<String[]> readTextFile() {
        ArrayList<String[]> ticketInfo = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
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

//    public static void main(String[] args) {
//        TextReader tr = new TextReader("NormalScenario.txt");
//        tr.readTextFile();
//    }
}
