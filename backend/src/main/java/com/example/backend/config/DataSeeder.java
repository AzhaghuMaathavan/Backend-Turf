package com.example.backend.config;

import com.example.backend.entity.Category;
import com.example.backend.entity.Product;
import com.example.backend.entity.User;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.ProductRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Bean
	CommandLineRunner seedData() {
		return args -> {
			Category football = categoryRepository.findByCategoryName("Football Turf")
					.orElseGet(() -> categoryRepository.save(Category.builder()
							.categoryName("Football Turf")
							.description("Outdoor football turf booking")
							.build()));

			Category basketball = categoryRepository.findByCategoryName("Basketball Court")
					.orElseGet(() -> categoryRepository.save(Category.builder()
							.categoryName("Basketball Court")
							.description("Indoor basketball court booking")
							.build()));

			Category badminton = categoryRepository.findByCategoryName("Badminton Court")
					.orElseGet(() -> categoryRepository.save(Category.builder()
							.categoryName("Badminton Court")
							.description("Indoor badminton court booking")
							.build()));

			productRepository.findByProductName("Football Turf")
					.map(existing -> {
						existing.setDescription("Book football turf slots with lights.");
						existing.setPrice(499.0);
						existing.setStockQuantity(40);
						existing.setImageUrl("/images/football.png");
						existing.setCategory(football);
						return productRepository.save(existing);
					})
					.orElseGet(() -> productRepository.save(Product.builder()
							.productName("Football Turf")
							.description("Book football turf slots with lights.")
							.price(499.0) // INR per hour
							.stockQuantity(40) // Total available slot-units (consumed by duration)
							.imageUrl("/images/football.png")
							.category(football)
							.build()));

			productRepository.findByProductName("Basketball Court")
					.map(existing -> {
						existing.setDescription("Indoor court. Includes equipment.");
						existing.setPrice(299.0);
						existing.setStockQuantity(30);
						existing.setImageUrl("/images/basketball.png");
						existing.setCategory(basketball);
						return productRepository.save(existing);
					})
					.orElseGet(() -> productRepository.save(Product.builder()
							.productName("Basketball Court")
							.description("Indoor court. Includes equipment.")
							.price(299.0) // INR per hour
							.stockQuantity(30)
							.imageUrl("/images/basketball.png")
							.category(basketball)
							.build()));

			productRepository.findByProductName("Badminton Court")
					.map(existing -> {
						existing.setDescription("Indoor badminton court with nets.");
						existing.setPrice(249.0);
						existing.setStockQuantity(30);
						existing.setImageUrl("/images/badminton.png");
						existing.setCategory(badminton);
						return productRepository.save(existing);
					})
					.orElseGet(() -> productRepository.save(Product.builder()
							.productName("Badminton Court")
							.description("Indoor badminton court with nets.")
							.price(249.0) // INR per hour
							.stockQuantity(30)
							.imageUrl("/images/badminton.png")
							.category(badminton)
							.build()));

			userRepository.findByEmail("demo@example.com")
					.orElseGet(() -> userRepository.save(User.builder()
							.userName("demo")
							.email("demo@example.com")
							.password(passwordEncoder.encode("demo"))
							.role(User.UserRole.CUSTOMER)
							.build()));

			userRepository.findByEmail("admin@example.com")
					.orElseGet(() -> userRepository.save(User.builder()
							.userName("admin")
							.email("admin@example.com")
							.password(passwordEncoder.encode("admin"))
							.role(User.UserRole.ADMIN)
							.build()));
		};
	}
}
