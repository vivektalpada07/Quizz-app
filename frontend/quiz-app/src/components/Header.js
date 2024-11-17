import { Container, Nav, Navbar } from "react-bootstrap";

function Header() {
    return (
        <Navbar bg="primary" data-bs-theme="dark">
            <Container>
                <Navbar.Brand href="#home"></Navbar.Brand>
                <Nav className="ml-auto" >
                    <Nav.Link href="/">Home</Nav.Link>
                    <Nav.Link href="/signIn">Sign In</Nav.Link>
                    <Nav.Link href="/signUp">Sign Up</Nav.Link>
                </Nav>
            </Container>
        </Navbar>
    );
}

export default Header;