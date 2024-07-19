package bg.softuni.travelbudgetplanner.controller;

import bg.softuni.travelbudgetplanner.model.dto.PackingItemDTO;
import bg.softuni.travelbudgetplanner.model.dto.PackingListDTO;
import bg.softuni.travelbudgetplanner.service.PackingListService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/packing-lists")
public class PackingListController {

    private final PackingListService packingListService;

    public PackingListController(PackingListService packingListService) {
        this.packingListService = packingListService;
    }

    @GetMapping("/create/{tripId}")
    public String createPackingListForm(@PathVariable Long tripId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("packingList", new PackingListDTO());
        model.addAttribute("tripId", tripId);
        return "create-packing-list";
    }

    @PostMapping("/create/{tripId}")
    public String createPackingList(@PathVariable Long tripId, @Valid PackingListDTO packingListDTO, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            return "create-packing-list";
        }
        packingListService.createPackingList(tripId, packingListDTO);
        return "redirect:/trips/view/" + tripId;
    }

    @GetMapping("/add-items/{packingListId}")
    public String addPackingItemsForm(@PathVariable Long packingListId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("packingItem", new PackingItemDTO());
        model.addAttribute("packingListId", packingListId);
        return "add-packing-items";
    }

    @PostMapping("/add-items/{packingListId}")
    public String addPackingItem(@PathVariable Long packingListId, @Valid PackingItemDTO packingItemDTO, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        if (bindingResult.hasErrors()) {
            return "add-packing-items";
        }
        packingListService.addPackingItem(packingListId, packingItemDTO);
        model.addAttribute("successMessage", "Successfully added " + packingItemDTO.getName() + " to the list.");
        model.addAttribute("packingItem", new PackingItemDTO()); // Reset the form
        return "add-packing-items";
    }

    @PostMapping("/delete/{packingListId}")
    public String deletePackingList(@PathVariable Long packingListId, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        packingListService.deletePackingList(packingListId);
        model.addAttribute("successMessage", "Successfully deleted packing list.");
        return "redirect:/dashboard"; // Or wherever you want to redirect after deletion
    }
}
