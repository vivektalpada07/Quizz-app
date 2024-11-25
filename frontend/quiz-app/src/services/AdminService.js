import axios from 'axios';

const API_URL = 'http://localhost:8080/api/users/admin/quizzes';

const getQuizzes = () => axios.get(API_URL);
const createQuiz = (quiz) => axios.post(API_URL, quiz);
const updateQuiz = (id, updatedQuizData) => axios.put(`${API_URL}/${id}`, updatedQuizData);
const deleteQuiz = (id) => axios.delete(`${API_URL}/${id}`);
const getQuizDetails = (id) => axios.get(`${API_URL}/${id}`);

export { getQuizzes, createQuiz, updateQuiz, deleteQuiz, getQuizDetails};
