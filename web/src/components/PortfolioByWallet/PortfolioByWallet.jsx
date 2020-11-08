
import React, {useState, useEffect} from 'react'
import FiatCurrency from "../FiatCurrency/FiatCurrency";
import PortfolioByWalletList from "../PortfolioByWalletList/PortfolioByWalletList";
import axios from 'axios';
import styled from 'styled-components'
import Tabs from  "../Tabs/Tabs";


// styled TD
const TD = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
`;
const PORTFOLIO_BYWALLET_URL = "http://localhost:8082/ginkoJ/portfolio/byWallet";

export default function PortfolioByWallet(props) {

    const [numberOfWallets, setNumberOfWallets] = React.useState(-1);
    const [portfolioByWallet, setPortfolioByWallet] = React.useState([]);

    const componentDidMount = async () => {
        let response2 = await axios.get(PORTFOLIO_BYWALLET_URL);
        let newPortfolioByWallet = response2.data;
        console.log("newPortfolioByWallet", newPortfolioByWallet);
        setPortfolioByWallet(newPortfolioByWallet);
    }

    useEffect( function() {
        console.log("..;");
          if ( numberOfWallets < 0)
          {
            setNumberOfWallets(0);
            console.log("refreshing now ..... ");
            componentDidMount();
          }
      })

    return (
        <div> 
            
        
            <h3>list of wallet coins balance and values</h3>
            <table>
                <thead>
                    <tr>
                        <th>Wallet</th>
                        <th>Coins</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        portfolioByWallet.map( ({walletName, coinDTOs}) => 
                            <PortfolioByWalletList 
                            key={walletName}
                            defaultFiatCurrency={props.defaultFiatCurrency}
                            walletName={walletName} portfolioByCoins={coinDTOs}/>
                        )
                    }
                </tbody>
            </table>

        </div>
    )

}