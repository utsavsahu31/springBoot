package com.productapp.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.productapp.dto.ProductDto;
import com.productapp.repository.Product;
import com.productapp.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping(path = "api")
public class ProductApi {

	private ProductService productService;

	@Autowired
	public ProductApi(ProductService productService) {
		this.productService = productService;
	}

	// --------get all products----------
	// @GetMapping(path = "products")
	@Operation(summary  = "get all products")
	@GetMapping(path = "products", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<Product> findAll() {
//		if(1==1)
//			throw new RuntimeException();//let assume some error in backend
		return productService.findAll();
	}
	
	@GetMapping(path = "productsV2")
	public CollectionModel<Product> findAllV2() {
		List<Product> products = productService.findAll();
		for (Product product : products) {
			Link link = linkTo(methodOn(ProductApi.class).findByIdLink(product.getId())).withSelfRel();
			product.add(link);
		}
		return CollectionModel.of(products);
}

	@Operation(summary  = "get an product by id")
	@GetMapping(path = "products/{id}")
	public Product findById(@Parameter(description = "product id must be pass")  @PathVariable(name = "id") int id) {
		return productService.getById(id);
	}

	@GetMapping(path = "productsV2/{id}")
	public EntityModel<Product> findByIdLink(@PathVariable(name = "id") int id) {
		Link link = linkTo(methodOn(ProductApi.class).findByIdLink(id)).withSelfRel();
		Product product = productService.getById(id);
		// now convert product to product dto
		//ProductDto productDto = convertProductToProductDto(product);

		product.add(link);
		return EntityModel.of(product);
	}

	
	private ProductDto convertProductToProductDto(Product product) {
		ProductDto productDto = new ProductDto();
		productDto.setId(product.getId());
		productDto.setName(product.getName());
		productDto.setPrice(product.getPrice());
		return productDto;
	}

	// ResponseEntity : bag container 2 things status code and data
	@Operation(summary  = "add a new product")
	@PostMapping(path = "products")
	public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductDto productDto) {
		// i need to convert dto to entity class
		Product product = convertProductDtoToProduct(productDto);
		Product savedProduct = productService.addProduct(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
	}

	private Product convertProductDtoToProduct(ProductDto productDto) {
		Product product = new Product();
		product.setId(productDto.getId());
		product.setName(productDto.getName());
		product.setPrice(productDto.getPrice());
		return product;
	}

//	@ResponseStatus(code=HttpStatus.NO_CONTENT)
//	@DeleteMapping(path = "products/{id}")
//	public void deleteProduct(@PathVariable(name = "id") int id){
//		  productService.deleteProduct(id);
//	}

	@DeleteMapping(path = "products/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") int id) {
		productService.deleteProduct(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping(path = "products/{id}")
	public Product updateProduct(@PathVariable(name = "id") int id, @RequestBody Product product) {
		return productService.updateProduct(id, product);
	}
	


}
