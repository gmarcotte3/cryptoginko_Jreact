import React, { Component } from 'react'
import styled from 'styled-components'

// styled TD
const TD = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
`;

export default class FiatCurrency extends Component {

    currencyOptions = {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    }
    currencyOptionsJpy = {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0,
    }

    getPrice = () => { 
        return (
            this.props.coinValue.toLocaleString(undefined, this.currencyOptions)
        );
    }
    getPriceNZD = () => { 
        return (
            this.props.coinValue.toLocaleString(undefined, this.currencyOptions)
        );
    }
    getPriceJPY = () => { 
        return (
            this.props.coinValue.toLocaleString(undefined, this.currencyOptionsJpy)
        );
    }

    getValue = () => {
        if ( this.props.fiatCurrency === 'JPM') {
            return ( this.getPriceJPY() +" 万円");
        } else if (this.props.fiatCurrency === 'JPY') {
            return ( this.getPriceJPY() +" 円");
        } else {
            return ( "$" + this.getPrice() );
        }
    }
    render() {
        return (
            <tr>
                <TD>{this.getValue()}</TD>
                <TD>{this.props.fiatCurrency}</TD>
            </tr>
        )
    }
}
