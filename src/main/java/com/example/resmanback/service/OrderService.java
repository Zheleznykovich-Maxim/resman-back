package com.example.resmanback.service;

import com.example.resmanback.model.Order;
import com.example.resmanback.model.dto.OrderRequest;

import java.util.List;

/**
 * Сервис для управления заказами.
 */
public interface OrderService {

    /**
     * Получает список всех заказов.
     */
    List<Order> getAllOrders();

    /**
     * Получает заказ по его идентификатору.
     *
     * @param id идентификатор заказа
     */
    Order getOrderById(Long id);

    /**
     * Создает новый заказ на основе переданных данных.
     *
     * @param orderRequest данные запроса на создание заказа
     */
    Order saveOrder(OrderRequest orderRequest);

    /**
     * Удаляет заказ по его идентификатору.
     *
     * @param id идентификатор заказа
     */
    void deleteOrder(Long id);
}