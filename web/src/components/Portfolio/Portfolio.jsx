import React, {useState, useEffect} from 'react'
//import Tabs from  "../Tabs/Tabs"; 
import FiatCurrency from "../FiatCurrency/FiatCurrency";
import axios from 'axios';

const BACKEND_URL = "http://localhost:8082/blockhead/portfolio";

export default function Portfolio(props) {

    const [totalValue, setTotalValue] = React.useState(-1);
    const [porfolioFiatValues, setPortfolioFiatValues] = React.useState([]);

    const componentDidMount = async () => {
        let response = await axios.put(BACKEND_URL);
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
                                <th>value</th>
                                <th>Fiat</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                porfolioFiatValues.map( ({coinValue, currency}) =>
                                <FiatCurrency key={currency}
                                    currency={currency}
                                    coinValue={coinValue}
                                    />
                                )
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        )
    
}
