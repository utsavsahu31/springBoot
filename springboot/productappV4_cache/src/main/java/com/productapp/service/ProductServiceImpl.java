package com.productapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.productapp.exceptions.ProductNotFoundException;
import com.productapp.repository.Product;
import com.productapp.repository.ProductDao;

//2. apply caching
@Service
@Transactional
public class ProductServiceImpl implements ProductService{

	private Logger logger=LoggerFactory.getLogger(ProductServiceImpl.class);
	private ProductDao productDao;
	
	@Autowired
	public ProductServiceImpl(ProductDao productDao) {
		this.productDao = productDao;
	}

	@Cacheable(value = "products")
	@Override
	public List<Product> findAll() {
		logger.info("===========findAll is called==========");
		return productDao.findAll();
	}
	
	@Cacheable(value = "products", key = "#id")
	@Override
	public Product getById(int id) {
		logger.info("===========product by id is called==========");
		return productDao.findById(id)
				.orElseThrow(() -> new ProductNotFoundException("product with id" + id + " is not found"));
	}

	@CachePut(value = "products", key = "#result.id")
	@Override
	public Product addProduct(Product product) {
		productDao.save(product);
		return product;
	}

	@CachePut(value = "products", key = "#result.id")
	@Override
	public Product updateProduct(int id, Product product) {
		Product productToUpdate= getById(id);
		productToUpdate.setPrice(product.getPrice());
		productDao.save(productToUpdate);//save works for both save and update operation
		return productToUpdate;
	}

	@CacheEvict(value = "products", key = "#id")
	@Override
	public Product deleteProduct(int id) {
		Product productToDelete= getById(id);
		productDao.delete(productToDelete);
		return productToDelete;
	}

	@CacheEvict(value = "products", allEntries = true)//remove all cache
	@Override
	public void evictCache() {}

}
