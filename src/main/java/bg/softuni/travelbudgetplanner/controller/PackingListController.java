package bg.softuni.travelbudgetplanner.controller;

import bg.softuni.travelbudgetplanner.model.entity.PackingItem;
import bg.softuni.travelbudgetplanner.model.entity.PackingList;
import bg.softuni.travelbudgetplanner.service.PackingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/packing-lists")
public class PackingListController {

    private final PackingListService packingListService;

    @Autowired
    public PackingListController(PackingListService packingListService) {
        this.packingListService = packingListService;
    }

    @GetMapping("/add-items/{packingListId}")
    public String showAddItemForm(@PathVariable Long packingListId, Model model) {
        PackingList packingList = packingListService.findById(packingListId);
        if (packingList == null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("packingListId", packingListId);
        model.addAttribute("packingItem", new PackingItem());
        return "add-packing-items";
    }

    @PostMapping("/add-items/{packingListId}")
    public String addItemToPackingList(@PathVariable Long packingListId, @ModelAttribute PackingItem packingItem, RedirectAttributes redirectAttributes) {
        PackingList packingList = packingListService.findById(packingListId);
        if (packingList != null) {
            packingList.getItems().add(packingItem);
            packingItem.setPackingList(packingList);
            packingListService.save(packingList);
            redirectAttributes.addFlashAttribute("successMessage", "Item added successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to add item.");
        }
        return "redirect:/trips/view/" + packingList.getTrip().getId();
    }
}
