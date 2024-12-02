package ExamenFinal.ExamenFinal.controller;

import ExamenFinal.ExamenFinal.dto.FacturaDto;
import ExamenFinal.ExamenFinal.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/factura")
@RequiredArgsConstructor
public class FacturaController {

    private FacturaService invoiceService;

    @PostMapping("/{tiendaId}")
    public ResponseEntity<String> createInvoice(@PathVariable String tiendaId, @RequestBody FacturaDto facturaRequest) {
        try {
            invoiceService.processInvoice(tiendaId, facturaRequest);
            return ResponseEntity.ok("Factura creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la factura: " + e.getMessage());
        }
    }

}
