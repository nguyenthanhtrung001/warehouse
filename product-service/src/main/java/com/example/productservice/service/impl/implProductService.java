package com.example.productservice.service.impl;

import com.example.productservice.client.InventoryClient;
import com.example.productservice.client.InvoiceClient;
import com.example.productservice.client.ReceiptClient;
import com.example.productservice.dao.request.ProductRequest;
import com.example.productservice.dao.response.BatchLocation;
import com.example.productservice.dao.response.ProductPropose;
import com.example.productservice.dao.response.ProductQuantity;
import com.example.productservice.dao.response.ProductResponse;
import com.example.productservice.entity.*;
import com.example.productservice.repository.ImageRepository;
import com.example.productservice.repository.PriceRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.IImageService;
import com.example.productservice.service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class implProductService implements IProductService {

    private ProductRepository productRepository;
    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private IImageService iImageService;
    @Autowired
    private InventoryClient inventoryClient;
    @Autowired
    private InvoiceClient invoiceClient;
    @Autowired
    private ReceiptClient receiptClient;
    private ModelMapper modelMapper  = new ModelMapper();
    public implProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findByStatus(1);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : productList)
        {
            ProductResponse response = modelMapper.map(product,ProductResponse.class);
            Price price = priceRepository.findFirstByProductIdOrderByEffectiveDateDesc(response.getId());
            if (price!=null) {
                response.setPrice(price.getPrice());
            }else response.setPrice(0l);
            try{
                Integer quantity = inventoryClient.getQuantityByProductId(product.getId());
                if (quantity!=null) response.setQuantity(quantity);
                else response.setQuantity(0);
            }catch (Exception e){
                e.printStackTrace();
            }

            List<String> images = imageRepository.findLinksByProductId(response.getId());
            if (images != null && !images.isEmpty()) {
                response.setImage(images.get(0));
            }
                productResponses.add(response);

        }
        return productResponses;
    }

    @Override
    public List<ProductResponse> getAllProductsHasLocationBatch() {
        List<Product> productList = productRepository.findByStatus(1);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : productList)
        {
            ProductResponse response = modelMapper.map(product,ProductResponse.class);
            Price price = priceRepository.findFirstByProductIdOrderByEffectiveDateDesc(response.getId());
            if (price!=null) {
                response.setPrice(price.getPrice());
            }else response.setPrice(0l);
            try{
                Integer quantity = inventoryClient.getQuantityByProductId(product.getId());
                if (quantity!=null) response.setQuantity(quantity);
                else response.setQuantity(0);
            }catch (Exception e){
                e.printStackTrace();
            }

            List<String> images = imageRepository.findLinksByProductId(response.getId());
            if (images != null && !images.isEmpty()) {
                response.setImage(images.get(0));
            }
            BatchLocation batchLocation = inventoryClient.getBatchLocationForProductId(response.getId());
            response.setBatchLocation(batchLocation);
            productResponses.add(response);

        }
        return productResponses;
    }

    @Override
    public List<ProductResponse> getTopLowestProduct(Integer top) {
        List<ProductResponse> productResponses = new ArrayList<>();
        try{
            List<ProductQuantity> productQuantitys = inventoryClient.getTopNLowestQuantity(top);
            for (ProductQuantity x : productQuantitys)
            {
                Product product = productRepository.findById(x.getProductId()).orElse(null);
                if (product!=null)
                {
                    ProductResponse response = modelMapper.map(product,ProductResponse.class);
                    response.setQuantity(Math.toIntExact(x.getQuantity()));
                    List<String> images = imageRepository.findLinksByProductId(response.getId());
                    if (images != null && !images.isEmpty()) {
                        response.setImage(images.get(0));
                    }
                    productResponses.add(response);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return productResponses;
    }

    @Override
    public List<ProductResponse> getNotifyTopLowestProduct(Integer quantity) {
        return getTopLowestProduct(10).stream()
                .filter(response -> response.getQuantity() <= quantity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getTopProductSale(Integer top) {
        List<ProductResponse> productResponses = new ArrayList<>();
        try{
            List<ProductQuantity> productQuantitys = invoiceClient.getTopProductSale(top);
            for (ProductQuantity x : productQuantitys)
            {
                Product product = productRepository.findById(x.getProductId()).orElse(null);
                if (product!=null)
                {
                    ProductResponse response = modelMapper.map(product,ProductResponse.class);
                    response.setQuantity(Math.toIntExact(x.getQuantity()));
                    Price price = priceRepository.findFirstByProductIdOrderByEffectiveDateDesc(response.getId());
                    if (price!=null) {
                        response.setPrice(price.getPrice());
                    }else response.setPrice(0l);
                    List<String> images = imageRepository.findLinksByProductId(response.getId());
                    if (images != null && !images.isEmpty()) {
                        response.setImage(images.get(0));
                    }
                    productResponses.add(response);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return productResponses;
    }

    @Override
    public List<ProductResponse> getProposeProduct() {
        List<ProductResponse> responses = new ArrayList<>();

        List<ProductPropose> productProposes  = new ArrayList<>();

        List<Product> productList = productRepository.findByStatus(1);
        for ( Product product : productList){
            ProductPropose productPropose = new ProductPropose();
            productPropose.setProductId(product.getId());
            try{
                ProductQuantity productQuantitys = invoiceClient.getQuantityProductSaleThreeMonth(product.getId());
                if (productQuantitys != null){
                    productPropose.setQuantitySale(productQuantitys.getQuantity());
                }else productPropose.setQuantitySale(0L);
                Integer quantityInventory = inventoryClient.getQuantityByProductId(product.getId());

                if( quantityInventory != null){
                    productPropose.setQuantityInventory(Long.valueOf(quantityInventory));
                }else productPropose.setQuantityInventory(0L);
                productProposes.add(productPropose);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        List<ProductPropose> proposes = getTop5Products(productProposes);

        for ( ProductPropose propose :proposes){
            Product product = productRepository.findById(propose.getProductId()).orElse(null);

            if (product!=null)
            {
                ProductResponse response = modelMapper.map(product,ProductResponse.class);
                response.setQuantity(Math.toIntExact(propose.getQuantityInventory()));

                Price price = priceRepository.findFirstByProductIdOrderByEffectiveDateDesc(response.getId());
                if (price!=null) {
                    response.setPrice(price.getPrice());
                }else response.setPrice(0l);

                List<String> images = imageRepository.findLinksByProductId(response.getId());
                if (images != null && !images.isEmpty()) {
                    response.setImage(images.get(0));
                }
                responses.add(response);
            }
        }


        return responses;
    }

    @Override
    public List<ProductResponse> getExpiredProduct() {
        List<Long> ListProductID = inventoryClient.getExpiredProductIds();
        System.out.println("SLllll:"+ ListProductID.size());
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Long id : ListProductID)
        {
            if (id ==null) continue;
            Product product = productRepository.findById(id).orElse(null);
            ProductResponse response = modelMapper.map(product,ProductResponse.class);
            Price price = priceRepository.findFirstByProductIdOrderByEffectiveDateDesc(response.getId());
            if (price!=null) {
                response.setPrice(price.getPrice());
            }else response.setPrice(0l);
            try{
                Integer quantity = inventoryClient.getQuantityByProductId(product.getId());
                if (quantity!=null) response.setQuantity(quantity);
                else response.setQuantity(0);
            }catch (Exception e){
                e.printStackTrace();
            }

            List<String> images = imageRepository.findLinksByProductId(response.getId());
            if (images != null && !images.isEmpty()) {
                response.setImage(images.get(0));
            }
            productResponses.add(response);

        }
        return productResponses;
    }

    public List<ProductPropose> getTop5Products(List<ProductPropose> products) {
        // Sắp xếp danh sách sản phẩm theo số lượng tồn kho tăng dần và số lượng bán ra giảm dần
        products.sort(Comparator.comparingLong(ProductPropose::getQuantityInventory)
                .thenComparing(Comparator.comparingLong(ProductPropose::getQuantitySale).reversed()));

        // Lấy 5 sản phẩm đầu tiên từ danh sách đã sắp xếp
        List<ProductPropose> top5Products = new ArrayList<>();
        for (int i = 0; i < Math.min(5, products.size()); i++) {
            top5Products.add(products.get(i));
        }

        return top5Products;
    }

    @Override
    public Integer getProductCount() {
        List<Product> products = productRepository.findAll();
        return products.size();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public String getNameProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product!=null){
            return product.getProductName();
        }
        return null;
    }

    @Override
    public boolean updateProduct(Product product, Long id) {
        Optional<Product> productOption= productRepository.findById(id);
        if(productOption.isPresent()){
            Product productUpdate= productOption.get();
            productUpdate.setProductName(product.getProductName());
            productUpdate.setDescription(product.getDescription());
            productUpdate.setStatus(product.getStatus());
            productUpdate.setWeight(product.getWeight());

            productUpdate.setBrand(product.getBrand());
            productUpdate.setProductGroup(product.getProductGroup());

            productRepository.save(productUpdate);
            return true;
        }else return false;
    }

    @Transactional
    public Product createProduct(ProductRequest product) {

        Product productNew = new Product();
        productNew.setProductName(product.getProductName());
        productNew.setProductGroup(new ProductGroup(product.getProductGroup()));
        productNew.setBrand( new Brand(product.getBrand()));
        productNew.setWeight(product.getWeight());
        productNew.setDescription(product.getDescription());
        productNew.setStatus(1);
        Product productSave = productRepository.save(productNew);

        Price price = new Price();
        price.setPrice(product.getPrices()==null?product.getPrices():0);
        price.setProduct(productSave);
      //  price.setEmployeeId(product.getEmployeeId());
        price.setEmployeeId(1L);
        LocalDateTime now = LocalDateTime.now();
        price.setEffectiveDate(now);
        priceRepository.save(price);
        Image image = new Image();
        image.setLink(product.getImages());
        image.setProduct(productSave);
        imageRepository.save(image);
        System.out.println("Gia: "+product.getPrices());
        System.out.println("anh: "+product.getImages());

        return productSave;
    }

    @Transactional
    public Product updateProduct(ProductRequest product) {
        Product pro = productRepository.findById(product.getId()).orElse(null);
        if (pro !=null ){
            pro.setProductName(product.getProductName());
            pro.setWeight(product.getWeight());
            pro.setDescription(product.getDescription());
            if ( product.getProductGroup() > 0) {
                ProductGroup productGroup = new ProductGroup();
                productGroup.setId(product.getProductGroup());
                pro.setProductGroup(productGroup);
            }
            if ( product.getBrand() > 0) {
                Brand brand = new Brand();
                brand.setId(product.getBrand());
                pro.setBrand(brand);
            }
            return productRepository.save(pro);
        }
        return null;
    }

    @Override
    public boolean deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
    @Transactional
    public void DeleteProductWithStatus(Long productId) {
        boolean check = false;
        try {
            check =receiptClient.checkProductIdExists(productId);

        }catch (Exception e){
            e.printStackTrace();
        }
        if (!check) {
            try{
                iImageService.deleteImagesByProductId(productId);
                priceRepository.deleteByProduct_Id(productId);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            deleteProduct(productId);
            return;
        }

        int status =0;
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            // Cập nhật trạng thái của sản phẩm
            product.setStatus(status);
            // Lưu lại thay đổi
            productRepository.save(product);
        } else {
            // Xử lý khi không tìm thấy sản phẩm
            throw new EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + productId);
        }
    }
}
