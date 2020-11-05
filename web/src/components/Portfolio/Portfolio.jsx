import React, {useState, useEffect} from 'react'
import styled from 'styled-components'
import Tabs from  "../Tabs/Tabs";
import PortfolioByCoins from '../PortfolioByCoins/PortfolioByCoins';


// styled TD
const TD = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
`;

const Div = styled.div`
    background: transparent linear-gradient(180deg, #393939 0%, #7A7A7A 100%) 0% 0% no-repeat padding-box;
    opacity: 1;
`;

export default function Portfolio(props) {
   
    return (
            <Div>
                <Tabs>
                    <div label="by coins">
                        <PortfolioByCoins 
                        defaultFiatCurrency={props.defaultFiatCurrency} 
                        portfolioUrl={props.portfolioUrl} 
                        portfolioByCoinsUrl={props.portfolioByCoinsUrl}
                        />
                    </div>
                    <div label="by wallets">
                    </div>
                </Tabs>
            </Div>
        )
    
}
