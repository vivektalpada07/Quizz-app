import { Button, Container, Nav, Navbar } from "react-bootstrap";
import { logOut } from "../services/AuthService";
import { useNavigate } from "react-router-dom";

function UserHeader() {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
          await logOut(); // This will call the logOut API
          navigate('/signIn'); // Redirect to the login page
        } catch (error) {
          console.error('Error during logout:', error);
        }
    };

    return (
        <Navbar bg="primary" data-bs-theme="dark">
            <Container>
                <Navbar.Brand href="#home"></Navbar.Brand>
                <Nav className="ml-auto" >
                    <Nav.Link href="/">Home</Nav.Link>
                    <Nav.Link href="/signIn">Sign In</Nav.Link>
                    <Nav.Link href="/signUp">Sign Up</Nav.Link>
                    <Button onClick={handleLogout}>Logout</Button>
                </Nav>
            </Container>
        </Navbar>
    );
}

export default UserHeader;