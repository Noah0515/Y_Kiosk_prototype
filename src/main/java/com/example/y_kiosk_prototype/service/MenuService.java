package com.example.y_kiosk_prototype.service;

import com.example.y_kiosk_prototype.DTO.MenuGroupReqDto;
import com.example.y_kiosk_prototype.entity.MenuGroup;
import com.example.y_kiosk_prototype.entity.Store;
import com.example.y_kiosk_prototype.repository.MenuGroupRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final StoreService storeService;


    public MenuGroup createMenuGroup(@RequestBody MenuGroupReqDto menuGroupReqDto) {
        Store store = storeService.findStoreById(menuGroupReqDto.getStoreId());
        MenuGroup menuGroup = menuGroupReqDto.toEntity(store);

        menuGroupRepository.save(menuGroup);
        log.info("MenuGroup {} created", menuGroup.getMenuGroupId());

        return menuGroup;
    }

    public List<MenuGroup> getAllMenuGroupsByStoreId(String storeId) {
        Store store = storeService.findStoreById(storeId);
        List<MenuGroup> menuGroups = menuGroupRepository.findAllByStore(store);

        log.info("MenuGroups number{}", menuGroups.size());
        return menuGroups;
    }
}
