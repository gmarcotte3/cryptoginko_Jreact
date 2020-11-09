import React from 'react'
import ginkoLogo from './ginkoLogo.svg';
import styled from 'styled-components'
import cog from './cog.svg';

// styled header
const Header = styled.header`
    background-color: #282c34;
    background: transparent url('./headerBackground.png') 0% 0% no-repeat;
    
    min-height: 25vh;
    max-height: 152px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: left;
    
    color: white;
    text-align: left;
`;
const MenuSection = styled.div`
    top: 1000px;
    left: 0px;
    width: 1920px;
    height: 52px;
    background: transparent linear-gradient(180deg, #000000 0%, #545454 100%) 0% 0% no-repeat padding-box;
    opacity: 1;
    text-align: right;
`;

// stypled logo
const Img = styled.img`
    height: 4rem;
    pointer-events: none;
`;

// stypled logo
const Img2 = styled.img`
    height: 2rem;
    pointer-events: none;
`;

//styled header hi
const H1 = styled.h1`
    font-size: 4rem;
`;


export default function GinkoHeader(props) {
    return (
        <>
        <Header>
            <table>
                <tbody>
                    <tr>
                        <td><Img src={ginkoLogo} alt="ginko logo" /></td>
                    </tr>
                    <tr>
                        <MenuSection>
                        <Img2 src={cog} alt="settings cog" /><br />
                        </MenuSection>
                    </tr>
                </tbody>
            </table>
        </Header>
        
        </>
    );
}