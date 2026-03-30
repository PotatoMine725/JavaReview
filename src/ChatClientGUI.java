import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatClientGUI {

    // ── Palette ──────────────────────────────────────────────────────────────
    private static final Color BG_DEEP     = new Color(24,  26,  30);
    private static final Color BG_PANEL    = new Color(32,  34,  40);
    private static final Color BG_INPUT    = new Color(42,  44,  52);
    private static final Color BORDER_CLR  = new Color(55,  58,  68);
    private static final Color MY_BUBBLE   = new Color(88, 101, 242);   // indigo — "my" messages
    private static final Color MY_TEXT     = Color.WHITE;
    private static final Color THEIR_TEXT  = new Color(220, 221, 222);
    private static final Color SYS_TEXT    = new Color(114, 137, 218);
    private static final Color INPUT_FG    = new Color(220, 221, 222);
    private static final Color HINT_CLR    = new Color(90,  95, 110);
    private static final Color PM_BADGE    = new Color(235, 69, 158);   // pink badge for PMs

    // Palette of bubble colours for other users (assigned by username hash)
    private static final Color[] USER_PALETTE = {
        new Color(67, 181, 129),   // green
        new Color(250, 166, 26),   // amber
        new Color(240, 71, 71),    // red
        new Color(26, 188, 156),   // teal
        new Color(155, 89, 182),   // purple
        new Color(52, 152, 219),   // sky
        new Color(230, 126, 34),   // orange
        new Color(231, 76, 60),    // crimson
    };

    // ── State ────────────────────────────────────────────────────────────────
    private final String clientName;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    // Cache username -> assigned colour
    private final Map<String, Color> userColors = new HashMap<>();

    // ── Swing components ─────────────────────────────────────────────────────
    private JFrame frame;
    private JPanel messagesPanel;   // holds MessageBubble rows
    private JScrollPane scrollPane;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel typingLabel;     // #9 typing indicator
    private JPanel userListPanel;   // side panel showing online users

    // #9 Typing: debounce timer
    private javax.swing.Timer typingStopTimer;
    private boolean isTyping = false;

    // ─────────────────────────────────────────────────────────────────────────

    public ChatClientGUI(String name) {
        this.clientName = name;
        // No forced self-color — colorForUser() assigns consistent palette colors
        // for everyone including yourself, so colors match across all windows.
        buildUI();
        connectToServer();
    }

    // ── UI Construction ──────────────────────────────────────────────────────

    private void buildUI() {
        applyNimbus();

        frame = new JFrame("ChatApp  ·  " + clientName);
        frame.setSize(860, 680);
        frame.setMinimumSize(new Dimension(620, 480));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(BG_DEEP);
        frame.setLayout(new BorderLayout());

        // ── Header bar ───────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PANEL);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR),
                new EmptyBorder(12, 20, 12, 20)));

        JLabel title = new JLabel("# phong-chung");
        title.setFont(loadFont(16f, Font.BOLD));
        title.setForeground(new Color(255, 255, 255));
        header.add(title, BorderLayout.WEST);

        JLabel hint = new JLabel("  /pm <ten> <noi dung>  để nhắn riêng");
        hint.setFont(loadFont(12f, Font.PLAIN));
        hint.setForeground(HINT_CLR);
        header.add(hint, BorderLayout.EAST);

        frame.add(header, BorderLayout.NORTH);

        // ── Center: messages + side user list ───────────────────────────────
        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        centerSplit.setDividerSize(1);
        centerSplit.setDividerLocation(620);
        centerSplit.setBackground(BORDER_CLR);
        centerSplit.setBorder(null);

        // Messages area
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(BG_DEEP);
        messagesPanel.setBorder(new EmptyBorder(16, 16, 8, 16));

        scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_DEEP);
        scrollPane.getVerticalScrollBar().setBackground(BG_DEEP);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        centerSplit.setLeftComponent(scrollPane);

        // Online users side panel
        userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
        userListPanel.setBackground(BG_PANEL);
        userListPanel.setBorder(new EmptyBorder(16, 12, 16, 12));

        JLabel onlineTitle = new JLabel("ONLINE");
        onlineTitle.setFont(loadFont(11f, Font.BOLD));
        onlineTitle.setForeground(HINT_CLR);
        onlineTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        onlineTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        userListPanel.add(onlineTitle);

        JScrollPane userScroll = new JScrollPane(userListPanel);
        userScroll.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER_CLR));
        userScroll.getViewport().setBackground(BG_PANEL);
        centerSplit.setRightComponent(userScroll);

        frame.add(centerSplit, BorderLayout.CENTER);

        // ── Bottom: typing indicator + input bar ─────────────────────────────
        JPanel southWrapper = new JPanel(new BorderLayout());
        southWrapper.setBackground(BG_DEEP);

        // #9 Typing label (sits above the input bar)
        typingLabel = new JLabel(" ");
        typingLabel.setFont(loadFont(12f, Font.ITALIC));
        typingLabel.setForeground(new Color(150, 155, 170));
        typingLabel.setBorder(new EmptyBorder(4, 22, 0, 0));
        southWrapper.add(typingLabel, BorderLayout.NORTH);

        // Input row
        JPanel inputRow = new JPanel(new BorderLayout(10, 0));
        inputRow.setBackground(BG_DEEP);
        inputRow.setBorder(new EmptyBorder(8, 16, 16, 16));

        messageField = new JTextField();
        messageField.setFont(loadFont(14f, Font.PLAIN));
        messageField.setBackground(BG_INPUT);
        messageField.setForeground(INPUT_FG);
        messageField.setCaretColor(INPUT_FG);
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(10, 14, 10, 14)));

        sendButton = new JButton("Gửi  ▶");
        sendButton.setFont(loadFont(13f, Font.BOLD));
        sendButton.setBackground(MY_BUBBLE);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendButton.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Hover effect on send button
        sendButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { sendButton.setBackground(MY_BUBBLE.brighter()); }
            public void mouseExited(MouseEvent e)  { sendButton.setBackground(MY_BUBBLE); }
        });

        inputRow.add(messageField, BorderLayout.CENTER);
        inputRow.add(sendButton,   BorderLayout.EAST);

        southWrapper.add(inputRow, BorderLayout.CENTER);
        frame.add(southWrapper, BorderLayout.SOUTH);

        // ── Wire actions ─────────────────────────────────────────────────────
        ActionListener sendAction = e -> sendMessage();
        sendButton.addActionListener(sendAction);
        messageField.addActionListener(sendAction);

        // #9: Typing indicator — fire when user types
        messageField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { onTyping(); }
            public void removeUpdate(DocumentEvent e)  { onTyping(); }
            public void changedUpdate(DocumentEvent e) {}
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ── Typing indicator (feature #9) ────────────────────────────────────────

    private void onTyping() {
        if (!isTyping) {
            isTyping = true;
            safeSend("TYPING|" + clientName);
        }
        // Reset debounce timer — if user stops typing for 2s, send STOP_TYPING
        if (typingStopTimer != null) typingStopTimer.stop();
        typingStopTimer = new javax.swing.Timer(2000, e -> {
            isTyping = false;
            safeSend("STOP_TYPING|" + clientName);
        });
        typingStopTimer.setRepeats(false);
        typingStopTimer.start();
    }

    // ── Server connection ────────────────────────────────────────────────────

    private void connectToServer() {
        try {
            // THAY ĐỔI: Cho phép nhập địa chỉ IP server
            String serverIP = JOptionPane.showInputDialog(null,
                    "Nhập địa chỉ IP của Server:\n(Để trống = localhost)",
                    "Kết nối Server",
                    JOptionPane.QUESTION_MESSAGE);
            
            // Nếu không nhập gì hoặc nhấn Cancel, dùng localhost
            if (serverIP == null || serverIP.trim().isEmpty()) {
                serverIP = "localhost";
            } else {
                serverIP = serverIP.trim();
            }
            
            // Kết nối đến server với IP đã nhập
            socket = new Socket(serverIP, 9999);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            // Handshake: tell server our name
            dos.writeUTF("JOIN|" + clientName);
            dos.flush();

            // Receive loop on background thread
            Thread t = new Thread(() -> {
                try {
                    while (true) {
                        String raw = dis.readUTF();
                        handleIncoming(raw);
                    }
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() ->
                        appendSystemMessage("Mất kết nối với server."));
                }
            });
            t.setDaemon(true);
            t.start();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame,
                "Không thể kết nối tới Server!\nHãy chắc chắn server đang chạy.",
                "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Incoming message router ───────────────────────────────────────────────

    private void handleIncoming(String raw) {
        SwingUtilities.invokeLater(() -> {
            // MSG|sender|HH:mm|content  — normal broadcast
            if (raw.startsWith("MSG|")) {
                String[] p = raw.split("\\|", 4);
                if (p.length == 4) appendChatBubble(p[1], p[2], p[3], false, false);

            // PM|from|to|HH:mm|content  — private message (#1)
            } else if (raw.startsWith("PM|")) {
                String[] p = raw.split("\\|", 5);
                if (p.length == 5) appendChatBubble(p[1], p[3], p[4], false, true);

            // JOIN_MSG|username|HH:mm  — announce join (#2)
            } else if (raw.startsWith("JOIN_MSG|")) {
                String[] p = raw.split("\\|");
                if (p.length >= 3)
                    appendSystemMessage("⟶ " + p[1] + " đã vào phòng  [" + p[2] + "]");

            // LEAVE_MSG|username|HH:mm  — announce leave (#2)
            } else if (raw.startsWith("LEAVE_MSG|")) {
                String[] p = raw.split("\\|");
                if (p.length >= 3)
                    appendSystemMessage("⟵ " + p[1] + " đã rời phòng  [" + p[2] + "]");

            // USERS|name1,name2,...  — online list update
            } else if (raw.startsWith("USERS|")) {
                String[] names = raw.substring(6).split(",");
                updateUserList(names);

            // SYS|...  — server system messages
            } else if (raw.startsWith("SYS|")) {
                appendSystemMessage(raw.substring(4));

            // TYPING|username  — show typing indicator (#9)
            } else if (raw.startsWith("TYPING|")) {
                String who = raw.substring(7);
                if (!who.equals(clientName))
                    typingLabel.setText(who + " đang gõ...");

            // STOP_TYPING|username  — hide typing indicator (#9)
            } else if (raw.startsWith("STOP_TYPING|")) {
                typingLabel.setText(" ");

            // Fallback: history lines that might be raw MSG| lines
            } else {
                appendSystemMessage(raw);
            }
        });
    }

    // ── Send ─────────────────────────────────────────────────────────────────

    private void sendMessage() {
        String text = messageField.getText().trim();
        if (text.isEmpty()) return;

        // Stop typing indicator immediately (local + notify others)
        if (typingStopTimer != null) typingStopTimer.stop();
        isTyping = false;
        typingLabel.setText(" ");  // clear instantly, no server round-trip needed
        safeSend("STOP_TYPING|" + clientName);

        // /pm shortcut: render own PM bubble locally + send to server
        if (text.startsWith("/pm ")) {
            String[] parts = text.substring(4).split(" ", 2);
            if (parts.length == 2) {
                // own bubble shown locally when server echoes it back — nothing extra needed
            }
        } else {
            // Optimistically render our own message immediately (feel snappy)
            // Server will also echo it back; we de-duplicate by sender == clientName
        }

        safeSend(text);
        messageField.setText("");
    }

    private void safeSend(String msg) {
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch (Exception ex) {
            appendSystemMessage("[Lỗi gửi tin]");
        }
    }

    // ── UI helpers ────────────────────────────────────────────────────────────

    /**
     * Append a chat bubble row to the messages panel.
     * #7: colour-coded by sender; #8: bubble shape; right-aligned for self.
     */
    private void appendChatBubble(String sender, String time, String content,
                                   boolean isHistory, boolean isPrivate) {
        boolean isMine = sender.equals(clientName);
        // Use the same palette-derived color for everyone so colors are
        // consistent across all chat windows — alignment distinguishes self.
        Color bubbleColor = colorForUser(sender);
        Color textColor   = MY_TEXT; // white text readable on all palette colors

        // Outer row: aligns bubble left or right
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG_DEEP);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        row.setBorder(new EmptyBorder(3, 0, 3, 0));

        // Bubble component (#8 rounded bubble)
        BubblePanel bubble = new BubblePanel(bubbleColor, isMine);
        bubble.setLayout(new BorderLayout(0, 4));
        bubble.setBorder(new EmptyBorder(8, 14, 8, 14));

        // Header: sender name + timestamp + optional PM badge
        JPanel bubbleHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        bubbleHeader.setOpaque(false);

        if (!isMine) {
            JLabel nameLabel = new JLabel(sender);
            nameLabel.setFont(loadFont(12f, Font.BOLD));
            nameLabel.setForeground(bubbleColor.brighter());
            bubbleHeader.add(nameLabel);
        }

        if (isPrivate) {
            JLabel pmTag = new JLabel(" PM ");
            pmTag.setFont(loadFont(10f, Font.BOLD));
            pmTag.setForeground(Color.WHITE);
            pmTag.setBackground(PM_BADGE);
            pmTag.setOpaque(true);
            pmTag.setBorder(new EmptyBorder(1, 4, 1, 4));
            bubbleHeader.add(pmTag);
        }

        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(loadFont(11f, Font.PLAIN));
        timeLabel.setForeground(new Color(180, 180, 200, 160));
        bubbleHeader.add(timeLabel);

        // Content label (word-wrapped via HTML)
        String htmlContent = "<html><body style='width:280px;'>" + escapeHtml(content) + "</body></html>";
        JLabel contentLabel = new JLabel(htmlContent);
        contentLabel.setFont(loadFont(14f, Font.PLAIN));
        contentLabel.setForeground(textColor);

        bubble.add(bubbleHeader,  BorderLayout.NORTH);
        bubble.add(contentLabel,  BorderLayout.CENTER);

        // Spacer so bubble doesn't stretch full width
        JPanel spacer = new JPanel();
        spacer.setBackground(BG_DEEP);
        spacer.setPreferredSize(new Dimension(120, 10));

        if (isMine) {
            row.add(spacer, BorderLayout.WEST);
            row.add(bubble, BorderLayout.CENTER);
        } else {
            row.add(bubble,  BorderLayout.CENTER);
            row.add(spacer,  BorderLayout.EAST);
        }

        messagesPanel.add(row);
        messagesPanel.add(Box.createVerticalStrut(4));
        messagesPanel.revalidate();
        scrollToBottom();
    }

    private void appendSystemMessage(String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));
        row.setBackground(BG_DEEP);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel label = new JLabel(text);
        label.setFont(loadFont(12f, Font.ITALIC));
        label.setForeground(SYS_TEXT);
        row.add(label);

        messagesPanel.add(row);
        messagesPanel.add(Box.createVerticalStrut(2));
        messagesPanel.revalidate();
        scrollToBottom();
    }

    private void updateUserList(String[] names) {
        // Remove all except the first "ONLINE" title label
        Component titleLabel = userListPanel.getComponent(0);
        userListPanel.removeAll();
        userListPanel.add(titleLabel);
        userListPanel.add(Box.createVerticalStrut(6));

        for (String name : names) {
            if (name.isBlank()) continue;
            Color dot = colorForUser(name);
            JPanel entry = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
            entry.setBackground(BG_PANEL);
            entry.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            entry.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Coloured dot
            JLabel dotLabel = new JLabel("●");
            dotLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
            dotLabel.setForeground(dot);
            entry.add(dotLabel);

            JLabel nameLabel = new JLabel(name);
            nameLabel.setFont(loadFont(13f, Font.PLAIN));
            nameLabel.setForeground(name.equals(clientName)
                ? new Color(255, 255, 255) : THEIR_TEXT);
            entry.add(nameLabel);

            userListPanel.add(entry);
        }
        userListPanel.revalidate();
        userListPanel.repaint();
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    // ── Colour assignment (#7) ───────────────────────────────────────────────

    private Color colorForUser(String name) {
        return userColors.computeIfAbsent(name, n -> {
            int idx = Math.abs(n.hashCode()) % USER_PALETTE.length;
            return USER_PALETTE[idx];
        });
    }

    // ── Custom rounded bubble panel (#8) ─────────────────────────────────────

    static class BubblePanel extends JPanel {
        private final Color color;
        private final boolean isRight;

        BubblePanel(Color color, boolean isRight) {
            this.color   = color;
            this.isRight = isRight;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int arc = 18;
            g2.setColor(color);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Utilities ─────────────────────────────────────────────────────────────

    private static Font loadFont(float size, int style) {
        // Use a clean, slightly modern monospace-adjacent font
        return new Font("Segoe UI", style, (int) size);
    }

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static void applyNimbus() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    // Override Nimbus dark defaults
                    UIManager.put("control",       BG_PANEL);
                    UIManager.put("text",          new Color(220, 221, 222));
                    UIManager.put("nimbusBase",    BG_DEEP);
                    UIManager.put("nimbusLightBackground", BG_INPUT);
                    break;
                }
            }
        } catch (Exception ignored) {}
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JTextField nameField = new JTextField(20);
            nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            int result = JOptionPane.showConfirmDialog(null, nameField,
                    "Nhập tên của bạn:", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) return;

            String name = nameField.getText().trim();
            if (name.isEmpty()) name = "Khách_" + (int)(Math.random() * 100);

            final String finalName = name;
            new ChatClientGUI(finalName);
        });
    }
}
