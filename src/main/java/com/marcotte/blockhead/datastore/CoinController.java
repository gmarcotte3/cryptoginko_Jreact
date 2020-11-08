package com.marcotte.blockhead.datastore;

import com.marcotte.blockhead.model.CoinDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
