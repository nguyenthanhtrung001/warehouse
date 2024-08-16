package com.example.productservice.repository;

import com.example.productservice.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
//    Optional<Image> findByProductId(long id);
    List<Image> findByProductId(long id);
    @Query("SELECT i.link FROM Image i WHERE i.product.id = :productId")
    List<String> findLinksByProductId(@Param("productId") long productId);

    void deleteByProduct_Id(Long productId);
}
