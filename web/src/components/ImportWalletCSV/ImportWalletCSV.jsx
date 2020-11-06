import React, { Component } from 'react'
import Tabs from  "../Tabs/Tabs"; 
import ImportGinkoAddressCSV from "../ImportGinkoAddressCSV/ImportGinkoAddressCSV";

const IMPORT_URL = "http://localhost:8082/ginkoJ/import/addresses?filename=%2Fhome%2Fgmarcotte%2FDesktop%2Fblockhead%2FpublicAddresses_test.csv";
export default class ImportWalletCSV extends Component {
    render() {
        return (
            <div>
                <Tabs>
                    <div label="Import Exodus">
                    <h1>Import address csv file from the Exosus wallet</h1>
                        <p>
                        This is where you can inport address export from exodus into this program. 
                        this will create/update coin address balance in the database so a update
                        portfoilo value can be calculated.</p>
                        <p>
                        coming to a screen near you</p>
                    </div>

                    <div label="Import Ginko">
                        <ImportGinkoAddressCSV 
                          importGinkoAddrURL={IMPORT_URL}/>
                        
                    </div>
                    
                    <div label="Export Ginko">
                        <h1>Export an ginko address csv file</h1>
                        <p>
                        Export the addresses from this program to create a backup of the coin 
                        addresses with balances</p>
                        <p>
                        coming to a screen near you</p>
                    </div>
                </Tabs>
            </div>
        )
    }
}
