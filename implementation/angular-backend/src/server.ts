import bodyParser from 'body-parser';
import cors from 'cors';
import express from 'express';
import fileUpload from 'express-fileupload';
import { createServer } from 'http';
import { Server } from 'socket.io';
import { configureServerForUserStory1 } from './userstory1/api';
import { configureServerForUserStory2 } from './userstory2/api';

// Server configuration
const app = express();
const server = createServer(app);
const io = new Server(server, {
  cors: {
    origin: '*'
  }
});
const port = 8181;

// Middleware configuration
app.use(cors());
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json({ limit: '50MB' }));
app.use(fileUpload());

// Tell server to listen to incoming requests
server.listen(port, () => {
  console.log(`Backend for the 'Angular Demo App' started and listening on port ${ port }..`)
})

// Configure server for user stories
configureServerForUserStory1(app, io);
configureServerForUserStory2(app);
