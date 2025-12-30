package com.example.y_kiosk_prototype.service;

import com.example.y_kiosk_prototype.DTO.MenuCategoryReqDto;
import com.example.y_kiosk_prototype.DTO.MenuGroupReqDto;
import com.example.y_kiosk_prototype.DTO.MenuReqDto;
import com.example.y_kiosk_prototype.entity.Menu;
import com.example.y_kiosk_prototype.entity.MenuCategory;
import com.example.y_kiosk_prototype.entity.MenuGroup;
import com.example.y_kiosk_prototype.entity.Store;
import com.example.y_kiosk_prototype.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuRepository menuRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final OptionCategoryRepository optionCategoryRepository;
    private final StoreService storeService;
    private final S3Service s3Service;


    public MenuGroup createMenuGroup(@RequestBody MenuGroupReqDto menuGroupReqDto) {
        Store store = storeService.findStoreById(menuGroupReqDto.getStoreId());
        MenuGroup menuGroup = menuGroupReqDto.toEntity(store);

        menuGroupRepository.save(menuGroup);
        log.info("MenuGroup {} created", menuGroup.getMenuGroupId());

        return menuGroup;
    }

    public List<MenuGroup> findAllMenuGroupsByStoreId(String storeId) {
        Store store = storeService.findStoreById(storeId);
        List<MenuGroup> menuGroups = menuGroupRepository.findAllByStore(store);

        log.info("MenuGroups number{}", menuGroups.size());
        return menuGroups;
    }

    public MenuGroup findMenuGroupById(int menuGroupId) {
        return menuGroupRepository.findMenuGroupByMenuGroupId(menuGroupId).orElse(null);
    }

    public MenuCategory createMenuCategory(MenuCategoryReqDto menuCategoryReqDto) {
        MenuGroup menuGroup = findMenuGroupById(menuCategoryReqDto.getMenuGroupId());
        MenuCategory menuCategory = menuCategoryReqDto.toEntity(menuGroup);
        menuCategoryRepository.save(menuCategory);
        return menuCategory;
    }

    public List<MenuCategory> findAllMenuCategoriesByMenuGroupId(int menuGroupId) {
        MenuGroup menuGroup = findMenuGroupById(menuGroupId);
        List<MenuCategory> menuCategories = menuCategoryRepository.findMenuCategoryByMenuGroup(menuGroup);
        log.info("MenuCategories number{}", menuCategories.size());

        return menuCategories;
    }

    public Menu createMenu(MenuReqDto menuReqDto, MultipartFile image){
        String imageUrl = "";
        MenuCategory menuCategory = menuCategoryRepository.findMenuCategoryByMenuCategoryId(menuReqDto.getMenuCategoryId()).orElse(null);
        Menu menu = menuReqDto.toEntity(menuCategory);
        try {
            // 이미지가 비어있지 않으면 S3에 업로드하고 URL을 받음
            if (image != null && !image.isEmpty()) {
                imageUrl = s3Service.upload(image);
            }

            // DTO를 엔티티로 변환 (imageUrl 필드가 엔티티에 있어야 함)

            menu.setImageUrl(imageUrl);
            menuRepository.save(menu);

        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 중 오류 발생", e);
        }


        return menu;
    }
}
