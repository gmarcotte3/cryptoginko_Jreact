import React from 'react'
import styled from 'styled-components'
import Tabs from  "../Tabs/Tabs";
import PortfolioByCoins from '../PortfolioByCoins/PortfolioByCoins';
import PortfolioByWallet from '../PortfolioByWallet/PortfolioByWallet';


const Div = styled.div`
    background: #2B2B2B 0% 0% no-repeat padding-box;
    //background: transparent linear-gradient(180deg, #393939 0%, #7A7A7A 100%) 0% 0% no-repeat padding-box;
    opacity: 1;
    height: 44px
`;



export default function Portfolio(props) {

    console.log( props);
   
    return (
            <Div>
                <Tabs>
                    <div label="by coins">
                        <PortfolioByCoins 
                        defaultFiatCurrency={props.defaultFiatCurrency} 
                        portfolioUrl={props.portfolioUrl} 
                        portfolioByCoinsUrl={props.portfolioByCoinsUrl}
                        totalValue={props.totalValue}
                        porfolioFiatValues={props.porfolioFiatValues}
                        portfolioByCoins={props.portfolioByCoins}          
                        />
                    </div>
                    <div label="by wallets">
                        <PortfolioByWallet
                        defaultFiatCurrency={props.defaultFiatCurrency} 
                        portfolioUrl={props.portfolioUrl} 
                        porfolioFiatValues={props.porfolioFiatValues}        
                        />
                    </div>
                </Tabs>
            </Div>
        )
    
}
