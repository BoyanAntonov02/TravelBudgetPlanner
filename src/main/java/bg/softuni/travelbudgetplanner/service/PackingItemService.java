package bg.softuni.travelbudgetplanner.service;

import bg.softuni.travelbudgetplanner.model.entity.PackingItem;
import bg.softuni.travelbudgetplanner.repository.PackingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PackingItemService {


    private PackingItemRepository packingItemRepository;

    public PackingItem save(PackingItem packingItem) {
        return packingItemRepository.save(packingItem);
    }
}

