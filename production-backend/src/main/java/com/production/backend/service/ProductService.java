package com.production.backend.service;

import com.production.backend.dto.CompositionDTO;
import com.production.backend.dto.ProductDTO;
import com.production.backend.entity.Composition;
import com.production.backend.entity.Product;
import com.production.backend.entity.Resource;
import com.production.backend.mapper.ProductMapper;
import com.production.backend.repository.ProductRepository;
import com.production.backend.response.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ResourceService resourceService;
    private final ProductMapper mapper;

    @Transactional
    public ProductResponse create(ProductDTO productDTO) {

        Product product = mapper.toEntity(productDTO);

        if (productDTO.getCompositions() != null) {

            for (CompositionDTO compositionDTO : productDTO.getCompositions()) {
                Resource resource = resourceService.findEntityById(compositionDTO.getResourceId());

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

    @Transactional
    public ProductResponse update(Integer id, ProductDTO dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setName(dto.getName());
        product.setValue(dto.getValue());
        product.getCompositions().clear();

        for (CompositionDTO compositionDTO : dto.getCompositions()) {
            Resource resource = resourceService.findEntityById(compositionDTO.getResourceId());

            Composition composition = new Composition();
            composition.setProduct(product);
            composition.setResource(resource);
            composition.setQtd(compositionDTO.getQtdResource());

            product.getCompositions().add(composition);
        }

        return mapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        List<Product> products = productRepository.findAllWithResources();

        return products.stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Integer id) {

        Product product = productRepository.findByIdWithResources(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Product not found"));

        return mapper.toResponse(product);
    }
}
