package com.productapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.productapp.exceptions.ProductNotFoundException;
import com.productapp.repository.Product;
import com.productapp.repository.ProductDao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService{

	private ProductDao productDao;
	
	@Autowired
	public ProductServiceImpl(ProductDao productDao) {
		this.productDao = productDao;
	}

	@Override
	@Cacheable(value="products")
	public List<Product> findAll() {
		return productDao.findAll();
	}

	@Override
	@Cacheable(value="products", key = "#id")
	public Product getById(int id) {
		return productDao.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("product with id" + id + " is not found"));
	}

	@Override
	@CachePut(value="products", key="#result.id")
	public Product addProduct(Product product) {
		productDao.save(product);
		return product;
	}

	@Override
	@CachePut(value="products", key="#result.id")
	public Product updateProduct(int id, Product product) {
		Product productToUpdate= getById(id);
		productToUpdate.setPrice(product.getPrice());
		productDao.save(productToUpdate);//save works for both save and update operation
		return productToUpdate;
	}

	@Override
	@CacheEvict(value="products", key="#id")
	public Product deleteProduct(int id) {
		Product productToDelete= getById(id);
		productDao.delete(productToDelete);
		return productToDelete;
	}
	
	@CacheEvict(value="products", allEntries=true)
	@Override
	public void evictCache() {
		log.info("cache is cleared...");
	}

}
