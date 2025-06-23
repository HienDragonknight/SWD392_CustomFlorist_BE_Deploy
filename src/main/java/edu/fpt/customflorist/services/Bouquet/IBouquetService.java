package edu.fpt.customflorist.services.Bouquet;

import edu.fpt.customflorist.dtos.Bouquet.BouquetDTO;
import edu.fpt.customflorist.dtos.Bouquet.BouquetRequestDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;

import java.util.List;

public interface IBouquetService {
    List<BouquetDTO> getAllBouquets();
    List<BouquetDTO> getAllActiveBouquets();
    BouquetDTO getBouquetById(Long id) throws DataNotFoundException;
    BouquetDTO createBouquet(BouquetRequestDTO bouquetDTO) throws DataNotFoundException;
    BouquetDTO updateBouquet(Long id, BouquetRequestDTO bouquetDTO) throws DataNotFoundException;
    void deleteBouquet(Long id) throws DataNotFoundException;
}
