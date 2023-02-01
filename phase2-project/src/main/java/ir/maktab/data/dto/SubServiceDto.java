package ir.maktab.data.dto;

import ir.maktab.data.model.MainService;
import jakarta.persistence.ManyToOne;

public class SubServiceDto {
    @ManyToOne
    private MainService mainService;
    private String subName;
    private double price;
    private String briefExplanation;
}
