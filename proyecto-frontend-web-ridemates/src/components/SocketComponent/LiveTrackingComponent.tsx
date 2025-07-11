import React, { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

export default function LiveTrackingComponent() {
  const [trackingData, setTrackingData] = useState<any[]>([]);
  const [client, setClient] = useState<Client | null>(null);
  const [isConnected, setIsConnected] = useState<boolean>(false);

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/tracking");
    const stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => {
        console.log(str);
      },
      onConnect: () => {
        console.log("WebSocket connection established");
        setIsConnected(true);
        stompClient.subscribe("/channel/tracking/", (message) => {
          const data = JSON.parse(message.body);
          setTrackingData((prevData) => [...prevData, data]);
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
  }, []);

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
      <div className="flex-grow overflow-y-auto bg-gray-100 p-4 rounded-lg shadow-inner">
        {trackingData.map((data, index) => (
          <div key={index} className="mb-2 p-2 bg-white rounded-lg shadow">
            <p>Latitude: {data.latitude}</p>
            <p>Longitude: {data.longitude}</p>
            <p>Timestamp: {new Date(data.timestamp).toLocaleString()}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
