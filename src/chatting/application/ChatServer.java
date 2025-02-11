package chatting.application;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ChatServer {

    private JFrame frame;
    private JTextField enter;
    private JPanel chatArea;
    private Box vertical = Box.createVerticalBox();
    private DataOutputStream dout;
    private DataInputStream din;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private Socket otherServerSocket; // Socket to connect to the other server

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ChatServer window = new ChatServer();
                window.frame.setVisible(true);
                window.startServer();
                window.connectToOtherServer("localhost", 6002); // Change port as needed
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ChatServer() {
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
        backArrow.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.exit(0);
            }
        });

        JLabel profile = createLabel("profile", "C:\\Users\\neesa\\OneDrive\\Pictures\\Screenshots\\WhatsApp Image 2024-07-26 at 21.50.25_adab37ab.jpg", 80, 0, 126, 53);
        headerPanel.add(profile);

        JLabel miaLabel = createLabel("MIA", null, 280, 0, 126, 20);
        miaLabel.setForeground(Color.WHITE);
        miaLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
        miaLabel.setHorizontalAlignment(JLabel.CENTER);
        headerPanel.add(miaLabel);

        JLabel active = createLabel("ACTIVE", null, 280, 20, 126, 33);
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

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

    private void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(6001);
                System.out.println("Server started on port 6001");
                while (true) {
                    clientSocket = serverSocket.accept();
                    din = new DataInputStream(clientSocket.getInputStream());
                    dout = new DataOutputStream(clientSocket.getOutputStream());
                    System.out.println("Client connected");

                    // Handle messages from the client
                    new Thread(() -> {
                        try {
                            while (true) {
                                String msg = din.readUTF();
                                SwingUtilities.invokeLater(() -> {
                                    Message message = new Message("Client", msg, LocalDateTime.now());
                                    displayMessage(message);
                                });
                            }
                        } catch (IOException e) {
                            System.out.println("Connection lost");
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void connectToOtherServer(String host, int port) {
        new Thread(() -> {
            try {
                otherServerSocket = new Socket(host, port);
                DataInputStream dinOther = new DataInputStream(otherServerSocket.getInputStream());
                DataOutputStream doutOther = new DataOutputStream(otherServerSocket.getOutputStream());

                // Handle messages from the other server
                new Thread(() -> {
                    try {
                        while (true) {
                            String msg = dinOther.readUTF();
                            SwingUtilities.invokeLater(() -> {
                                Message message = new Message("Other Server", msg, LocalDateTime.now());
                                displayMessage(message);
                            });
                        }
                    } catch (IOException e) {
                        System.out.println("Connection lost");
                        e.printStackTrace();
                    }
                }).start();
                
                // Forward messages from this server to the other server
                dout = doutOther; // Forwarding messages to the other server

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage() {
        String messageText = enter.getText().trim();
        if (!messageText.isEmpty()) {
            try {
                Message message = new Message("Server", messageText, LocalDateTime.now());
                displayMessage(message);
                dout.writeUTF(messageText);
                dout.flush();
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
        if (message.getSender().equals("Server")) {
            messageLabel.setBackground(Color.BLACK); // Color for server messages
            messageLabel.setForeground(Color.WHITE);
        } else if (message.getSender().equals("Client")) {
            messageLabel.setBackground(new Color(0, 132, 255)); // Color for client messages
            messageLabel.setForeground(Color.WHITE);
        } else if (message.getSender().equals("Other Server")) {
            messageLabel.setBackground(new Color(255, 165, 0)); // Color for other server messages
            messageLabel.setForeground(Color.WHITE);
        }

        messagePanel.add(messageLabel);

        JLabel timestampLabel = new JLabel(message.getFormattedTimestamp());
        timestampLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        timestampLabel.setForeground(Color.GRAY);
        messagePanel.add(timestampLabel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(messagePanel);
        vertical.add(rightPanel);
        vertical.add(Box.createVerticalStrut(5));
        chatArea.add(vertical);
        frame.revalidate();
    }

    private JPanel createChatPanel(String message) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel messageLabel = new JLabel("<html><p style='width: 200px;'>" + message + "</p></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(0, 132, 255)); // Example color for received messages
        messageLabel.setForeground(Color.WHITE);
        panel.add(messageLabel);

        JLabel timestampLabel = new JLabel(getFormattedTimestamp());
        timestampLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        timestampLabel.setForeground(Color.GRAY);
        panel.add(timestampLabel);

        return panel;
    }

    private String getFormattedTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return timestamp.format(formatter);
    }

    private JLabel createLabel(String text, String iconPath, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        if (iconPath != null) {
            label.setIcon(new ImageIcon(iconPath));
        }
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(x, y, width, height);
        return label;
    }

    private static class Message {
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

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getFormattedTimestamp() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            return timestamp.format(formatter);
        }
    }
}
