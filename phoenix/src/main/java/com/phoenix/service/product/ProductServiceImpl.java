package com.phoenix.service.product;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.phoenix.data.dto.ProductDto;
import com.phoenix.data.models.Product;
import com.phoenix.data.repository.ProductRepository;
import com.phoenix.service.cloud.CloudinaryService;
import com.phoenix.web.exceptions.BusinessLogicException;
import com.phoenix.web.exceptions.ProductDoesNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
    
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CloudinaryService cloudinaryService;
    
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findProductById(Long productId) throws ProductDoesNotExistException{
        if (productId == null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        Optional<Product> queryResult = productRepository.findById(productId);
        if (queryResult.isPresent()){
            return queryResult.get();
        }
        throw new ProductDoesNotExistException("Product with ID:"+productId + ": does not exists");

    }

    @Override
    public Product createProduct(ProductDto productDto) throws BusinessLogicException {
        if (productDto == null){
            throw new IllegalArgumentException("Argument cannot be null");
        }

        Optional<Product> query = productRepository.findByName(productDto.getName());
        if (query.isPresent()){
            throw new BusinessLogicException("Product with name " + productDto.getName() + " already exists");
        }

        Product product = new Product();
        try {
            if (productDto.getImage() != null) {
                Map<?, ?> uploadResult = cloudinaryService.upload(productDto.getImage().getBytes(), ObjectUtils.asMap(
                        "public_id", "inventory/" + productDto.getImage().getOriginalFilename(),
                        "overwrite", true));
                product.setImageUrl(uploadResult.get("url").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setDescription(productDto.getDescription());

        return productRepository.save(product);
    }

    private Product saveOrUpdate(Product product) throws BusinessLogicException{
        if (product == null){
            throw new BusinessLogicException("Product cannot be null");
        }
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, JsonPatch productPatch) throws BusinessLogicException {
        Optional<Product> productQuery = productRepository.findById(productId);
        if (productQuery.isEmpty()){
            throw new BusinessLogicException("Product with ID" +productId+ " does not exist");
        }
        Product targetProduct = productQuery.get();

        try{
            targetProduct = applyPatchToProduct(productPatch, targetProduct);
            log.info("Product after patch -> {}", targetProduct);
            return saveOrUpdate(targetProduct);
        } catch (JsonPatchException | JsonProcessingException | BusinessLogicException je) {
            throw new BusinessLogicException("Update Failed");
        }

        }


    private Product applyPatchToProduct(JsonPatch productPatch, Product targetProduct) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = productPatch.apply(objectMapper.convertValue(targetProduct, JsonNode.class));
        return objectMapper.treeToValue(patched, Product.class);
    }

    }

//    @Override
//    public Product updateProduct(Long productId, ProductDto productDto) throws ProductDoesNotExistException {
//        if (productRepository.findById(productId).isPresent()){
//            Product foundProduct = productRepository.findById(productId).get();
//
//            foundProduct.setPrice(productDto.getPrice());
//            foundProduct.setName(productDto.getName());
//            foundProduct.setQuantity(productDto.getQuantity());
//            foundProduct.setDescription(productDto.getDescription());
//
//            Product updatedProduct;
//            updatedProduct = productRepository.save(foundProduct);
//            return updatedProduct;
//        }else{
//            return null;
//        }
//
//    }



