package com.production.backend.service;

import com.production.backend.entity.Product;
import com.production.backend.entity.Resource;
import com.production.backend.entity.Composition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ConflictServiceTest {

    private ConflictService conflictService;

    @BeforeEach
    void setup() {
        conflictService = new ConflictService();
    }

    @Test
    void shouldGroupProductsThatShareResources() {

        Resource flour = new Resource();
        flour.setCode(1L);

        Product p1 = new Product();
        p1.setCode(1L);
        p1.setCompositions(List.of(createComposition(flour)));

        Product p2 = new Product();
        p2.setCode(2L);
        p2.setCompositions(List.of(createComposition(flour)));

        List<Product> products = List.of(p1, p2);

        List<List<Product>> groups =
                conflictService.buildConflictGroups(products);

        assertEquals(1, groups.size());
        assertEquals(2, groups.get(0).size());
    }

    @Test
    void shouldSeparateProductsWithoutSharedResources() {

        Resource flour = new Resource();
        flour.setCode(1L);

        Resource sugar = new Resource();
        sugar.setCode(2L);

        Product p1 = new Product();
        p1.setCode(1L);
        p1.setCompositions(List.of(createComposition(flour)));

        Product p2 = new Product();
        p2.setCode(2L);
        p2.setCompositions(List.of(createComposition(sugar)));

        List<Product> products = List.of(p1, p2);

        List<List<Product>> groups =
                conflictService.buildConflictGroups(products);

        assertEquals(2, groups.size());
    }

    @Test
    void findConnectedComponentsShouldReturnSingleGroup() {

        Product p1 = new Product();
        Product p2 = new Product();

        Map<Product, List<Product>> adjacency = new HashMap<>();
        adjacency.put(p1, List.of(p2));
        adjacency.put(p2, List.of(p1));

        List<List<Product>> groups =
                conflictService.findConnectedComponents(adjacency);

        assertEquals(1, groups.size());
        assertEquals(2, groups.get(0).size());
    }

    @Test
    void dfsShouldVisitAllConnectedNodes() {

        Product p1 = new Product();
        Product p2 = new Product();

        Map<Product, List<Product>> adjacency = new HashMap<>();
        adjacency.put(p1, List.of(p2));
        adjacency.put(p2, List.of(p1));

        Set<Product> visited = new HashSet<>();
        List<Product> group = new ArrayList<>();

        conflictService.dfs(p1, adjacency, visited, group);

        assertTrue(visited.contains(p1));
        assertTrue(visited.contains(p2));
        assertEquals(2, group.size());
    }

    private Composition createComposition(Resource resource) {

        Composition comp = new Composition();
        comp.setResource(resource);
        comp.setQtd(1);
        return comp;
    }
}