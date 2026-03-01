package com.production.backend.service;

import com.production.backend.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConflictService {

    public List<List<Product>> buildConflictGroups(List<Product> products) {

        Map<Product, List<Product>> adjacency = new HashMap<>();

        for (Product p : products) {
            adjacency.put(p, new ArrayList<>());
        }

        for (int i = 0; i < products.size(); i++) {
            for (int j = i + 1; j < products.size(); j++) {

                Product p1 = products.get(i);
                Product p2 = products.get(j);

                if (shareResource(p1, p2)) {
                    adjacency.get(p1).add(p2);
                    adjacency.get(p2).add(p1);
                }
            }
        }

        return findConnectedComponents(adjacency);
    }

    public List<List<Product>> findConnectedComponents(
            Map<Product, List<Product>> adjacency) {

        Set<Product> visited = new HashSet<>();
        List<List<Product>> groups = new ArrayList<>();

        for (Product product : adjacency.keySet()) {

            if (!visited.contains(product)) {

                List<Product> group = new ArrayList<>();
                dfs(product, adjacency, visited, group);
                groups.add(group);
            }
        }

        return groups;
    }

    public void dfs(Product current,
                     Map<Product, List<Product>> adjacency,
                     Set<Product> visited,
                     List<Product> group) {

        visited.add(current);
        group.add(current);

        for (Product neighbor : adjacency.get(current)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, adjacency, visited, group);
            }
        }
    }

    private boolean shareResource(Product p1, Product p2) {

        Set<Long> r1 = p1.getCompositions()
                .stream()
                .map(c -> c.getResource().getCode())
                .collect(Collectors.toSet());

        return p2.getCompositions()
                .stream()
                .map(c -> c.getResource().getCode())
                .anyMatch(r1::contains);
    }
}
