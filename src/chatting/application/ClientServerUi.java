package chatting.application;

import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientServerUi {

    private JFrame frame;
    private JTextField enter;
    private JPanel chatArea;
    private Box vertical = Box.createVerticalBox();
    private Socket socket;
    private DataOutputStream dout;
    private DataInputStream din;
    private boolean reconnecting = false; // Flag to prevent multiple reconnection attempts

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ClientServerUi window = new ClientServerUi();
                window.frame.setVisible(true);
                window.initializeConnection();
                window.startListening();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initializeConnection() throws IOException {
        socket = new Socket("127.0.0.1", 6001);
        dout = new DataOutputStream(socket.getOutputStream());
        din = new DataInputStream(socket.getInputStream());
    }

    private void startListening() {
        new Thread(() -> {
            try {
                while (true) {
                    String msg = din.readUTF(); // Using readUTF for UTF encoded strings
                    if (msg != null) {
                        Message message = new Message("Other", msg, LocalDateTime.now());
                        SwingUtilities.invokeLater(() -> displayMessage(message));
                    } else {
                        handleDisconnection();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                handleDisconnection();
            }
        }).start();
    }

    private void handleDisconnection() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(frame, "Connection to the server lost. Attempting to reconnect...", "Connection Error", JOptionPane.ERROR_MESSAGE);
            if (!reconnecting) {
                reconnecting = true;
                reconnect();
            }
        });
    }

    private void reconnect() {
        new Thread(() -> {
            while (true) {
                try {
                    socket = new Socket("127.0.0.1", 6001);
                    dout = new DataOutputStream(socket.getOutputStream());
                    din = new DataInputStream(socket.getInputStream());
                    startListening(); // Restart listening for messages
                    reconnecting = false; // Reset flag when successful
                    break; // Exit loop when successful
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(5000); // Wait before trying to reconnect
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }

    public ClientServerUi() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBackground(new Color(176, 196, 222));
        frame.setBounds(100, 100, 752, 660);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(192, 192, 192));
        frame.getContentPane().setLayout(null);
        frame.setUndecorated(true);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(64, 64, 64));
        headerPanel.setBounds(0, 0, 752, 53);
        headerPanel.setLayout(null);
        frame.getContentPane().add(headerPanel);

        JLabel backArrow = createLabel("back", "C:\\\\Users\\\\neesa\\\\OneDrive\\\\Pictures\\\\Screenshots\\\\WhatsApp Image 2024-07-26 at 15.00.03_aaf9ccdb.jpg", 10, 0, 71, 53);
        headerPanel.add(backArrow);
        backArrow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        JLabel profile = createLabel("profile", "C:\\Users\\neesa\\OneDrive\\Pictures\\Screenshots\\WhatsApp Image 2024-07-27 at 13.42.47_043ed2cb.jpg", 80, 0, 126, 53);
        headerPanel.add(profile);

        JLabel miaLabel = createLabel("NIKKI", null, 280, 0, 126, 20);
        miaLabel.setForeground(Color.WHITE);
        miaLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        miaLabel.setHorizontalAlignment(JLabel.CENTER);
        headerPanel.add(miaLabel);

        JLabel active = createLabel("ACTIVE", null, 280, 0, 126, 53);
        active.setForeground(Color.WHITE);
        active.setFont(new Font("Tahoma", Font.BOLD, 18));
        headerPanel.add(active);

        JLabel videocall = createLabel("videocall", "C:\\\\Users\\\\neesa\\\\OneDrive\\\\Pictures\\\\Screenshots\\\\WhatsApp Image 2024-07-26 at 15.13.28_555772e4.jpg", 581, 0, 63, 53);
        headerPanel.add(videocall);

        JLabel call = createLabel("call", "C:\\\\Users\\\\neesa\\\\OneDrive\\\\Pictures\\\\Screenshots\\\\WhatsApp Image 2024-07-26 at 15.14.13_fa3613fc.jpg", 649, 0, 63, 53);
        headerPanel.add(call);

        JLabel settings = createLabel("settings", "C:\\\\Users\\\\neesa\\\\OneDrive\\\\Pictures\\\\Screenshots\\\\WhatsApp Image 2024-07-26 at 15.14.31_05b9908f.jpg", 716, 0, 36, 53);
        headerPanel.add(settings);

        chatArea = new JPanel();
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBounds(0, 53, 752, 540);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.getContentPane().add(scrollPane);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(192, 192, 192));
        inputPanel.setBounds(0, 593, 752, 67);
        inputPanel.setLayout(null);
        frame.getContentPane().add(inputPanel);

        enter = new JTextField();
        enter.setFont(new Font("Segoe Print", Font.PLAIN, 15));
        enter.setBounds(10, 14, 631, 40);
        inputPanel.add(enter);
        enter.setColumns(10);

        JButton send = new JButton("SEND");
        send.setIcon(new ImageIcon("C:\\\\Users\\\\neesa\\\\OneDrive\\\\Pictures\\\\Screenshots\\\\WhatsApp Image 2024-07-26 at 22.10.47_49b19024.jpg"));
        send.setForeground(Color.DARK_GRAY);
        send.setBackground(Color.DARK_GRAY);
        send.setFont(new Font("Tahoma", Font.PLAIN, 22));
        send.setBounds(651, 14, 91, 40);
        inputPanel.add(send);

        send.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sendMessage();
            }
        });

        enter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
    }

    private void sendMessage() {
        String messageText = enter.getText().trim();
        if (!messageText.isEmpty()) {
            Message message = new Message("Me", messageText, LocalDateTime.now());
            try {
                dout.writeUTF(messageText); // Use writeUTF for UTF encoded strings
                dout.flush();
                displayMessage(message);
                enter.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayMessage(Message message) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(new Color(245, 245, 245));
        messagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel messageLabel = new JLabel("<html><p style='width: 200px;'>" + message.getText() + "</p></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setOpaque(true);

        // Set background color based on message sender
        if (message.getSender().equals("Me")) {
            messageLabel.setBackground(Color.BLACK); // Color for messages sent by the user
            messageLabel.setForeground(Color.WHITE);
        } else if (message.getSender().equals("Other")) {
            messageLabel.setBackground(new Color(0, 132, 255)); // Color for messages received from others
            messageLabel.setForeground(Color.WHITE);
        }

        messagePanel.add(messageLabel);

        JLabel timestampLabel = new JLabel(message.getFormattedTimestamp());
        timestampLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        timestampLabel.setForeground(Color.GRAY);
        messagePanel.add(timestampLabel);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(messagePanel, BorderLayout.LINE_END);
        vertical.add(rightPanel);
        vertical.add(Box.createVerticalStrut(5));
        chatArea.add(vertical);

        frame.revalidate();
    }

    private JLabel createLabel(String text, String iconPath, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        if (iconPath != null) {
            label.setIcon(new ImageIcon(iconPath));
        }
        label.setBounds(x, y, width, height);
        return label;
    }

    // Message class definition
    public class Message {
        private String sender;
        private String text;
        private LocalDateTime timestamp;

        public Message(String sender, String text, LocalDateTime timestamp) {
            this.sender = sender;
            this.text = text;
            this.timestamp = timestamp;
        }

        public String getSender() {
            return sender;
        }

        public String getText() {
            return text;
        }

        public String getFormattedTimestamp() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            return timestamp.format(formatter);
        }
    }
}
