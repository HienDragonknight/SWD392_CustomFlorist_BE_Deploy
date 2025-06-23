package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.dtos.Flower.FlowerDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.models.Flower;
import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.services.Flower.IFlowerService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/api/v1/flowers")
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
public class FlowerController {
    private final IFlowerService flowerService;

    @PostMapping
    public ResponseEntity<?> createFlower(@Valid @RequestBody FlowerDTO flowerDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Flower flower = flowerService.createFlower(flowerDTO);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Flower created successfully")
                            .data(flower)
                            .status(HttpStatus.CREATED)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{flowerId}")
    public ResponseEntity<?> updateFlower(@PathVariable Long flowerId, @Valid @RequestBody FlowerDTO flowerDTO, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Flower flower = flowerService.updateFlower(flowerId, flowerDTO);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Flower updated successfully")
                            .data(flower)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{flowerId}")
    public ResponseEntity<?> getFlowerById(@PathVariable Long flowerId) {
        try {
            Flower flower = flowerService.getFlowerById(flowerId);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Flower retrieved successfully")
                            .data(flower)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFlowers(@RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) String flowerType,
                                           @RequestParam(required = false) String color,
                                           @RequestParam(required = false) BigDecimal minPrice,
                                           @RequestParam(required = false) BigDecimal maxPrice,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "50") int size,
                                           @Parameter(description = "Sort direction (ASC or DESC), default is ASC", example = "ASC")
                                               @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "flowerId"));
            Page<Flower> flowers = flowerService.getAllFlowers(keyword, flowerType, color, minPrice, maxPrice, pageable);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Flowers retrieved successfully")
                            .data(flowers)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getAllFlowersActive(@RequestParam(required = false) String keyword,
                                                 @RequestParam(required = false) String flowerType,
                                                 @RequestParam(required = false) String color,
                                                 @RequestParam(required = false) BigDecimal minPrice,
                                                 @RequestParam(required = false) BigDecimal maxPrice,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "50") int size,
                                                 @Parameter(description = "Sort direction (ASC or DESC), default is ASC", example = "ASC")
                                                     @RequestParam(defaultValue = "ASC") Sort.Direction direction
                                                 ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "flowerId"));
            Page<Flower> flowers = flowerService.getAllFlowersActive(keyword, flowerType, color, minPrice, maxPrice, pageable);
            return ResponseEntity.ok().body(
                    ResponseObject.builder()
                            .message("Active flowers retrieved successfully")
                            .data(flowers)
                            .status(HttpStatus.OK)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
