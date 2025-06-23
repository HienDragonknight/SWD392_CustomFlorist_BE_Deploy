package edu.fpt.customflorist.services.Order;

import edu.fpt.customflorist.dtos.Order.OrderDTO;
import edu.fpt.customflorist.dtos.Order.OrderUpdateDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.models.*;
import edu.fpt.customflorist.models.Enums.Status;
import edu.fpt.customflorist.repositories.*;
import edu.fpt.customflorist.responses.DeliveryHistory.DeliveryHistoryResponse;
import edu.fpt.customflorist.responses.DeliveryHistory.DeliveryStatusHistoryResponse;
import edu.fpt.customflorist.responses.Order.OrderBouquetFlowerResponse;
import edu.fpt.customflorist.responses.Order.OrderItemResponse;
import edu.fpt.customflorist.responses.Order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderBouquetFlowerRepository orderBouquetFlowerRepository;
    private final UserRepository userRepository;
    private final PromotionRepository promotionRepository;
    private final BouquetRepository bouquetRepository;
    private final FlowerRepository flowerRepository;

    @Override
    public Order createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        Set<OrderItem> orderItems = new HashSet<>();
        List<OrderBouquetFlower> orderBouquetFlowers = new ArrayList<>();

        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Promotion promotion = null;
        if (orderDTO.getPromotionId() != null) {
            promotion = promotionRepository.findById(orderDTO.getPromotionId())
                    .orElseThrow(() -> new DataNotFoundException("Promotion not found"));

            if (promotion.getValidTo().isBefore(LocalDate.now())) {
                throw new DataNotFoundException("Promotion has expired");
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setPromotion(promotion);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setIsActive(true);

        order = orderRepository.save(order);

        for (var orderItemDTO : orderDTO.getOrderItems()) {
            Bouquet bouquet = bouquetRepository.findById(orderItemDTO.getBouquetId())
                    .orElseThrow(() -> new DataNotFoundException("Bouquet not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBouquet(bouquet);
            orderItem.setQuantity(orderItemDTO.getQuantity());
            orderItem.setSubTotal(orderItemDTO.getSubTotal());
            orderItem.setIsActive(true);

            orderItems.add(orderItem);

            orderItem = orderItemRepository.save(orderItem);

            for (var bouquetFlowerDTO : orderItemDTO.getOrderBouquetFlowers()) {
                Flower flower = flowerRepository.findById(bouquetFlowerDTO.getFlowerId())
                        .orElseThrow(() -> new DataNotFoundException("Flower not found"));

                OrderBouquetFlower orderBouquetFlower = new OrderBouquetFlower();
                orderBouquetFlower.setOrderItem(orderItem);
                orderBouquetFlower.setFlower(flower);
                orderBouquetFlower.setQuantity(bouquetFlowerDTO.getQuantity());

                orderBouquetFlowers.add(orderBouquetFlower);

                orderBouquetFlowerRepository.save(orderBouquetFlower);
            }

            orderItem.setOrderBouquetFlowers(orderBouquetFlowers);
            orderBouquetFlowers = new ArrayList<>();
        }

        order.setOrderItems(orderItems);
        return order;
    }

    @Override
    public void updateOrder(Long orderId, OrderUpdateDTO status) throws DataNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

        if (order.getStatus() == Status.DELIVERED || order.getStatus() == Status.CANCELLED) {
            throw new IllegalStateException("Cannot update order as it is already " + order.getStatus());
        }

        if (status.getStatus() == Status.CANCELLED) {
            order.setReason(status.getReason());
        }

        order.setStatus(status.getStatus());
        orderRepository.save(order);
    }


    @Override
    public void deleteOrder(Long orderId) throws DataNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

        order.setIsActive(false);
        orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) throws DataNotFoundException {
        return orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
    }

    @Transactional
    @Override
    public Page<Order> getAllOrdersActive(Long userId, LocalDateTime minOrderDate, LocalDateTime maxOrderDate,
                                          BigDecimal minPrice, BigDecimal maxPrice, String statusStr, Pageable pageable) {
        Status status = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + statusStr);
            }
        }
        return orderRepository.findActiveByFilters(userId, minOrderDate, maxOrderDate, minPrice, maxPrice, status, pageable);
    }

    @Transactional
    @Override
    public Page<Order> getAllOrdersActiveFoDelivery(LocalDateTime minOrderDate, LocalDateTime maxOrderDate, BigDecimal minPrice, BigDecimal maxPrice, String statusStr, String customerName, Pageable pageable) {
        Status status = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + statusStr);
            }
        }
        return orderRepository.findActiveByFilters(minOrderDate, maxOrderDate, minPrice, maxPrice, status, customerName, pageable);
    }

    @Transactional
    @Override
    public Page<Order> getAllOrders(LocalDateTime minOrderDate, LocalDateTime maxOrderDate,
                                    BigDecimal minPrice, BigDecimal maxPrice, String statusStr,
                                    Long userId, String userName, String phone, Pageable pageable) {
        Status status = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = Status.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + statusStr);
            }
        }
        return orderRepository.findAllByFiltersWithDeliveries(minOrderDate, maxOrderDate, minPrice, maxPrice, status, userId, userName, phone, pageable);
    }

    public OrderResponse convertToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getUserId())
                .userName(order.getUser().getName())
                .promotionId(order.getPromotion() != null ? order.getPromotion().getPromotionId() : null)
                .orderDate(order.getOrderDate())
                .status(order.getStatus().name())
                .reason(order.getReason())
                .totalPrice(order.getTotalPrice())
                .phone(order.getUser().getPhone())
                .shippingAddress(order.getShippingAddress())
                .isActive(order.getIsActive())
                .orderItems(order.getOrderItems().stream().map(this::convertToOrderItemResponse).toList())
                .deliveryHistories(order.getDeliveryHistories().stream().map(this::convertToDeliveryHistoryResponse).toList())
                .build();
    }

    public OrderItemResponse convertToOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .orderItemId(orderItem.getOrderItemId())
                .bouquetId(orderItem.getBouquet().getBouquetId())
                .bouquetName(orderItem.getBouquet().getName())
                .quantity(orderItem.getQuantity())
                .subTotal(orderItem.getSubTotal())
                .isActive(orderItem.getIsActive())
                .bouquetFlowers(orderItem.getOrderBouquetFlowers().stream().map(this::convertToOrderBouquetFlowerResponse).toList())
                .build();
    }

    public OrderBouquetFlowerResponse convertToOrderBouquetFlowerResponse(OrderBouquetFlower obf) {
        return OrderBouquetFlowerResponse.builder()
                .flowerId(obf.getFlower().getFlowerId())
                .flowerName(obf.getFlower().getName())
                .quantity(obf.getQuantity())
                .build();
    }

    public DeliveryHistoryResponse convertToDeliveryHistoryResponse(DeliveryHistory data) {
        return DeliveryHistoryResponse.fromEntity(data);
    }

    public DeliveryStatusHistoryResponse convertToDeliveryStatusHistoryResponse(DeliveryStatusHistory data) {
        return DeliveryStatusHistoryResponse.fromEntity(data);
    }

}