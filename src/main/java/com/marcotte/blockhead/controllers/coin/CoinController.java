package com.marcotte.blockhead.controllers.coin;

import com.marcotte.blockhead.datastore.coin.Coin;
import com.marcotte.blockhead.services.coin.CoinService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Coin Rest API", tags = "coin")
@RestController
@RequestMapping("/coin")
public class CoinController {
    @Autowired
    private CoinService coinService;

    @GetMapping("/")
    public ResponseEntity<List<Coin>> getAllCoins() {
        List<Coin> result = coinService.findAll();
        return new ResponseEntity<List<Coin>>(result, HttpStatus.OK);
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<List<Coin>> getCoinByTicker(@PathVariable String ticker) {
        List<Coin> result = coinService.findByTicker(ticker);
        return new ResponseEntity<List<Coin>>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete/all")
    public @ResponseBody  ResponseEntity<String> deleteAddresses() throws Exception
    {
        coinService.deleteAll();
        return new ResponseEntity<String>("Coin have been cleared", HttpStatus.OK);
    }
}
