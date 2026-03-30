import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChatHistoryManager {
    private static final String FILE_NAME = "chat_history.txt";
    // #3: Formatter for timestamps shown in chat
    public static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("HH:mm");

    // Returns current time as formatted string e.g. "14:32"
    public static String now() {
        return LocalDateTime.now().format(TIMESTAMP_FMT);
    }

    // Append a single message line to the history file
    public static synchronized void saveMessage(String message) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(message);
        } catch (IOException e) {
            System.out.println("Loi ghi lich su: " + e.getMessage());
        }
    }

    // Read all history lines from file
    public static List<String> loadHistory() {
        List<String> history = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return history;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            System.out.println("Loi doc lich su: " + e.getMessage());
        }
        return history;
    }
}
