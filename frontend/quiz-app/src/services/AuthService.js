import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

const signIn = (signInData) => axios.post(`${API_URL}/login`, signInData);
const signUp = (signUpData) => axios.post(`${API_URL}/register`, signUpData);
const logOut = () => axios.post(`${API_URL}/logout`);

// Retrieve the user details to determine the role.
const getUserDetails = (id) =>
    axios.get(`http://localhost:8080/api/users/${id}`);

export { signIn, signUp, logOut, getUserDetails };
