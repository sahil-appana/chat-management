# Chat Management System

A simple and secure chat application for two users, built with Java Swing and Socket Programming.

## 📌 Features
- **Real-time Messaging**: Instant text-based chat between two users.
- **Secure Communication**: Implements **SSL/TLS** for encrypted message exchange.
- **User-Friendly UI**: Java Swing-based graphical interface.
- **Message History**: Logs chat history locally.
- **Typing Indicator**: Shows when the other user is typing.
- **Cross-Platform Compatibility**: Works on any OS with Java installed.

## 🛠️ Tech Stack
- **Programming Language**: Java  
- **UI Framework**: Swing  
- **Networking**: Socket Programming (TCP/IP)  
- **Security**: SSL/TLS  

## 🚀 Installation & Setup

### 1️⃣ Clone the Repository
```sh
git clone https://github.com/sahil-appana/chat-management.git
cd chat-management
```

### 2️⃣ Run the Chat Server
```sh
javac ChatServer.java
java ChatServer
```

### 3️⃣ Run the Chat Client (On Each User's Device)
```sh
javac ChatClient.java
java ChatClient
```
## 📌 Usage
1. Start the **server** on one machine.
2. Start the **client** on two different machines.
3. Connect using the provided **IP address & port**.
4. Begin chatting securely.

## 🛠 Troubleshooting
- **Connection Error?** Check if the server is running and firewall settings allow connections.
- **Messages Not Sending?** Ensure both clients are connected to the same network.
- **Encryption Issues?** Verify that the SSL certificate is correctly configured.


## 🤝 Contributing
Want to improve the chat system? Feel free to:
- Report issues in the [Issues](https://github.com/sahil-appana/chat-management/issues) tab.
- Submit a pull request with enhancements.

