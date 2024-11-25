import axios from 'axios';

const API_URL = 'http://localhost:8080/api/quizzes'; 

const getQuizzes = () => axios.get(`${API_URL}/ongoing`); 
const getQuizDetails = (id) => axios.get(`${API_URL}/${id}`); 

export { getQuizzes, getQuizDetails };
