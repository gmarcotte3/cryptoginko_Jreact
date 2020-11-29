import React from 'react'
import FiatCurrency from "../FiatCurrency/FiatCurrency";
import PotfolioByCoin from "../PotfolioByCoin/PotfolioByCoin";
import styled from 'styled-components'


// styled TD
const TDPrice = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
    text-align: right;
`;

// styled TD
const Table = styled.table`
    border: 1px solid #2c2b2b;
    width : 90%;

`;
// styled TD
const Div = styled.div`
margin-right:100px;
`;

const DivPage = styled.div`
position: relative;
    top: -20px;
    background: transparent linear-gradient(180deg, #393939 0%, #7A7A7A 100%) 0% 0% no-repeat padding-box;
`;

export default function PortfolioByCoins(props) {

    console.log("portfolioByCoins portfolioValue=", props.porfolioFiatValues);

    return (
        <DivPage> 
            <h1>Portfolio value</h1>
            <p></p>
            <table>
                <thead>
                    <tr>
                        <th>Total Portfolio Value</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <TDPrice><FiatCurrency key="USD"
                            currency="USD"
                            coinValue={props.porfolioFiatValues.priceUSD}
                            />
                        </TDPrice>
                        <TDPrice>
                            <FiatCurrency key="NZD"
                            currency="NZD"
                            coinValue={props.porfolioFiatValues.priceNZD}
                            />
                        </TDPrice>
                        <TDPrice>
                            <FiatCurrency key="JPM"
                            currency="JPM"
                            coinValue={props.porfolioFiatValues.priceJPM}
                            />
                        </TDPrice>
                        <TDPrice>
                            <FiatCurrency key="JPY"
                            currency="JPY"
                            coinValue={props.porfolioFiatValues.priceJPY}
                            />
                        </TDPrice>
                    </tr>
                </tbody>
            </table>
            <Div>
                <h3>list of coins balance and values</h3>
                <Table>
                    <thead>
                        <tr>
                            <th>Icon</th>
                            <th>coin name</th>
                            <th>Ticker</th>
                            <th>coin balance</th>
                            <th>Current Price</th>
                            <th>value</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                        props.portfolioByCoins.map( ({coinName, ticker, coinBalance, fiat_prices, fiat_balances }) =>
                        <PotfolioByCoin 
                                key={ticker}
                                coinName={coinName} 
                                ticker={ticker} 
                                coinBalance={coinBalance} 
                                fiat_prices={fiat_prices}
                                fiat_balances={fiat_balances}
                                defaultFiatCurrency={props.defaultFiatCurrency}
                                />
                            )  
                        }
                    </tbody>
                </Table>
            </Div>

        </DivPage>
    )

}
