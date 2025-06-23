package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.dtos.Bouquet.BouquetDTO;
import edu.fpt.customflorist.dtos.Bouquet.BouquetRequestDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.services.Bouquet.BouquetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/api/v1/bouquets")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
public class BouquetController {
    private final BouquetService bouquetService;

    @GetMapping
    public ResponseEntity<ResponseObject> getAllBouquets() {
        List<BouquetDTO> bouquets = bouquetService.getAllBouquets();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get all bouquets successfully")
                        .data(bouquets)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<ResponseObject> getAllActiveBouquets() {
        List<BouquetDTO> bouquets = bouquetService.getAllActiveBouquets();
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get all active bouquets successfully")
                        .data(bouquets)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBouquetById(@PathVariable("id") Long id) throws DataNotFoundException {
        BouquetDTO bouquet = bouquetService.getBouquetById(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Get bouquet by id successfully")
                        .data(bouquet)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ResponseObject> createBouquet(@RequestBody BouquetRequestDTO bouquetDTO) throws DataNotFoundException {
        BouquetDTO createdBouquet = bouquetService.createBouquet(bouquetDTO);
        return new ResponseEntity<>(
                ResponseObject.builder()
                        .message("Create bouquet successfully")
                        .data(createdBouquet)
                        .status(HttpStatus.CREATED)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateBouquet(@PathVariable("id") Long id, @RequestBody BouquetRequestDTO bouquetDTO) throws DataNotFoundException {
        BouquetDTO updatedBouquet = bouquetService.updateBouquet(id, bouquetDTO);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Update bouquet successfully")
                        .data(updatedBouquet)
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteBouquet(@PathVariable("id") Long id) throws DataNotFoundException {
        bouquetService.deleteBouquet(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Delete bouquet successfully")
                        .data(null)
                        .status(HttpStatus.NO_CONTENT)
                        .build()
        );
    }
}