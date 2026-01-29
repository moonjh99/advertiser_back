package com.example.advertiser.order.repository;

import com.example.advertiser.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        select distinct o
        from Order o
        left join fetch o.items
        where o.userId = :userId
        order by o.createdAt desc
    """)
    List<Order> findWithItemsByUserId(@Param("userId") Long userId);


    @Query("""
        select o
        from Order o
        left join fetch o.items
        where o.id = :id
    """)
    Optional<Order> findByIdWithItems(@Param("id") Long id);

}
