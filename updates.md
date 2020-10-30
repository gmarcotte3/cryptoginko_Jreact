# Updates
##2020-10-30
moved repo back to github. added react front end.

Just started working on the react front end so lots of work to do.

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