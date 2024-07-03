package com.example.productservice.service;

import com.example.productservice.entity.Image;

import java.util.List;

public interface  IImageService {
    List<Image> getAllImages();
    Image getImageById(Long id);
    Image  createImage(Image image);
    boolean updateImage(Image image, long id);
    boolean deleteImage(Long id);


}
