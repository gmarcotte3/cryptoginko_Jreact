# Blockhead - blockchain address tracker Back-end
This is a blockchain address checking program. It will track a number of public addresses from different
crypto currencies.
This will provide REST apis to store and track addresses to track a portfoilo value. and with wallet
transaction csv dumps can be used to track profit and loss.

## Test with swaggar interface
http://localhost:8082/blockhead/swagger-ui.html

## Database connection
Access to the h2 database can be had from a console:

Open a browser and type 
```
http://localhost:8082/blockhead/h2
```


## External rest interfaces
### blockcypher explorer service

https://api.blockcypher.com/v1/{cryptoSymbol}/main/addrs/{address}/coin_balance

### zcash explorer service
https://api.zcha.in/v2/mainnet/accounts/{addresse}


### bitcoin cash
https://api.blockchair.com/bitcoin-cash/dashboards/address/{address}

#### example 
```
{
  data: 
  [
    {
      sum_value: "109374",
      sum_value_usd: 0.7162,
      sum_spending_value_usd: 0,
      max_time_receiving: "2016-10-25 08:36:20",
      max_time_spending: "0000-00-00 00:00:00",
      min_time_receiving: "2016-10-25 08:36:20",
      count_total: "1",
      rate: 0,
      sum_value_unspent: "109374",
      sum_value_unspent_usd: 0.7162,
      plu_usd: -0.7162,
      count_unspent: "1",
      min_time_spending: "0000-00-00 00:00:00",
      pl_usd: 0,
      receiving_activity: 
      [
        {
          year: 2016,
          month: 10,
          value: "1",
        }
      ],
      spending_activity: [ ],
    }
  ],
  rows: 1,
  limit: 1,
  time: 2.2496650218964,
  cache: -2,
  source: "AW",
}
```
### Dash - insite
Rest interface to dash block chain. opensource and free
```aidl
http://insight.masternode.io:3000/api/addr/${ashaddress}?noTxList=1
```
results in:
```aidl
  {
      "addrStr": "XbzmNWPE8xxxxxxxxxxxxxxxxxxxxxxxxx",
      "coin_balance": 0.5,
      "balanceSat": 50000000,
      "totalReceived": 0.5,
      "totalReceivedSat": 50000000,
      "totalSent": 0,
      "totalSentSat": 0,
      "unconfirmedBalance": 0,
      "unconfirmedBalanceSat": 0,
      "unconfirmedTxApperances": 0,
      "txApperances": 1,
      "transactions": [  
      ]
  }
```

# Updates
##2020.04.27
added Csv report output. all addresses sorted by currency, walletname
      csv report output. summary of address grouped by currency, walletname

##2020.04.18
fixed bug with wallet management. added summary of blockstoreaddress by wallet balances

##2020.02.29
added support for exodus address csv dump input. can now read a csv file of exodus wallet addresses and import them into
the database. new addresses are added, existing address are updated.

##2019.12.15
added history tracking balances of coin address to the blockstore. when updating the balance of a coin address if the balance changes
a new record will be crated, old record update to point to the new record. 

if the balance does not change but the blockchain has been checked the date will be updated
##2019.10.25
Initial version. 
At this point we only have the back-end started. Back enbd is accessable via Swagger interface to REST API

The REST currently provides address get/put
Portfoio check with using database information or update database information using external block-explorers.

Current version saves address but does not save portfoil history. 
