package com.example.secondProject.DTO;

import com.example.secondProject.entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StockDTO {
    private String rank;
    private String name;
    private String rate;
    private String presentprice;
    private String updown;
    private String quantity;


}