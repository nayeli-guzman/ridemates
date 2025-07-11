import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
import { useAuthContext } from "../../contexts/AuthContext";
import { getUsername } from "../../utils/jwtUtils";

export default function LiveChatComponent() {
  const [messages, setMessages] = useState<any[]>([]);
  const [input, setInput] = useState<string>("");
  const [client, setClient] = useState<Client | null>(null);
  const [isConnected, setIsConnected] = useState<boolean>(false);
  const context = useAuthContext();
  const username = getUsername(context.session);

  useEffect(() => {
    if (context.isLoading) return;
    const socket = new SockJS("http://localhost:8080/chat");
    const stompClient = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: `Bearer ${context.session}`,
      },
      debug: (str) => {
        console.log(str);
      },
      onConnect: () => {
        console.log("WebSocket connection established");
        setIsConnected(true);
        stompClient.subscribe(`/channel/private/${username}`, (message) => {
          setMessages((prevMessages) => [...prevMessages, message.body]);
        });
      },
      onDisconnect: () => {
        console.log("WebSocket connection closed");
        setIsConnected(false);
      },
      onStompError: (error) => {
        console.error("WebSocket error:", error);
      },
    });

    stompClient.activate();

    setClient(stompClient);
    return () => {
      stompClient.deactivate();
    };
  }, [context.isLoading]);

  const sendMessage = () => {
    if (client && input.trim()) {
      const response = JSON.stringify({
        content: input,
      });
      client.publish({ destination: "/app/chat", body: response });
      setInput("");
    }
  };

  const formatDate = (millis: number) => {
    const date = new Date(millis);
    return date.toLocaleTimeString();
  };

  return (
    <div className="flex flex-col w-full h-full p-4">
      <div className="flex justify-between mb-4">
        <button
          onClick={() => client?.deactivate()}
          className="p-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
          disabled={!isConnected}
        >
          Disconnect
        </button>
      </div>
      <div className="flex mt-4">
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          className="flex-grow p-2 border border-gray-300 rounded-l-lg focus:outline-none"
          placeholder="Type a message..."
        />
        <button
          onClick={sendMessage}
          className="p-2 bg-blue-500 text-white rounded-r-lg hover:bg-blue-600"
          disabled={!isConnected}
        >
          Send
        </button>
      </div>
      <div className="flex-grow overflow-y-auto bg-gray-100 p-4 rounded-lg shadow-inner">
        {messages.map((message, index) => (
          <div key={index} className="mb-2 p-2 bg-white rounded-lg shadow">
            {`[${formatDate(message.time)}] ${message.from}: ${message.content}`}
          </div>
        ))}
      </div>
    </div>
  );
}
