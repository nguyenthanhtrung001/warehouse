package com.example.productservice.service;

import com.example.productservice.dao.request.ProductRequest;
import com.example.productservice.dao.response.ProductResponse;
import com.example.productservice.entity.Product;

import java.util.List;

public interface IProductService {
    public List<ProductResponse> getAllProducts();
    public List<ProductResponse> getAllProductsInWarehouse( Long wareHouseId);
    public List<ProductResponse> getAllProductsHasLocationBatch(Long wareHouseId);
    public List<ProductResponse> getTopLowestProduct(Integer top, Long warehouseId );
    public List<ProductResponse> getNotifyTopLowestProduct(Integer quantity, Long wareHouseID);


    public List<ProductResponse> getTopProductSale(Integer top, Long wareHouseId);
    public List<ProductResponse> getProposeProduct();
    public List<ProductResponse> getExpiredProduct(Long warehouseId);
    public Integer getProductCount();
    public Product getProductById(Long id);
    public ProductResponse getProductQuantityById(Long id, Long warehouseId);
    public String getNameProductById(Long id);
    public Product createProduct(ProductRequest product);
    public Product updateProduct(ProductRequest product);
    public boolean updateProduct(Product product, Long id);
    public boolean deleteProduct(Long id);
    public void DeleteProductWithStatus(Long productId);

}
