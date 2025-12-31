package com.example.y_kiosk_prototype.service;

import com.example.y_kiosk_prototype.DTO.*;
import com.example.y_kiosk_prototype.composite_key.OptionCategoryId;
import com.example.y_kiosk_prototype.composite_key.OrderId;
import com.example.y_kiosk_prototype.composite_key.OrderedMenuId;
import com.example.y_kiosk_prototype.composite_key.OrderedMenuOptionId;
import com.example.y_kiosk_prototype.data.OrderState;
import com.example.y_kiosk_prototype.entity.*;
import com.example.y_kiosk_prototype.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final OrderRepository orderRepository;
    private final OrderedMenuRepository orderedMenuRepository;
    private final OrderedMenuOptionRepository orderedMenuOptionRepository;
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

    private int generateOrderNum(String storeId) {

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay(); // 2025-12-31 00:00:00
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX); // 2025-12-31 23:59:59

        return orderRepository.findFirstByOrderId_StoreIdAndOrderId_OrderTimeBetweenOrderByOrderId_OrderTimeDesc(
                        storeId, startOfDay, endOfDay
                )
                .map(lastOrder -> lastOrder.getOrderId().getOrderNum() + 1) // 마지막 주문이 있으면 번호 + 1
                .orElse(1);

    }
    public int placeOrder(OrderReqDto dto) {
        // 1. 매장 확인
        Store store = storeService.findStoreById(dto.getStoreId());
        log.info("입력된 StoreID: {} (길이: {})", dto.getStoreId(), dto.getStoreId().length());

        // 2. 주문(Order) 엔티티 생성
        // 복합키 구성 요소 결정 (주문번호는 보통 날짜+순번이나 시퀀스 사용)
        log.info("주문번호 생성");
        LocalDateTime orderTime = LocalDateTime.now().withNano(0);
        log.info("now: {}", orderTime);
        int orderNum = generateOrderNum(dto.getStoreId()); // 주문 번호 생성 로직(하단 참고)
        log.info("주문번호: {}", orderNum);

        log.info("주문 생성");
        /*
        Order order = Order.builder()
                .store(store)
                .orderId(
                        OrderId.builder()
                                .orderNum(orderNum)
                                .orderTime(orderTime)
                                .storeId(store.getStoreId())
                                .build()
                )
                .orderState(OrderState.ORDERED)
                .build();*/
        OrderId orderId = OrderId.builder()
                .orderNum(orderNum)
                .orderTime(orderTime.withNano(0)) // 초 단위 절삭은 유지!
                .storeId(store.getStoreId())
                .build();

// 2. Order 엔티티를 생성할 때 관계를 맺어줍니다.
        Order order = Order.builder()
                .orderId(orderId)  // 여기서 orderId 안의 storeId는 null 상태입니다.
                .store(store)      // @MapsId("storeId")가 이 store 객체의 ID를 가져와서 위 orderId에 꽂아줍니다.
                .orderState(OrderState.ORDERED)
                .build();

        log.info("order's storeId: {}", order.getStore().getStoreId());
        log.info("주문 생성 완료");

        int seq = 1;
        for (OrderedMenuReqDto menuDto : dto.getOrderedMenus()) {
            Menu menu = menuRepository.findMenuByMenuId(menuDto.getMenuId()).orElse(null);
            log.info("OrderedMenuId 생성");
            log.info("now: {}", orderTime);
            OrderedMenuId orderedMenuId = OrderedMenuId.builder()
                    .menuId(menuDto.getMenuId())
                    .orderNum(orderNum)
                    .orderTime(orderTime)
                    .storeId(store.getStoreId())
                    .orderedMenuSeq(seq++) //
                    .build();
            log.info("OrderedMenuId 생성완료");
            OrderedMenu orderedMenu = OrderedMenu.builder()
                    .orderedMenuId(orderedMenuId)
                    .quantity(menuDto.getQuantity())
                    .order(order)
                    .build();

            int menuGroupId = menu.getMenuCategory().getMenuGroup().getMenuGroupId();

            order.setMenuGroup(menuGroupRepository.findMenuGroupByMenuGroupId(menuGroupId).orElse(null));

            // 4. OrderedMenuOption 생성 루프
            for (OrderedMenuOptionReqDto optionDto : menuDto.getOrderedMenuOptions()) {
                OptionCategory optionCategory = optionCategoryRepository.findById(
                         OptionCategoryId.builder()
                                .categoryId(optionDto.getCategoryId())
                                .optionId(optionDto.getOptionId())
                                .menuId(menuDto.getMenuId())
                                .build()
                ).orElseThrow(() -> new RuntimeException("존재하지 않는 옵션 카테고리입니다."));
//new OptionCategoryId(optionDto.getCategoryId(), optionDto.getOptionId(), menuDto.getMenuId())

// 2. OrderedMenuOptionId 생성
                OrderedMenuOptionId optionId = OrderedMenuOptionId.builder()
                        .orderedMenuId(orderedMenuId)
                        .optionCategoryId(optionCategory.getOptionCategoryId()) // 조회한 객체의 ID 사용
                        .build();

                // 3. OrderedMenuOption 엔티티 생성 (여기가 핵심!)
                OrderedMenuOption orderedMenuOption = OrderedMenuOption.builder()
                        .orderedMenuOptionId(optionId)
                        .optionContent(optionDto.getOptionContent())
                        .orderedMenu(orderedMenu)
                        // 💡 401. 아래 줄이 반드시 있어야 합니다.
                        // @MapsId가 이 객체를 보고 ID를 채우기 때문에, 이게 null이면 에러가 납니다.
                        .optionCategory(optionCategory)
                        .build();

                orderedMenu.getOrderedMenuOptions().add(orderedMenuOption);

            }
            order.getOrderedMenus().add(orderedMenu);
        }

        orderRepository.save(order);
        return orderNum; // 생성된 주문 번호 반환
    }
}
