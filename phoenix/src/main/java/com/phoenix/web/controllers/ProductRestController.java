package com.phoenix.web.controllers;

import com.github.fge.jsonpatch.JsonPatch;
import com.phoenix.data.dto.ProductDto;
import com.phoenix.data.models.Product;
import com.phoenix.service.product.ProductService;
import com.phoenix.web.exceptions.BusinessLogicException;
import com.phoenix.web.exceptions.ProductDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {


    @Autowired
    ProductService productService;

    @GetMapping()
    public ResponseEntity<?> findAllProducts(){
        List<Product> productList = productService.getAllProducts();
        return ResponseEntity.ok().body(productList);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createProducts(@ModelAttribute ProductDto productDto) throws BusinessLogicException {

        //set multipart to product Dto
//        if (productImage != null){
//            productDto.setImage(productImage);
        //}

        try {
            Product savedProduct = productService.createProduct(productDto);
            return ResponseEntity.ok().body(savedProduct);
        } catch (BusinessLogicException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public  ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody JsonPatch productPatch) throws BusinessLogicException {

        try{
            Product updatedProduct = productService.updateProduct(id, productPatch);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        }catch (BusinessLogicException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
