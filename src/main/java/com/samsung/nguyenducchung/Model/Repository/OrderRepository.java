package com.samsung.nguyenducchung.Model.Repository;

import com.samsung.nguyenducchung.Model.Entity.Order;
import com.samsung.nguyenducchung.Model.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(Users user);
}

