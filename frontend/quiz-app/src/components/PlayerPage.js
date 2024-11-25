import React, { useEffect, useState } from 'react';
import Footer from './Footer';
import UserHeader from './UserHeader';
import { useNavigate } from 'react-router-dom';
import { Button, Col, Row, Table } from 'react-bootstrap';
import { getQuizzes } from '../services/PlayerService';

function PlayerPage() {
    const [ongoingQuizzes, setOngoingQuizzes] = useState([]);
    const navigate = useNavigate();

    // Fetch ongoing quizzes when the component loads
    useEffect(() => {
        fetchOngoingQuizzes();
    }, []);

    const fetchOngoingQuizzes = async () => {
        try {
        const response = await getQuizzes(); // Get quizzes for the player
        // Filter ongoing quizzes
        const ongoing = response.data.filter((quiz) => quiz.status === 'ongoing');
        setOngoingQuizzes(ongoing);
        } catch (error) {
        console.error('Error fetching quizzes:', error);
        }
    };

    const handleJoinQuiz = (id) => {
        // Redirect to the QuizPage according to the id
        navigate('/quiz/${id}');
    };

    return (
        <div>
            <UserHeader/>
            <br/>
            <div align="center">
            <h1>Welcome to the Player Page</h1>
            <br/>
            {/* List of quizzes */}
            <Row className="d-flex justify-content-center mb-4">
                <Col md={8}>
                <h3>Quiz Tournaments</h3>
                <Table striped bordered>
                    <thead>
                    <tr>
                        <th>Quiz Name</th>
                        <th>Category</th>
                        <th>Difficulty</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {ongoingQuizzes.map((quiz) => (
                        <tr key={quiz.id}>
                        <td>{quiz.name}</td>
                        <td>{quiz.category}</td>
                        <td>{quiz.difficulty}</td>
                        <td>
                        <Button variant="primary" onClick={() => handleJoinQuiz(quiz.id)}>
                        Join
                        </Button>
                        </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
                </Col>
            </Row>
            </div>
            <Footer/>
        </div>
        
    );
};

export default PlayerPage;
