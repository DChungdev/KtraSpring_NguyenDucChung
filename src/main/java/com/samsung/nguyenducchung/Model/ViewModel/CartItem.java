package com.samsung.nguyenducchung.Model.ViewModel;

import com.samsung.nguyenducchung.Model.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;

}
