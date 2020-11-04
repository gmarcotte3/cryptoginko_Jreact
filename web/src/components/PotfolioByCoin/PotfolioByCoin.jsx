import React from 'react'
import styled from 'styled-components'

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

    console.log("props=", props);

    return (
            <tr><TD>{props.coinName}</TD><TD>{props.ticker}</TD><TD>{props.coinBalance}</TD></tr>
    );
}