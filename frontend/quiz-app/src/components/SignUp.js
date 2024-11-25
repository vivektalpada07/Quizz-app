import React, { useState } from 'react';
import { signUp } from '../services/AuthService';
import Header from './Header';
import Footer from './Footer';
import { useNavigate } from 'react-router-dom';
import { Alert, Button, Col, Container, Form, Row } from 'react-bootstrap';

function SignUp() {
    const [signUpData, setSignUpData] = useState({ username: '', firstName: '', lastName: '', email: '', password: '' , role: 'PLAYER'});
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const navigate = useNavigate();

    // Handle sign-in submission
    const handleSignUp = async () => {
        setError(null);
        setSuccess(null);
        
        try {
        await signUp(signUpData.username, signUpData.firstName, signUpData.lastName, signUpData.email, signUpData.password);
        setSuccess('Your Sign Up is successful!');

        // Redirect to the sign in page after successful sign up
        navigate('/signIn');
        } catch (err) {
        console.error('Error signing up:', err);
        setError(err.message || 'An error occurred during sign-up.');
        }
    };

    return (
        <div>
            <Header/>
            <br/>
            <div align="center">
            <Container className="mt-4">
            <h1>Sign Up</h1>
            <br/>
            {/* Form to sign up */}
            <Row className="d-flex justify-content-center mb-4">
                <Col md={6}>
                {error && <Alert variant="danger">{error}</Alert>}
                {success && <Alert variant="success">{success}</Alert>}
                <Form style={{ maxWidth: '400px', width: '100%' }}>
                    <Form.Group className="mb-3 d-flex align-items-center" controlId="formUsername">
                    <Form.Label className="me-3 text-start" style={{ minWidth: '100px' }}>Username :</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Enter Username"
                        value={signUpData.username}
                        onChange={(e) => setSignUpData({ ...signUpData, username: e.target.value })}
                    />
                    </Form.Group>

                    <Form.Group className="mb-3 d-flex align-items-center" controlId="formFirstName">
                    <Form.Label className="me-3 text-start" style={{ minWidth: '100px' }}>First Name :</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Enter First Name"
                        value={signUpData.firstName}
                        onChange={(e) => setSignUpData({ ...signUpData, firstName: e.target.value })}
                    />
                    </Form.Group>

                    <Form.Group className="mb-3 d-flex align-items-center" controlId="formLastName">
                    <Form.Label className="me-3 text-start" style={{ minWidth: '100px' }}>Last Name :</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Enter Last Name"
                        value={signUpData.lastName}
                        onChange={(e) => setSignUpData({ ...signUpData, lastName: e.target.value })}
                    />
                    </Form.Group>

                    <Form.Group className="mb-3 d-flex align-items-center" controlId="formEmail">
                    <Form.Label className="me-3 text-start" style={{ minWidth: '100px' }}>Email :</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Enter Email"
                        value={signUpData.email}
                        onChange={(e) => setSignUpData({ ...signUpData, email: e.target.value })}
                    />
                    </Form.Group>

                    <Form.Group className="mb-3 d-flex align-items-center" controlId="formPassword">
                    <Form.Label className="me-3 text-start" style={{ minWidth: '100px' }}>Password :</Form.Label>
                    <Form.Control
                        type="password"
                        placeholder="Enter Password"
                        value={signUpData.password}
                        onChange={(e) => setSignUpData({ ...signUpData, password: e.target.value })}
                    />
                    </Form.Group>

                    {/* This button is used to sign up */}
                    <Button variant="primary" onClick={handleSignUp}>
                    Sign Up
                    </Button>
                </Form>
                </Col>
            </Row>
            </Container>
            </div>
            <Footer/>
        </div>
        
    );
};

export default SignUp;
