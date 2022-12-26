package com.spring.controller;

import org.example.dto.Produk;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProdukController {

    @GetMapping("/products")
    public ResponseEntity product(){
        Optional<Produk> produk = getProduk();
        if (!produk.isPresent()) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("ResponseCode", 1);
            response.put("ResponseMessage", "Failed");
            response.put("ResponseDescriptions", "Data tidak Ditemukan");
            return ResponseEntity.ok().body(response);
        }

        Produk dataProduk = produk.get();

        return ResponseEntity.ok().body(dataProduk);
    }

    private Optional<Produk> getProduk(){
        Produk produk = new Produk();
        produk.setProdukName("product -1 ");
        produk.setQty(10);
        return Optional.of(produk);
    }
}
