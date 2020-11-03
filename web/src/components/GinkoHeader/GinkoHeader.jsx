import React from 'react'
import ginkoLogo from './ginkoLogo.svg';
import styled from 'styled-components'

// styled header
const Header = styled.header`
background-color: #282c34;
    min-height: 20vh;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: left;
    
    color: white;
`;

// stypled logo
const Img = styled.img`
    height: 4rem;
    pointer-events: none;
`;

//styled header hi
const H1 = styled.h1`
    font-size: 4rem;
`;


export default function GinkoHeader(props) {
    return (
        <Header>
          <Img src={ginkoLogo} alt="ginko logo" />
        </Header>
    );
}