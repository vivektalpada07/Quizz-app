import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import HomePage from './components/HomePage';
import SignIn from './components/SignIn';
import SignUp from './components/SignUp';
import AdminPage from './components/AdminPage';
import PlayerPage from './components/PlayerPage';
import QuizPage from './components/QuizPage';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route exact path='/' element={<HomePage />}/> 
          <Route path='/signIn' element={<SignIn />}/>
          <Route path='/signUp' element={<SignUp />}/> 
          <Route path='/adminPage' element={<AdminPage />}/> 
          <Route path='/playerPage' element={<PlayerPage />}/>
          <Route path='/quiz' element={<QuizPage />}/>         
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
