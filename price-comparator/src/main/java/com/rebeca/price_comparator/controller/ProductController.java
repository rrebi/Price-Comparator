package com.rebeca.price_comparator.controller;

import com.rebeca.price_comparator.model.Discount;
import com.rebeca.price_comparator.model.PriceAlertDTO;
import com.rebeca.price_comparator.model.PriceEntry;
import com.rebeca.price_comparator.model.SubstituteDTO;
import com.rebeca.price_comparator.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/best-discounts")
    public ResponseEntity<?> getBestDiscounts() {
        var discounts = productService.getBestDiscounts();
        if (discounts.isEmpty()) {
            return jsonMessage("No active discounts found today.");
        }
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/new-discounts")
    public ResponseEntity<?> getNewDiscounts() {
        var discounts = productService.getNewDiscounts();
        if (discounts.isEmpty()) {
            return jsonMessage("No new discounts found in the last 24 hours.");
        }
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/{productId}/history")
    public ResponseEntity<?> getPriceHistory(@PathVariable String productId,
                                             @RequestParam(required = false) String store) {
        List<PriceEntry> history = productService.getPriceHistory(productId, store);
        if (history.isEmpty()) {
            return jsonMessage("No price history for product");
        }
        return ResponseEntity.ok(history);
    }

    private ResponseEntity<Map<String, String>> jsonMessage(String message) {
        return ResponseEntity.ok(Map.of("message", message));
    }

    //trying price history based on store/brand/category
    @GetMapping("/history")
    public ResponseEntity<?> getPriceHistoryForGroup(
            @RequestParam(required = false) String store,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category
    ) {
        var history = productService.getPriceHistoryFiltered(store, brand, category);
        if (history.isEmpty()) {
            return jsonMessage("No prixe history for product");
        }
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{productId}/substitutes")
    public ResponseEntity<?> getValueSubstitutes(
            @PathVariable String productId,
            @RequestParam(required = false) String store
    ) {
        List<SubstituteDTO> substitutes = productService.getSubstitutes(productId, store);
        if (substitutes.isEmpty()) {
            return jsonMessage("No substitutes found.");
        }
        return ResponseEntity.ok(substitutes);
    }

    @GetMapping("/{productId}/alerts")
    public ResponseEntity<?> getPriceAlerts(
            @PathVariable String productId,
            @RequestParam double target,
            @RequestParam(required = false) String store
    ) {
        var alerts = productService.getPriceAlerts(productId, target, store);
        if (alerts.isEmpty()) {
            return jsonMessage("No prices found below the target.");
        }
        return ResponseEntity.ok(alerts);
    }


    @PostMapping("/alerts")
    public ResponseEntity<?> addAlert(@RequestBody PriceAlertDTO request) {
        productService.registerAlert(request);
        return jsonMessage("Alert registered");
    }

    @GetMapping("/alerts/matches")
    public ResponseEntity<?> getTriggeredAlerts() {
        return ResponseEntity.ok(productService.getTriggeredAlerts());
    }

    @DeleteMapping("/alerts/{productId}")
    public ResponseEntity<?> deleteAlert(@PathVariable String productId) {
        boolean removed = productService.deleteAlertByProductId(productId);
        if (removed) {
            return jsonMessage("Alert deleted");
        } else {
            return jsonMessage("No alert found");
        }
    }

}
