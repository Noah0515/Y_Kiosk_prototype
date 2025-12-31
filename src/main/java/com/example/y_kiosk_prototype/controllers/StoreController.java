package com.example.y_kiosk_prototype.controllers;

import com.example.y_kiosk_prototype.DTO.*;
import com.example.y_kiosk_prototype.entity.*;
import com.example.y_kiosk_prototype.service.MenuService;
import com.example.y_kiosk_prototype.service.StoreService;
import com.example.y_kiosk_prototype.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class StoreController {
    private final StoreService storeService;
    private final UserService userService;
    private final MenuService menuService;

    @GetMapping("/store")
    @PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    public String getStorePage() {
        return "store";
    }

    @PreAuthorize("hasRole('NORMAL')")
    @PostMapping("/api/user/store/create")
    public ResponseEntity<String> createStore(@RequestBody StoreReqDto storeReqDto, Authentication authentication) {
        if (authentication == null) {
            log.warn("인증되지 않는 사용자의 접근");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("storeName: {}", storeReqDto.getStoreName());
        log.info("userDetails: {}", userDetails);

        UserInfo userInfo = userService.findUserByUserId(userDetails.getUsername());

        Store newStore = storeService.createStore(storeReqDto, userInfo);

        return ResponseEntity.status(HttpStatus.CREATED).body(newStore.getStoreId());
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    @GetMapping("/api/user/store/list")
    public ResponseEntity<List<StoreResDto>> getStoreList(Authentication authentication) {
        log.info("getStoreList");
        if (authentication == null) {
            log.warn("인증되지 않는 사용자의 접근");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserInfo userInfo = userService.findUserByUserId(userDetails.getUsername());

        List<Store> stores = storeService.findAllStoresByUserInfo(userInfo);
        log.info("stores number: {}", stores.size());

        List<StoreResDto> responseStoreResDtos = stores.stream().map(StoreResDto::from).toList();
        return ResponseEntity.ok(responseStoreResDtos);
    }


    @PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    @PostMapping("/api/user/store/group/create")
    public ResponseEntity<Integer> createGroup(@RequestBody MenuGroupReqDto menuGroupReqDto, Authentication authentication) {
        if (authentication == null) {
            log.warn("인증되지 않는 사용자의 접근");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("groupName: {}", menuGroupReqDto.getGroupName());
        log.info("userDetails: {}", userDetails);

        UserInfo userInfo = userService.findUserByUserId(userDetails.getUsername());

        MenuGroup newMenuGroup = menuService.createMenuGroup(menuGroupReqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newMenuGroup.getMenuGroupId());
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    @GetMapping("/api/user/store/group/list")
    public ResponseEntity<List<MenuGroupResDto>> getGroupList(@ModelAttribute StoreInfoReqDto storeInfoReqDto, Authentication authentication) {
        log.info("getMenuGroupList {}", storeInfoReqDto.getStoreId());
        if (authentication == null) {
            log.warn("인증되지 않는 사용자의 접근");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<MenuGroup> menuGroups = menuService.findAllMenuGroupsByStoreId(storeInfoReqDto.getStoreId());
        log.info("menuGroups number: {}", menuGroups.size());

        List<MenuGroupResDto> responseMenuGroupResDtos = menuGroups.stream().map(MenuGroupResDto::from).toList();
        return ResponseEntity.ok(responseMenuGroupResDtos);
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    @PostMapping("/api/user/store/group/category/create")
    public ResponseEntity<Integer> createCategory(@RequestBody MenuCategoryReqDto menuCategoryReqDto, Authentication authentication) {
        if (authentication == null) {
            log.warn("인증되지 않는 사용자의 접근");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("categoryName: {}", menuCategoryReqDto.getMenuCategoryName());
        log.info("userDetails: {}", userDetails);

        UserInfo userInfo = userService.findUserByUserId(userDetails.getUsername());

        MenuCategory newMenuCategory = menuService.createMenuCategory(menuCategoryReqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newMenuCategory.getMenuCategoryId());
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    @GetMapping("/api/user/store/group/category/list")
    public ResponseEntity<List<MenuCategoryResDto>> getCategoryList(@ModelAttribute MenuGroupInfoReqDto menuGroupInfoReqDto, Authentication authentication) {
        log.info("getCategoryList 메뉴 그룹 정보{}", menuGroupInfoReqDto.getMenuGroupId());
        if (authentication == null) {
            log.warn("인증되지 않는 사용자의 접근");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<MenuCategory> menuCategories = menuService.findAllMenuCategoriesByMenuGroupId(menuGroupInfoReqDto.getMenuGroupId());
        log.info("menuCategories number: {}", menuCategories.size());

        List<MenuCategoryResDto> responseMenuCategoryResDtos = menuCategories.stream().map(MenuCategoryResDto::from).toList();
        return ResponseEntity.ok(responseMenuCategoryResDtos);
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    @PostMapping("/api/user/store/group/category/menu/create")
    public ResponseEntity<Integer> createMenu(@RequestPart("menuData") MenuReqDto menuReqDto, @RequestPart("image")MultipartFile image, Authentication authentication) {
        if (authentication == null) {
            log.warn("인증되지 않는 사용자의 접근");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        log.info("menuName: {}", menuReqDto.getMenuName());
        log.info("userDetails: {}", userDetails);

        Menu newMenu = menuService.createMenu(menuReqDto, image);

        return ResponseEntity.status(HttpStatus.CREATED).body(newMenu.getMenuId());
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    @GetMapping("/api/user/store/menu-list")
    public ResponseEntity<StoreMenuDetailResDto> getStoreMenuList(@ModelAttribute StoreInfoReqDto storeInfoReqDto, Authentication authentication) {
        log.info("getStoreMenuList 가게 정보{}", storeInfoReqDto.getStoreId());
        if (authentication == null) {
            log.warn("인증되지 않는 사용자의 접근");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        StoreMenuDetailResDto storeMenuDetailResDto = storeService.getFullStoreMenu(storeInfoReqDto.getStoreId());

        log.info("storeMenuDetailResDto: {}", storeMenuDetailResDto);
        return ResponseEntity.ok(storeMenuDetailResDto);
    }

    @PreAuthorize("hasAnyRole('NORMAL', 'MANAGER')")
    @PostMapping("api/user/store/menu/new-order")
    public ResponseEntity<Integer> newOrder(@RequestBody OrderReqDto orderReqDto, Authentication authentication) {
        // 1. 서비스 호출하여 주문 처리 및 주문 번호 생성
        // (authentication 정보는 필요시 서비스에 넘겨 소유권 검증 등에 활용 가능)
        int orderNum = menuService.placeOrder(orderReqDto);

        // 2. 생성된 주문 번호를 클라이언트(안드로이드)에 반환
        return ResponseEntity.ok(orderNum);
    }
}
