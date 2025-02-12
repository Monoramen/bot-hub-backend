import axios from "axios";

const apiClientCommands =  axios.create({
  baseURL: "http://localhost:8080/commands", // Замените на ваш базовый URL
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiClientCommands;
