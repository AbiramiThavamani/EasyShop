package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController {
    private final CategoryDao categoryDao;
    private final ProductDao productDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @GetMapping(path = "/categories")
    public List<Category> getAll() {
        try {
            return categoryDao.getAllCategories();

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong.", ex);

        }

    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Category> getById(@PathVariable int id) {
        Category category = categoryDao.getById(id);
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
        }
        return ResponseEntity.ok(category);
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping(path = "/{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        try {
            return productDao.listByCategoryId(categoryId);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong.", ex);
        }

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category addCategory(@RequestBody Category category) {
        try {
            return categoryDao.create(category);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong.", ex);

        }

    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        try {
            categoryDao.update(id, category);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong.", ex);
        }
    }

@DeleteMapping(path = "/{id}")
@ResponseStatus(value = HttpStatus.NO_CONTENT)
@PreAuthorize("hasRole('Role_ADMIN')")
    public void deleteCategory(@PathVariable int id) {
        try {
            Category category = categoryDao.getById(id);
            if (category == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");

            }
            categoryDao.delete(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong.", ex);
        }
    }

}
