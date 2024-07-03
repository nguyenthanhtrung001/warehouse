package com.example.productservice.service.impl;

import com.example.productservice.entity.Brand;
import com.example.productservice.entity.Image;
import com.example.productservice.repository.ImageRepository;
import com.example.productservice.service.IImageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class implImageService implements IImageService {
    ImageRepository imageRepository;

    public implImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    @Override
    public Image createImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public boolean updateImage(Image image, long id) {
        Optional<Image> imageOption= imageRepository.findByProductId(id);
        if(imageOption.isPresent()){
            Image brandUpdate= imageOption.get();
            brandUpdate.setLink(image.getLink());
            imageRepository.save(brandUpdate);
            return true;
        }else return false;
    }

    @Override
    public boolean deleteImage(Long id) {
        if (imageRepository.existsById(id)) {
            imageRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
