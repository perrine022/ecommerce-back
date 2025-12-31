package com.tradeall.tradefood.controller;

import com.tradeall.tradefood.dto.sirene.SireneCompanyInfoDTO;
import com.tradeall.tradefood.dto.sirene.SireneResponseDTO;
import com.tradeall.tradefood.service.SireneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sirene")
public class SireneController {

    private final SireneService sireneService;

    public SireneController(SireneService sireneService) {
        this.sireneService = sireneService;
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateSirene(@RequestParam String sirene) {
        try {
            SireneCompanyInfoDTO info = sireneService.validateSirene(sirene);
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/import-page")
    public ResponseEntity<SireneResponseDTO> importPage(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "62.02A") String activitePrincipale,
            @RequestParam(defaultValue = "100") Integer nombre,
            @RequestParam(required = false) String curseur) {
        
        SireneResponseDTO response = sireneService.fetchCompanies(q, activitePrincipale, nombre, curseur);
        return ResponseEntity.ok(response);
    }
}
