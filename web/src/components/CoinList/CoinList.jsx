import React from 'react';
import Coin from '../Coin/Coin';
import styled from 'styled-components'
import axios from 'axios';


const Table = styled.table`
    border: 1px solid #2c2b2b;
    width : 90%;

`;

const DivPage = styled.div`
position: relative;
    top: -20px;
    background: transparent linear-gradient(180deg, #393939 0%, #7A7A7A 100%) 0% 0% no-repeat padding-box;
`;

// Styled button
const Button = styled.button`
    font-size: 11px;
    width: 64px;
    margin: 3px 5px 0;
    vertical-align: middle;
`;

const CURRENT_COIN_PRICE_URL = "http://localhost:8082/ginkoJ/currency/coingeko/quote/all";
export default function CoinList(props) {

    const handleRefresh = async (event) => {
        event.preventDefault();
        await axios.put(CURRENT_COIN_PRICE_URL);
        props.handleRefreshCoinPrices();
    }


    // render as a table of coins
    return (
        <DivPage label="current prices">
            <Button className="btn btn-info" onClick={handleRefresh}>Refresh</Button>
            <Table >
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Ticker</th>
                        <th>Price USD</th>
                        <th>Price NZD</th>
                        <th>Price JPY</th>
                    </tr>
                </thead>
                <tbody>
                    {
                        // TODO need to future proof this call, pass all the props
                        props.coinData.map( ({coinName, ticker, priceNZD, priceUSD, priceJPY }) =>
                        <Coin key={ticker} 
                            coinName={coinName} 
                            ticker={ticker} 
                            priceNZD={priceNZD} 
                            priceUSD={priceUSD}
                            priceJPY={priceJPY}
                            />
                        )  
                    }
                </tbody>
            </Table>
        </DivPage>
    )
}