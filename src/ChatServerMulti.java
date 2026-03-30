import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServerMulti {

    // #1: Map username -> output stream for targeted PM delivery
    private static final Map<String, DataOutputStream> clientMap = new LinkedHashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("Server Multi-Chat dang mo cua o cong 9999...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Co mot khach vua ket noi! IP: " + clientSocket.getInetAddress());

                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

                Thread clientThread = new Thread(new ClientHandler(dis, dos, clientSocket));
                clientThread.setDaemon(true);
                clientThread.start();
            }
        } catch (Exception e) {
            System.out.println("Loi Server: " + e.getMessage());
        }
    }

    // Broadcast to every connected client
    public static void broadcastMessage(String message) {
        ChatHistoryManager.saveMessage(message);
        synchronized (clientMap) {
            for (DataOutputStream writer : clientMap.values()) {
                sendTo(writer, message);
            }
        }
    }

    // #1: Send a private message only to sender + target
    private static void sendPrivateMessage(String fromName, DataOutputStream senderDos, String targetName, String content) {
        String ts = ChatHistoryManager.now();
        // #1 PM prefix — client side detects "PM|" prefix for special rendering
        String pmPayload = "PM|" + fromName + "|" + targetName + "|" + ts + "|" + content;

        synchronized (clientMap) {
            DataOutputStream targetDos = clientMap.get(targetName);
            if (targetDos == null) {
                sendTo(senderDos, "SYS|Khong tim thay nguoi dung: " + targetName);
                return;
            }
            sendTo(targetDos, pmPayload);
            if (!targetName.equals(fromName)) {
                sendTo(senderDos, pmPayload); // echo back to sender
            }
        }
    }

    private static void sendTo(DataOutputStream dos, String msg) {
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (Exception ignored) {}
    }

    // Send current online user list to all clients
    private static void broadcastUserList() {
        synchronized (clientMap) {
            String userList = "USERS|" + String.join(",", clientMap.keySet());
            for (DataOutputStream writer : clientMap.values()) {
                sendTo(writer, userList);
            }
        }
    }

    // ---------------------------------------------------------------
    private static class ClientHandler implements Runnable {
        private final DataInputStream dis;
        private final DataOutputStream dos;
        private final Socket socket;
        private String username = null;

        public ClientHandler(DataInputStream dis, DataOutputStream dos, Socket socket) {
            this.dis = dis;
            this.dos = dos;
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // First message from client is always the JOIN handshake: "JOIN|username"
                String handshake = dis.readUTF();
                if (!handshake.startsWith("JOIN|")) {
                    sendTo(dos, "SYS|Protocol error.");
                    socket.close();
                    return;
                }
                String requestedName = handshake.substring(5).trim();

                // #4 (bonus): deduplicate usernames
                synchronized (clientMap) {
                    if (clientMap.containsKey(requestedName)) {
                        sendTo(dos, "SYS|Ten '" + requestedName + "' da duoc dung. Hay chon ten khac.");
                        socket.close();
                        return;
                    }
                    username = requestedName;
                    clientMap.put(username, dos);
                }

                // Send chat history to the newcomer
                List<String> history = ChatHistoryManager.loadHistory();
                if (!history.isEmpty()) {
                    sendTo(dos, "SYS|--- LICH SU TRO CHUYEN ---");
                    for (String msg : history) {
                        sendTo(dos, msg);
                    }
                    sendTo(dos, "SYS|--------------------------");
                }

                // #2: Announce join to everyone; #3: with timestamp
                String joinMsg = "JOIN_MSG|" + username + "|" + ChatHistoryManager.now();
                broadcastMessage(joinMsg);
                broadcastUserList();

                // Main receive loop
                while (true) {
                    String raw = dis.readUTF();

                    // #1: /pm command: "/pm targetName message body here"
                    if (raw.startsWith("/pm ")) {
                        String[] parts = raw.substring(4).split(" ", 2);
                        if (parts.length == 2) {
                            sendPrivateMessage(username, dos, parts[0].trim(), parts[1].trim());
                        } else {
                            sendTo(dos, "SYS|Cu phap: /pm <ten_nguoi> <noi_dung>");
                        }
                        continue;
                    }

                    // /quit command
                    if (raw.equals("/quit")) break;

                    // Typing signals: relay to others only, not saved as messages
                    if (raw.startsWith("TYPING|") || raw.startsWith("STOP_TYPING|")) {
                        synchronized (clientMap) {
                            for (Map.Entry<String, DataOutputStream> entry : clientMap.entrySet()) {
                                if (!entry.getKey().equals(username)) {
                                    sendTo(entry.getValue(), raw);
                                }
                            }
                        }
                        continue;
                    }

                    // Normal chat message: wrap with sender + timestamp
                    // #3: Format: "MSG|sender|HH:mm|content"
                    String ts = ChatHistoryManager.now();
                    String payload = "MSG|" + username + "|" + ts + "|" + raw;
                    System.out.println("[" + ts + "] " + username + ": " + raw);
                    broadcastMessage(payload);
                }

            } catch (Exception e) {
                // Client disconnected abruptly
            } finally {
                disconnect();
            }
        }

        private void disconnect() {
            if (username != null) {
                synchronized (clientMap) {
                    clientMap.remove(username);
                }
                // #2: Broadcast leave announcement
                String leaveMsg = "LEAVE_MSG|" + username + "|" + ChatHistoryManager.now();
                broadcastMessage(leaveMsg);
                broadcastUserList();
                System.out.println(username + " da roi phong.");
            }
            try { socket.close(); } catch (Exception ignored) {}
        }
    }
}
