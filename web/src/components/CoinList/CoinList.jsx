import React from 'react';
import Coin from '../Coin/Coin';
import styled from 'styled-components'

const Table = styled.table`
    font-size: 1rem;
  }
`;

const Div = styled.div`
    background: #2B2B2B 0% 0% no-repeat padding-box;
    //background: transparent linear-gradient(180deg, #393939 0%, #7A7A7A 100%) 0% 0% no-repeat padding-box;
    opacity: 1;
    height: 44px
`;


export default function CoinList(props) {
    // render as a table of coins

        return (
            <Div label="current prices">
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
                          props.coinData.map( ({name, ticker, price, nzd, usd, jpy }) =>
                            <Coin key={ticker} 
                                name={name} 
                                ticker={ticker} 
                                price={price} 
                                nzd={nzd} 
                                usd={usd}
                                jpy={jpy}
                                />
                            )  
                        }
                    </tbody>
                </Table>
            </Div>
        )
}