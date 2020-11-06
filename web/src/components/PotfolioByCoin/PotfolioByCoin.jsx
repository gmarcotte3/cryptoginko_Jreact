import React from 'react'
import styled from 'styled-components'
import FiatCurrency from "../FiatCurrency/FiatCurrency";

// styled TD
const TD = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
`;


/**
 * this component displays one line from the PortfolioByCoin row. 
 * it displays the coin, coin balance, current price, coins value.
 * 
 */
export default function PotfolioByCoin(props) {

    console.log( "defaultFiatCurrency",props.defaultFiatCurrency)

    function findcode(item) {
        if (item.code === props.defaultFiatCurrency )
        return item;
    }
   
    let price = props.fiat_prices.find(findcode);
    let coinvalue = props.fiat_balances.find(findcode);

    return (
            <tr><TD>{props.coinName}</TD>
                <TD>{props.ticker}</TD>
                <TD>{props.coinBalance}</TD>
                <FiatCurrency   key={"price" + props.ticker}
                                currency={price.code}
                                coinValue={price.value}
                                symbol={price.symbol}
                    />
                <FiatCurrency   key={"value" + props.ticker}
                                currency={coinvalue.code}
                                coinValue={coinvalue.value}
                                symbol={coinvalue.symbol}
                    />
                
            </tr>
    );
}
