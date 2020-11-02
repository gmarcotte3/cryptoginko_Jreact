import React, {useState, useEffect} from 'react'
//import Tabs from  "../Tabs/Tabs"; 
import FiatCurrency from "../FiatCurrency/FiatCurrency";
import axios from 'axios';

const BACKEND_URL = "http://localhost:8082/blockhead/portfolio";
const PORTFOLIO_DETAIL_BY_COIN = "http://localhost:8082/blockhead/portfolio/history/detail/";

export default function Portfolio(props) {

    const [totalValue, setTotalValue] = React.useState(-1);
    const [porfolioFiatValues, setPortfolioFiatValues] = React.useState([]);

    const componentDidMount = async () => {
        let url = PORTFOLIO_DETAIL_BY_COIN + props.defaultFiatCurrency;
        console.log("url", url);
        let response = await axios.get(url);
        let updatedPorfolioFiatValues = response.data;
        console.log("porfolioFiatValues", updatedPorfolioFiatValues);

        let totalValue = 0;
        for( let i =0; i< updatedPorfolioFiatValues.length; i++) {
            totalValue += updatedPorfolioFiatValues[i].coinValue;
        }
        console.log("total Value=", totalValue);
        setPortfolioFiatValues(updatedPorfolioFiatValues);
        setTotalValue(totalValue);
    }

    useEffect( function() {
        if ( totalValue === -1)
        {
            componentDidMount();
        }
    })

    /*
    {
     porfolioFiatValues.map( ({coinName, coinValue, fiatCurrency, coinBalance}) =>
                            <FiatCurrency key={fiatCurrency}  
                                fiatCurrency={fiatCurrency}
                                coinValue={coinValue}
                                />
                            )
    }
    */

    return (
        <div>
            <div label="overall">
                <h1>Protfolio total value</h1>
                <p>
                show the Protfolio current total value and broken down
                by coin.</p>
                <table>
                    <thead>
                        <tr>
                            <th>coin</th>
                            <th>coin balance</th>
                            <th>fiat value</th>
                        </tr>
                    </thead>
                    <tbody>
                        <td>{totalValue}</td>
                    </tbody>
                </table>
            </div>
        </div>
    )
    
}
