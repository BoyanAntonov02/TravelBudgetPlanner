package bg.softuni.travelbudgetplanner.service;

import bg.softuni.travelbudgetplanner.model.dto.PackingItemDTO;
import bg.softuni.travelbudgetplanner.model.dto.PackingListDTO;
import bg.softuni.travelbudgetplanner.model.entity.PackingItem;
import bg.softuni.travelbudgetplanner.model.entity.PackingList;
import bg.softuni.travelbudgetplanner.model.entity.Trip;
import bg.softuni.travelbudgetplanner.repository.PackingListRepository;
import bg.softuni.travelbudgetplanner.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PackingListService {

    private final PackingListRepository packingListRepository;
    private final TripRepository tripRepository;

    public PackingListService(PackingListRepository packingListRepository, TripRepository tripRepository) {
        this.packingListRepository = packingListRepository;
        this.tripRepository = tripRepository;
    }

    public void createPackingList(Long tripId, PackingListDTO packingListDTO) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("Invalid trip ID: " + tripId));
        PackingList packingList = new PackingList();
        packingList.setName(packingListDTO.getName());
        packingList.setTrip(trip);
        packingListRepository.save(packingList);
    }

    public void addPackingItem(Long packingListId, PackingItemDTO packingItemDTO) {
        PackingList packingList = packingListRepository.findById(packingListId).orElseThrow(() -> new IllegalArgumentException("Invalid packing list ID: " + packingListId));
        PackingItem packingItem = new PackingItem();
        packingItem.setName(packingItemDTO.getName());
        packingItem.setQuantity(packingItemDTO.getQuantity());
        packingItem.setPackingList(packingList);
        Set<PackingItem> items = packingList.getItems();
        if (items == null) {
            items = new HashSet<>();
        }
        items.add(packingItem);
        packingList.setItems(items);
        packingListRepository.save(packingList);
    }

    public void deletePackingList(Long packingListId) {
        packingListRepository.deleteById(packingListId);
    }
}
