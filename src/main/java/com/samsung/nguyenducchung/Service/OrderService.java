package com.samsung.nguyenducchung.Service;

import com.samsung.nguyenducchung.Model.Entity.Order;
import com.samsung.nguyenducchung.Model.Entity.OrderItem;
import com.samsung.nguyenducchung.Model.Repository.OrderItemRepository;
import com.samsung.nguyenducchung.Model.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Lưu đơn hàng
    public void save(Order order) {
        orderRepository.save(order);
    }

    // Lưu order item
    public void saveOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }
}
