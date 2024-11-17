import { Container, Navbar } from "react-bootstrap";

function Footer() {
    return(
        <Navbar bg="primary" data-bs-theme="dark">
            <Container>
                <p>Copyright &copy; 2024</p>
            </Container>
        </Navbar>
    );
}

export default Footer;