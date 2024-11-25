import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Form, Button, Modal, Table } from 'react-bootstrap';
import UserHeader from './UserHeader';
import Footer from './Footer';
import { getQuizzes, createQuiz, updateQuiz, deleteQuiz, getQuizDetails } from '../services/AdminService';

function AdminPage() {
  const [quizzes, setQuizzes] = useState([]);
  const [newQuiz, setNewQuiz] = useState({ username: '', name: '' , category: '', difficulty: ''});
  const [editQuiz, setEditQuiz] = useState(null);
  const [showEditModal, setShowEditModal] = useState(false);
  const [deleteQuizPrompt, setDeleteQuizPrompt] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [selectedQuizDetails, setSelectedQuizDetails] = useState(null);

  // Fetch quizzes when the component loads
  useEffect(() => {
    fetchQuizzes();
  }, []);

  const fetchQuizzes = async () => {
    try {
      const response = await getQuizzes();
      setQuizzes(response.data);
    } catch (error) {
      console.error('Error fetching quizzes:', error);
    }
  };

  // Fetch quiz details when a quiz is clicked
  const fetchQuizDetails = async (id) => {
    try {
      const response = await getQuizDetails(id);
      setSelectedQuizDetails(response.data);
    } catch (error) {
      console.error('Error fetching quiz details:', error);
    }
  };

  // Handle the create quiz tournament
  const handleCreateQuiz = async () => {
    // Validate form fields
    if (!newQuiz.username.trim()) {
        alert("Creator field cannot be blank.");
        return;
    }
    if (!newQuiz.name.trim()) {
        alert("Name field cannot be blank.");
        return;
    }
    if (!newQuiz.category.trim()) {
        alert("Category field cannot be blank.");
        return;
    }
    if (!newQuiz.difficulty.trim()) {
        alert("Difficulty field cannot be blank.");
        return;
    }

    try {
      await createQuiz(newQuiz);
      setNewQuiz({ username: '', name: '' , category: '', difficulty: ''});
      fetchQuizzes();
    } catch (error) {
      console.error('Error creating quiz:', error);
    }
  };

  // Handle the update quiz tournament
  const handleUpdateQuiz = async (id, updatedQuizData) => {
    if (!id || !updatedQuizData) return; 
    try {
        await updateQuiz(id, updatedQuizData);
        setEditQuiz(null);
        setShowEditModal(false); 
        fetchQuizzes();
    } catch (error) {
        console.error('Error updating quiz:', error);
    }
  };

  // Handle the delete quiz tournament
  const handleDeleteQuiz = async (id) => {
    try {
      await deleteQuiz(id);
      setShowDeleteModal(false);
      fetchQuizzes();
    } catch (error) {
      console.error('Error deleting quiz:', error);
    }
  };

  return (
    <div>
    <UserHeader/>
    <Container className="mt-4">
      <h1 className="text-center mb-4">Welcome to the Admin Page</h1>
      <br/><br/>
      {/* Button to Retrieve All Products */}
      <Row className="mb-4">
        <Col>
          <Button variant="info" onClick={fetchQuizzes}>
            Retrieve All Quizzes
          </Button>
        </Col>
      </Row>

      {/* Form to create a new quiz */}
      <Row className="d-flex justify-content-center mb-4">
        <Col md={6}>
          <h3>Create Quiz</h3>
          <br/>
          <Form>
            <Form.Group className="mb-3 d-flex align-items-center" controlId="formUsername">
              <Form.Label className="me-3 text-start" style={{ minWidth: '100px' }}>Creator</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter Creator"
                value={newQuiz.username}
                onChange={(e) => setNewQuiz({ ...newQuiz, username: e.target.value })}
              />
            </Form.Group>

            <Form.Group className="mb-3 d-flex align-items-center" controlId="formName">
              <Form.Label className="me-3 text-start" style={{ minWidth: '100px' }}>Name</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter Name"
                value={newQuiz.name}
                onChange={(e) => setNewQuiz({ ...newQuiz, name: e.target.value })}
              />
            </Form.Group>

            <Form.Group className="mb-3 d-flex align-items-center" controlId="formCategory">
              <Form.Label className="me-3 text-start" style={{ minWidth: '100px' }}>Category</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter Category"
                value={newQuiz.category}
                onChange={(e) => setNewQuiz({ ...newQuiz, category: e.target.value })}
              />
            </Form.Group>

            <Form.Group className="mb-3 d-flex align-items-center" controlId="formDifficulty">
              <Form.Label className="me-3 text-start" style={{ minWidth: '100px' }}>Difficulty</Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter Difficulty"
                value={newQuiz.difficulty}
                onChange={(e) => setNewQuiz({ ...newQuiz, difficulty: e.target.value })}
              />
            </Form.Group>

            {/* This button is used to add the quizzes */}
            <Button variant="primary" onClick={handleCreateQuiz}>
              Add Quiz
            </Button>
          </Form>
        </Col>
      </Row>


      {/* List of quizzes */}
      <Row className="d-flex justify-content-center mb-4">
        <Col md={8}>
          <h3>Quiz Tournaments</h3>
          <Table striped bordered>
            <thead>
              <tr>
                <th>Creator</th>
                <th>Name</th>
                <th>Category</th>
                <th>Difficulty</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {quizzes.map((quiz) => (
                <tr key={quiz.id}>
                  <td>{quiz.username}</td>
                  <td>
                    <Button
                        variant="link"
                        onClick={() => fetchQuizDetails(quiz.id)} // Fetch quiz details when name is clicked
                      >
                    {quiz.name}
                    </Button>
                    </td>
                  <td>{quiz.category}</td>
                  <td>{quiz.difficulty}</td>
                  <td>
                    {/* This button is used to update the quizzes */}
                    <Button
                      variant="warning"
                      className="me-2"
                      onClick={() => {
                        setEditQuiz(quiz);
                        setShowEditModal(true);
                      }}
                    >
                      Update
                    </Button>
                    {/* This button is used to delete the quizzes */}
                    <Button variant="danger" onClick={() => handleDeleteQuiz(quiz.id)}>
                      Delete
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>


      {/* Modal for editing a quiz */}
      <Modal show={showEditModal} onHide={() => setShowEditModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Update Quiz</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {editQuiz && (
            <Form>
              <Form.Group className="mb-3" controlId="editUsername">
                <Form.Label>Username</Form.Label>
                <Form.Control
                  type="text"
                  value={editQuiz.username}
                  onChange={(e) => setEditQuiz({ ...editQuiz, username: e.target.value })}
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="editName">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  type="text"
                  value={editQuiz.name}
                  onChange={(e) => setEditQuiz({ ...editQuiz, name: e.target.value })}
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="editCategory">
                <Form.Label>Category</Form.Label>
                <Form.Control
                  type="text"
                  value={editQuiz.category}
                  onChange={(e) => setEditQuiz({ ...editQuiz, category: e.target.value })}
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="editDifficulty">
                <Form.Label>Difficulty</Form.Label>
                <Form.Control
                  type="text"
                  value={editQuiz.difficulty}
                  onChange={(e) => setEditQuiz({ ...editQuiz, difficulty: e.target.value })}
                />
              </Form.Group>
              <Button variant="primary" 
                onClick={() => {
                    if (editQuiz && editQuiz.id) {
                    handleUpdateQuiz(editQuiz.id, editQuiz);
                    } else {
                    console.error('Edit quiz data is invalid or missing an ID.');
                    alert('Unable to update quiz. Please try again.');
                    }
                }}>
                Update
              </Button>
            </Form>
          )}
        </Modal.Body>
      </Modal>

      {/* Modal for delete confirmation */}
      <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Confirm Deletion</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <p>Are you sure you want to delete this quiz?</p>
            <Button
              variant="danger"
              onClick={() => {
                if (deleteQuizPrompt && deleteQuizPrompt.id) {
                  handleDeleteQuiz(deleteQuizPrompt.id);
                } else {
                  console.error('delete quiz prompt is invalid.');
                }
              }}
            >
              Delete
            </Button>
            <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
              Cancel
            </Button>
          </Modal.Body>
        </Modal>

        {/* Modal for displaying quiz details */}
        <Modal show={selectedQuizDetails !== null} onHide={() => setSelectedQuizDetails(null)}>
          <Modal.Header closeButton>
            <Modal.Title>Quiz Details</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {selectedQuizDetails ? (
              <div>
                <h4>Questions</h4>
                <ul>
                  {selectedQuizDetails.questions.map((question, index) => (
                    <li key={index}>
                      <strong>{question.text}</strong>
                      <div>Correct Answer: {question.correctAnswer}</div>
                      <div>Incorrect Answers: {question.incorrectAnswers.join(', ')}</div>
                    </li>
                  ))}
                </ul>
              </div>
            ) : (
              <p>Loading quiz details...</p>
            )}
          </Modal.Body>
        </Modal>
    </Container>
    <Footer/>
    </div>
  );
}

export default AdminPage;
