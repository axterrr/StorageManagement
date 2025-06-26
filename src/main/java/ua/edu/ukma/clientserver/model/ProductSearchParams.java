package ua.edu.ukma.clientserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchParams {
    private String name;
    private String description;
    private String manufacturer;
    private Integer amountFrom;
    private Integer amountTo;
    private Double priceFrom;
    private Double priceTo;
    private Integer groupId;
}
