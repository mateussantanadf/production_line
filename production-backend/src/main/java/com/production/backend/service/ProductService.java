package com.production.backend.service;

import com.production.backend.dto.CompositionDTO;
import com.production.backend.dto.ProductDTO;
import com.production.backend.entity.Composition;
import com.production.backend.entity.Product;
import com.production.backend.entity.Resource;
import com.production.backend.mapper.ProductMapper;
import com.production.backend.repository.ProductRepository;
import com.production.backend.repository.ResourceRepository;
import com.production.backend.response.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ResourceRepository resourceRepository;
    private final ProductMapper mapper;

    @Transactional
    public ProductResponse create(ProductDTO productDTO) {

        Product product = mapper.toEntity(productDTO);

        if (productDTO.getCompositions() != null) {

            for (CompositionDTO compositionDTO : productDTO.getCompositions()) {
                Resource resource = resourceRepository.findById(compositionDTO.getResourceId())
                        .orElseThrow(() ->
                                new EntityNotFoundException("Resource not found: " + compositionDTO.getResourceId()));

                Composition composition = new Composition();
                composition.setProduct(product);
                composition.setResource(resource);
                composition.setQtd(compositionDTO.getQtdResource());

                product.getCompositions().add(composition);
            }
        }

        Product saved = productRepository.save(product);
        return mapper.toResponse(saved);
    }
}
