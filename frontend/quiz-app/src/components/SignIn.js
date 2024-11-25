import React, { useState } from 'react';
import { signIn, getUserDetails } from '../services/AuthService';
import Header from './Header';
import Footer from './Footer';
import { Link, useNavigate } from 'react-router-dom';
import { Alert, Button, Col, Container, Form, Row } from 'react-bootstrap';

function SignIn() {
    const [signInData, setSignInData] = useState({ username: '', password: '' });
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const navigate = useNavigate();

    // Handle sign-in submission
    const handleSignIn = async () => {
        setError(null);
        setSuccess(null);
        
        try {
            // Authenticate the user
            const signInResponse = await signIn(signInData);
            const { id } = signInResponse.data; 
    
            // Fetch user details by ID
            const userDetailsResponse = await getUserDetails(id);
            const { role } = userDetailsResponse.data;
    
            // Redirect based on role
            if (role === 'ADMIN') {
                navigate('/adminPage');
            } else if (role === 'PLAYER'){
                navigate('/playerPage');
            } else{
                navigate('/');
            }
        } catch (err) {
            console.error('Error during sign in or fetching user details:', err);
        }
    };

    return (
        <div>
            <Header/>
            <br/>
            <div align="center">
            <Container className="mt-4">
            <h1>Sign In</h1>
            <br/>
            {/* Form to sign in */}
            <Row className="d-flex justify-content-center mb-4">
                <Col md={6}>
                {error && <Alert variant="danger">{error}</Alert>}
                {success && <Alert variant="success">{success}</Alert>}
                <Form style={{ maxWidth: '400px', width: '100%' }}>
                    <Form.Group className="mb-3 d-flex align-items-center" controlId="formUsername">
                    <Form.Label className="me-3 text-start" style={{ minWidth: '80px' }}>Username :</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Enter Username"
                        value={signInData.username}
                        onChange={(e) => setSignInData({ ...signInData, username: e.target.value })}
                    />
                    </Form.Group>

                    <Form.Group className="mb-3 d-flex align-items-center" controlId="formPassword">
                    <Form.Label className="me-3 text-start" style={{ minWidth: '80px' }}>Password :</Form.Label>
                    <Form.Control
                        type="password"
                        placeholder="Enter Password"
                        value={signInData.password}
                        onChange={(e) => setSignInData({ ...signInData, password: e.target.value })}
                    />
                    </Form.Group>

                    {/* This button is used to sign in */}
                    <Button variant="primary" onClick={handleSignIn}>
                    Sign In
                    </Button>
                </Form>
                <div className="text-center mt-3">
                    <p>
                    Don't have an account?{' '}
                    <Link to="/signUp" className="text-primary">
                        Sign Up
                    </Link>
                    </p>
                </div>
                </Col>
            </Row>
            </Container>
            </div>
            <Footer/>
        </div>
        
    );
};

export default SignIn;
