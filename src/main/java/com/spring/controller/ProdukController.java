package com.spring.controller;

import org.example.dto.Produk;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdukController {

    @GetMapping("/products")
    public ResponseEntity product(){
        Produk produk = new Produk();
        produk.setProdukName("product -1 ");
        produk.setQty(10);
        return ResponseEntity.ok().body(produk);
    }
}
