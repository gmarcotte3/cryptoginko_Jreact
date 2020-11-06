# CryptoGinko - blockchain address tracker Back-end with React front end
This is a blockchain address checking program. It will track a number of public addresses from different
crypto currencies.
This will provide REST apis to store and track addresses to track a portfoilo value. and with wallet
transaction csv dumps can be used to track profit and loss.

## Test with swaggar interface
http://localhost:8082/ginkoJ/swagger-ui.html

## Database connection
Access to the h2 database can be had from a console:

Open a browser and type 
```
http://localhost:8082/ginkoJ/h2
```
## Front end
see ./web/reademe.md for react front end

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


