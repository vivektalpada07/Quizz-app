import React from 'react';
import Header from './Header';
import Footer from './Footer';

function HomePage() {
    return (
        <div>
            <Header/>
            <br/>
            <div align="center">
            <h1>Welcome to the Quiz Tournament</h1>
            <br/><br/><br/>
            <h4 align='left'>Tournaments</h4>
            </div>
            <Footer/>
        </div>
        
    );
};

export default HomePage;
