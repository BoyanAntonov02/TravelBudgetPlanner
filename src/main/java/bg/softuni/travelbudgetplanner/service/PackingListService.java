package bg.softuni.travelbudgetplanner.service;

import bg.softuni.travelbudgetplanner.model.entity.PackingList;
import bg.softuni.travelbudgetplanner.repository.PackingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackingListService {

    private final PackingListRepository packingListRepository;

    @Autowired
    public PackingListService(PackingListRepository packingListRepository) {
        this.packingListRepository = packingListRepository;
    }

    public PackingList findById(Long id) {
        return packingListRepository.findById(id).orElse(null);
    }

    public PackingList save(PackingList packingList) {
        return packingListRepository.save(packingList);
    }
}
