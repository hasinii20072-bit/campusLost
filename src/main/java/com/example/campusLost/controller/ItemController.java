package com.example.campusLost.controller;

import com.example.campusLost.entity.ClaimRequest;
import com.example.campusLost.entity.Item;
import com.example.campusLost.repository.ClaimRepository;
import com.example.campusLost.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ClaimRepository claimRepository;

    // 1. Dashboard View
    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("items", itemRepository.findAll());
        model.addAttribute("claims", claimRepository.findAll());
        model.addAttribute("newItem", new Item());
        model.addAttribute("newClaim", new ClaimRequest());
        return "index";
    }

    // 2. Report Lost/Found Item
    @PostMapping("/save")
    public String saveItem(@ModelAttribute("newItem") Item newItem) {
        itemRepository.save(newItem);
        return "redirect:/";
    }

    // 3. Claim Submission (Proof-of-Ownership)
    @PostMapping("/claims/submit")
    public String submitClaim(
            @RequestParam("itemId") Long itemId,
            @RequestParam("claimerName") String claimerName,
            @RequestParam("claimerStudentId") String claimerStudentId,
            @RequestParam(name = "contactInfo", required = false) String contactInfo,
            @RequestParam("proofDescription") String proofDescription) {

        ClaimRequest claim = new ClaimRequest();
        claim.setItemId(itemId);
        claim.setClaimerName(claimerName);
        claim.setClaimerStudentId(claimerStudentId);
        claim.setContactInfo(contactInfo);
        claim.setProofDescription(proofDescription);
        
        claimRepository.save(claim);

        // Item status update to CLAIM_PENDING
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item != null) {
            item.setStatus("CLAIM_PENDING");
            itemRepository.save(item);
        }

        return "redirect:/";
    }

    // 4. Finder Review: Approve / Reject with strict ID validation
    @PostMapping("/claims/{id}/review")
    public String reviewClaim(
            @PathVariable Long id, 
            @RequestParam String action, 
            @RequestParam String reviewerStudentId) {
        
        ClaimRequest claim = claimRepository.findById(id).orElseThrow();
        Item item = itemRepository.findById(claim.getItemId()).orElseThrow();

        if ("APPROVE".equalsIgnoreCase(action)) {
            // 🔒 Strict Validation: Check if entered ID matches the item reporter's ID
            if (item.getReporterStudentId() != null && !item.getReporterStudentId().trim().equalsIgnoreCase(reviewerStudentId.trim())) {
                return "redirect:/?error=invalid_finder_id"; 
            }
            
            claim.setStatus("APPROVED");
            if (item.getContactInfo() != null) {
                claim.setContactInfo(item.getContactInfo());
            }
            item.setStatus("RESOLVED");
            
        } else if ("REJECT".equalsIgnoreCase(action)) {
            claim.setStatus("REJECTED");
            item.setStatus("OPEN");
        }
        
        claimRepository.save(claim);
        itemRepository.save(item);
        return "redirect:/";
    }

    // 5. Multi-Level Dispute Escalation (To Admin / Security Desk)
    @PostMapping("/claims/{id}/dispute")
    public String raiseDispute(@PathVariable Long id) {
        ClaimRequest claim = claimRepository.findById(id).orElseThrow();
        claim.setStatus("DISPUTED");
        claimRepository.save(claim);
        return "redirect:/";
    }
}