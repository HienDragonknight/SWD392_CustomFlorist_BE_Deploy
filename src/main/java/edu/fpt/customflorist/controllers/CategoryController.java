package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.dtos.Category.CategoryDTO;
import edu.fpt.customflorist.models.Category;
import edu.fpt.customflorist.responses.Category.CategoryResponse;
import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.services.Category.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/api/v1/categories")
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping
    public ResponseEntity<ResponseObject> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        try {
            Category category = categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Category created successfully")
                            .status(HttpStatus.OK)
                            .data(CategoryResponse.fromEntity(category))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        try {
            Category category = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Category updated successfully")
                            .status(HttpStatus.OK)
                            .data(CategoryResponse.fromEntity(category))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCategoryById(@PathVariable Long id) {
        try {
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Category retrieved successfully")
                            .status(HttpStatus.OK)
                            .data(CategoryResponse.fromEntity(category))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ResponseObject> findAllActiveCategories(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoryResponse> categories = categoryService.findAllActive(keyword, pageable)
                .map(CategoryResponse::fromEntity);

        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Active categories retrieved successfully")
                        .status(HttpStatus.OK)
                        .data(categories)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ResponseObject> findAllCategories(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                    !(authentication instanceof AnonymousAuthenticationToken);

            boolean isGuestOrCustomer = !isAuthenticated ||
                    authentication.getAuthorities().stream()
                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CUSTOMER") ||
                                    grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS"));

            Page<CategoryResponse> categories;
            if (isGuestOrCustomer) {
                // Nếu là GUEST hoặc CUSTOMER, chỉ lấy category có isActive = true
                categories = categoryService.findAllActive(keyword, pageable)
                        .map(CategoryResponse::fromEntity);
            } else {
                // Nếu là ADMIN hoặc STAFF, lấy tất cả category
                categories = categoryService.findAll(keyword, pageable)
                        .map(CategoryResponse::fromEntity);
            }

            return ResponseEntity.ok(
                    ResponseObject.builder()
                            .message("Categories retrieved successfully")
                            .status(HttpStatus.OK)
                            .data(categories)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(e.getMessage())
                            .status(HttpStatus.BAD_REQUEST)
                            .data(null)
                            .build()
            );
        }
    }

}
