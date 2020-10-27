import React, { Component } from 'react'
import styled from 'styled-components'
import axios from 'axios';

// Styled button
const Button = styled.button`
    border: 3px solid blue;
    background-color: black;
    color:white;
    border-radius: 10px;
    font-size: 18px;
    text-align: center;
    width: 150px;
`;

// http://localhost:8082/blockhead/import/addresses
const IMPORT_GINKO_ADDR_URL = "http://localhost:8082/blockhead/import/addresses?filename=%2Fhome%2Fgmarcotte%2FDesktop%2Fblockhead%2FpublicAddresses_test.csv";
export default class ImportGinkoAddressCSV extends Component {
    handleClick = async (event) => {
        event.preventDefault();
        let URL = "http://localhost:8082/blockhead/import/addresses?filename=%2Fhome%2Fgmarcotte%2FDesktop%2Fblockhead%2FpublicAddresses_test.csv";
        let filename=document.getElementById('theFileName').value;
        let fullPathFilename=document.getElementById('theFullPathFilename').value;
        console.log("filename=",filename);
        console.log("fullpath=",fullPathFilename);
        let response = await axios.put(IMPORT_GINKO_ADDR_URL);
        console.log(response);
    }





    render() {
        return (
            <div>
                <h1>Import Ginko address csv file</h1>
                <p>
                This is where you can inport address export from this ginko portfolio management
                program. This will create/update coin address balance in the database so a update
                portfoilo value can be calculated.</p>
                <div>
                <table>
                    <tbody>

                    
                        <tr>
                            <td><input id="theFileName" type="file" name="csvfile" required /> </td>
                        </tr>
                        <tr>
                            <tr><input id="theFullPathFilename" type="text" name="fullfilepath" required /></tr>
                        </tr>
                        <tr>
                            <td><Button onClick={this.handleClick}>Upload</Button></td>
                        </tr>
                    </tbody>
                </table>
                </div>
            </div>
        )
    }
}
