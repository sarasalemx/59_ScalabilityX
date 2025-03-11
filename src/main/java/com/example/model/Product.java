package com.example.model;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class Product {
    private UUID id;
    private String name;
    private double price;
}