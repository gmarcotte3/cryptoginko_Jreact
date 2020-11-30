
import React, {useState, useEffect} from 'react'
import PortfolioByWalletList from "../PortfolioByWalletList/PortfolioByWalletList";
import axios from 'axios';
import styled from 'styled-components'


const Table = styled.table`
    width : 100%
`;

const DivPage = styled.div`
position: relative;
    top: -20px;
    background: transparent linear-gradient(180deg, #393939 0%, #7A7A7A 100%) 0% 0% no-repeat padding-box;
`;


const PORTFOLIO_BYWALLET_URL = "http://localhost:8082/ginkoJ/portfolio/byWallet";

export default function PortfolioByWallet(props) {

    const [numberOfWallets, setNumberOfWallets] = useState(-1);
    const [portfolioByWallet, setPortfolioByWallet] = useState([]);

    const componentDidMount = async () => {
        let response2 = await axios.get(PORTFOLIO_BYWALLET_URL);
        let newPortfolioByWallet = response2.data;
        setPortfolioByWallet(newPortfolioByWallet);
    }

    useEffect( function() {
          if ( numberOfWallets < 0)
          {
            setNumberOfWallets(0);
            componentDidMount();
          }
      })

    return (
        <DivPage> 
            
        
            <h3>list of wallet coins balance and values</h3>
            <Table>
                <thead>
                    <tr>
                        <th>Wallet</th>
                        <th>Coins</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        portfolioByWallet.map( ({walletName, coinDTOs, valueNZD, valueJPY}) => 
                            <PortfolioByWalletList 
                            key={walletName}
                            defaultFiatCurrency={props.defaultFiatCurrency}
                            walletName={walletName} portfolioByCoins={coinDTOs}
                            valueNZD={valueNZD} valueJPY={valueJPY}/>
                        )
                    }
                </tbody>
            </Table>

        </DivPage>
    )

}
