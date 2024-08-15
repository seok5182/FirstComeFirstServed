package com.sparta.firstcomefirstserved.dto;

import com.sparta.firstcomefirstserved.entity.Cart;
import java.util.List;
import lombok.Getter;

@Getter
public class OrderRequest {

	List<Cart> cartList;
}
