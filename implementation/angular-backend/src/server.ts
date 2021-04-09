import bodyParser from 'body-parser';
import cors from 'cors';
import express from 'express';
import { createServer } from 'http';
import { Server } from 'socket.io';
import { configureServerForUserStory1 } from './userstory1/api';
import { RealTimeData } from './userstory1/model/realtime-data.interface';

// Server configuration
const app = express();
// Middleware configuration
app.use(cors());
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json());

const server = createServer(app);
const io = new Server(server, {
  cors: {
    origin: '*'
  }
});
const port = 8181;

server.listen(port, () => {
  console.log(`Backend for the 'Angular Demo App' started and listening on port ${ port }..`)
})

// WebSocket configuration
io.on('connection', (socket) => {
  socket.on('clientRealTimeUpdate', (data: RealTimeData) => {
    console.log(data);
    socket.emit('serverRealTimeUpdate', {
      senderId: data.senderId
    });
  });
});

// Configure server for user stories
configureServerForUserStory1(app);
