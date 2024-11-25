import React, { useState, useEffect } from 'react';
import { Button, Modal } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom'; // Use useHistory for navigation in React Router v5 or useNavigate in React Router v6
import { getQuizDetails } from '../services/PlayerService'; 

function QuizPage() {
  const [quiz, setQuiz] = useState(null);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [score, setScore] = useState(0);
  const [showFeedback, setShowFeedback] = useState(false);
  const [feedbackMessage, setFeedbackMessage] = useState('');
  const [selectedAnswer, setSelectedAnswer] = useState('');

  const { id, questionIndex } = useParams(); 
  const navigate = useNavigate();

  // Fetch quiz details when the component loads
  useEffect(() => {
    fetchQuizDetails();
  }, []);

  const fetchQuizDetails = async () => {
    try {
      const response = await getQuizDetails(id);
      setQuiz(response.data);
    } catch (error) {
      console.error('Error fetching quiz details:', error);
    }
  };

  const handleAnswerSelect = (answer) => {
    setSelectedAnswer(answer);
  };

  const handleNextQuestion = () => {
    if (selectedAnswer === quiz.questions[currentQuestionIndex].correctAnswer) {
      setScore(score + 1);
      setFeedbackMessage('Correct! Well done.');
    } else {
      setFeedbackMessage('Incorrect! The correct answer is: ' + quiz.questions[currentQuestionIndex].correctAnswer);
    }
    setShowFeedback(true);

    setTimeout(() => {
      setShowFeedback(false);
      if (currentQuestionIndex + 1 < quiz.questions.length) {
        // Move to next question
        navigate('/quiz/${id}/question/${currentQuestionIndex + 1}');
      } else {
        alert(`Quiz completed! Your score: ${score + 1}`);
      }
    }, 2000); // Wait 2 seconds before moving to next question
  };

  if (!quiz) return <div>Loading quiz...</div>;

  const currentQuestion = quiz.questions[currentQuestionIndex];

  return (
    <div>
      <h1>{quiz.name}</h1>
      <div>
        <h4>{currentQuestion.text}</h4>
        {currentQuestion.answers.map((answer) => (
          <Button
            key={answer}
            variant="outline-primary"
            onClick={() => handleAnswerSelect(answer)}
          >
            {answer}
          </Button>
        ))}
      </div>

      <Button variant="primary" onClick={handleNextQuestion}>
        Next
      </Button>

      {/* Feedback Modal */}
      <Modal show={showFeedback} onHide={() => setShowFeedback(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Feedback</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>{feedbackMessage}</p>
        </Modal.Body>
      </Modal>
    </div>
  );
}

export default QuizPage;
