import bodyParser from 'body-parser';
import cors from 'cors';
import express from 'express';
import { configureServerForUserStory1 } from './userstory1/api';

// Server configuration
const app = express();
const port = 8181;

app.listen(port, () => {
  console.log(`Backend for the 'Angular Demo App' started and listening on port ${ port }..`)
})

// Middleware configuration
app.use(cors());
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json());

// Configure server for user stories
configureServerForUserStory1(app);
