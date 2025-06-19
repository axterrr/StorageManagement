package ua.edu.ukma.clientserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Integer id;
    private String name;
    private String description;
    private String manufacturer;
    private Integer amount;
    private Double price;
    private Integer groupId;
}
