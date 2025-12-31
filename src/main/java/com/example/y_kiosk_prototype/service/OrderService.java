package com.example.y_kiosk_prototype.service;

import com.example.y_kiosk_prototype.DTO.StoreOrderResDto;
import com.example.y_kiosk_prototype.entity.Store;
import com.example.y_kiosk_prototype.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final StoreService storeService;

    @Transactional(readOnly = true)
    public StoreOrderResDto getCurrentStoreOrder(String storeId) {
        Store store = storeService.findStoreById(storeId);

        return StoreOrderResDto.from(store);
    }

}
