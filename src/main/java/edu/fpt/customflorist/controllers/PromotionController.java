package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.dtos.Promotion.PromotionDTO;
import edu.fpt.customflorist.dtos.Promotion.PromotionRequestDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.services.Promotion.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/api/v1/promotions")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
public class PromotionController {
    private final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<ResponseObject> getAllPromotions() {
        List<PromotionDTO> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get all promotions successfully")
                        .data(promotions)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<ResponseObject> getActivePromotions() {
        List<PromotionDTO> promotions = promotionService.getActivePromotions();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get active promotions successfully")
                        .data(promotions)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getPromotionById(@PathVariable Long id) throws DataNotFoundException {
        PromotionDTO promotion = promotionService.getPromotionById(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get promotion by id successfully")
                        .data(promotion)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ResponseObject> getPromotionByCode(@PathVariable String code) throws DataNotFoundException {
        PromotionDTO promotion = promotionService.findPromotionByCode(code);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get promotion by code successfully")
                        .data(promotion)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createPromotion(@RequestBody PromotionRequestDTO promotionDTO) {
        PromotionDTO createdPromotion = promotionService.createPromotion(promotionDTO);
        return new ResponseEntity<>(
                ResponseObject.builder()
                        .message("Create promotion successfully")
                        .data(createdPromotion)
                        .status(HttpStatus.CREATED)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updatePromotion(@PathVariable Long id, @RequestBody PromotionRequestDTO promotionDTO) throws DataNotFoundException {
        PromotionDTO updatedPromotion = promotionService.updatePromotion(id, promotionDTO);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Update promotion successfully")
                        .data(updatedPromotion)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deletePromotion(@PathVariable Long id) throws DataNotFoundException {
        promotionService.deletePromotion(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Delete promotion successfully")
                        .data(null)
                        .status(HttpStatus.NO_CONTENT)
                        .build()
        );
    }

//    @PostMapping("/apply")
//    public ResponseEntity<Map<String, Boolean>> applyPromotion(@RequestBody Map<String, String> request) {
//        String code = request.get("code");
//        boolean applied = promotionService.applyPromotion(code);
//        return ResponseEntity.ok(Map.of("applied", applied));
//    }
}
