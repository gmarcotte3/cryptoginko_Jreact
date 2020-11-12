import React from 'react'
import ginkoLogo from './ginkoLogo.svg';
import styled from 'styled-components'
import cog from './cog.png';


const DivBanner = styled.div`
    background-color: #282c34;
    background: transparent url('./headerBackground.png') 0% 0% no-repeat;
    height: 150px;
`;

const MenuSection = styled.div`
    position: relative;
    top: 0 px;
    left: 0 px;
    width: 1920px;
    height: 90px;
    background: transparent linear-gradient(180deg, #000000 0%, #545454 100%) 0% 0% no-repeat padding-box;
    opacity: 1;
`;

// stypled logo
const Img = styled.img`
    height: 4rem;
    pointer-events: none;
    position: absolute;
    top: 40px;
`;

// stypled cog
const Img2 = styled.img`
    height: 2rem;
    pointer-events: none;
    position: absolute;
    left: 30px;
    top: 15px;
    
`;

//styled header hi
const H1 = styled.h1`
    font-size: 4rem;
`;


export default function GinkoHeader(props) {
    return (
        <>
        <DivBanner>
            <Img src={ginkoLogo} alt="ginko logo" />
            
        </DivBanner>
            <table>
                <tbody>
                    <tr>
                        <MenuSection>
                            <Img2 src={cog} alt="settings cog" /><br />
                        </MenuSection>
                    </tr>
                </tbody>
            </table>

        </>
    );
}
