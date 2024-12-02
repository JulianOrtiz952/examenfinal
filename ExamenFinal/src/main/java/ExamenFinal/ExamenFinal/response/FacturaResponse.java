package ExamenFinal.ExamenFinal.response;

import lombok.Data;

@Data
public class FacturaResponse {
    private String status;
    private String message;
    private FacturaData data;
}
