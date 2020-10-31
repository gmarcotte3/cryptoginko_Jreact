import React from 'react'
import styled from 'styled-components'

// styled TD
const TD = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
`;

export default function CoinAddress(props) {
    return (
        <tr>
            <TD>{props.address}</TD>
            <TD>{props.walletName}</TD>
            <TD>{props.currency}</TD>
            <TD>{props.lastBalance}</TD>
            <TD>{props.lastUpdated}</TD>
            <TD>{props.message}</TD>
        </tr>
    )
}
