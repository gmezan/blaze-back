package com.example.blaze.test.service;

import com.example.blaze.test.domain.Order;
import com.example.blaze.test.domain.enums.OrderStatus;
import com.example.blaze.test.domain.enums.Taxes;
import com.example.blaze.test.dto.AddItemsDto;
import com.example.blaze.test.dto.OrderDto;
import com.example.blaze.test.repository.OrderRepository;
import com.example.blaze.test.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }


    @Override
    public Page<OrderDto> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable).map(this::toOrderDto);
    }

    @Override
    public OrderDto get(String id) {
        return orderRepository.findById(id).map(this::toOrderDto)
                .map(this::fillProducts).orElse(null);
    }

    @Override
    public OrderDto create(OrderDto orderDto) {
        this.calculateAmounts(orderDto);
        var order = this.toOrder(orderDto);
        order.setNumber(null);
        order.setNumber(String.valueOf(this.sequenceGeneratorService.generateSequenceOrder(Order.SEQUENCE_NAME)));
        return this.toOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto update(String id, OrderDto orderDto) {
        return this.orderRepository.findById(id)
                .filter(o -> o.getStatus().equals(OrderStatus.PENDING))
                .map(order -> {
                    this.calculateAmounts(orderDto);
                    var newO = this.toOrder(orderDto);
                    newO.setNumber(order.getNumber());
                    return this.orderRepository.save(newO);
                })
                .map(this::toOrderDto)
                .map(this::fillProducts)
                .orElse(null);
    }

    @Override
    public void delete(String id) {
        this.orderRepository.deleteById(id);
    }

    /*
        Changes the order status to REJECT
     */
    @Override
    public OrderDto reject(String id, OrderDto orderDto) {
        return this.orderRepository.findById(id)
                .filter(o -> o.getStatus().equals(OrderStatus.PENDING))
                .map(order -> {
                    order.setStatus(OrderStatus.REJECTED);
                    return this.orderRepository.save(order);
                })
                .map(this::toOrderDto)
                .map(this::fillProducts)
                .orElse(null);
    }

    /*
        Changes the order status to COMPLETE
     */
    @Override
    public OrderDto complete(String id, OrderDto orderDto) {
        return this.orderRepository.findById(id)
                .filter(o -> o.getStatus().equals(OrderStatus.PENDING))
                .map(order -> {
                    order.setStatus(OrderStatus.COMPLETED);
                    return this.orderRepository.save(order);
                })
                .map(this::toOrderDto)
                .map(this::fillProducts)
                .orElse(null);
    }

    /*
        Adds 1 unit of product with id=:id in the products attribute of Order with number=:number
     */
    @Override
    public OrderDto addProduct(String number, String id, OrderDto orderDto) {
        log.info("Adding product={} for order={}", id, number);
        return this.orderRepository.findById(number)
                .filter(o -> o.getStatus().equals(OrderStatus.PENDING))
                .map(this::toOrderDto)
                .map(dto -> {
                    this.productRepository.findById(id).flatMap(p -> dto.getProducts().stream().filter(po -> po.getId().equals(p.getId())).findFirst())
                            .ifPresent(productOrder -> productOrder.setQuantity(productOrder.getQuantity() + 1));
                    return dto;
                })
                .map(this::calculateAmounts)
                .map(this::toOrder)
                .map(this.orderRepository::save)
                .map(this::toOrderDto)
                .map(this::fillProducts)
                .orElse(null);
    }

    /*
        Deletes 1 unit of product with id=:id in the products attribute of Order with number=:number
     */
    @Override
    public OrderDto deleteProduct(String number, String id, OrderDto orderDto) {
        log.info("Deleting product={} for order={}", id, number);
        return this.orderRepository.findById(number)
                .filter(o -> o.getStatus().equals(OrderStatus.PENDING))
                .map(this::toOrderDto)
                .map(dto -> {
                    this.productRepository.findById(id).flatMap(p ->
                            dto.getProducts().stream().filter(po -> po.getId().equals(p.getId())).findFirst())
                            .ifPresent(productOrder -> Optional.of(productOrder.getQuantity()).ifPresent(q -> {
                                if (q <= 1) {
                                    dto.getProducts().remove(productOrder);
                                } else {
                                    productOrder.setQuantity(productOrder.getQuantity() - 1);
                                }
                    }));
                    return dto;
                })
                .map(this::calculateAmounts)
                .map(this::toOrder)
                .map(this.orderRepository::save)
                .map(this::toOrderDto)
                .map(this::fillProducts)
                .orElse(null);
    }

    /*
        Adds products from the itemsDto.itemsSelected list to the Order with number=:number
     */
    @Override
    public OrderDto addItems(String number, AddItemsDto itemsDto) {
        return this.orderRepository.findById(number)
                .filter(o -> o.getStatus().equals(OrderStatus.PENDING))
                .map(this::toOrderDto)
                .map(dto -> {
                    var currentItems = dto.getProducts().stream().map(OrderDto.ProductOrder::getId).collect(Collectors.toList());
                    itemsDto.getItemsSelected().stream().filter(item -> !currentItems.contains(item.getId()))
                            .forEach(i -> dto.getProducts().add(i));
                    return dto;
                })
                .map(this::calculateAmounts)
                .map(this::toOrder)
                .map(this.orderRepository::save)
                .map(this::toOrderDto)
                .map(this::fillProducts)
                .orElse(null);
    }

    /*
        Computes total amount and taxes amounts
     */
    private OrderDto calculateAmounts(OrderDto orderDto) {
        this.fillProducts(orderDto);
        orderDto.setTotalAmount(orderDto.getProducts().stream()
                        .map(po -> po.getPrice().multiply(BigDecimal.valueOf(po.getQuantity())))
                        .reduce(BigDecimal::add).orElse(BigDecimal.ZERO)
        );
        orderDto.setTaxesAmounts(new HashMap<>());
        orderDto.setTotalTaxes(BigDecimal.ZERO);
        var subTotal = orderDto.getTotalAmount();
        var subTotalTax = BigDecimal.ZERO;
        for (Taxes tax: Taxes.values()) {
            subTotalTax =  tax.getRate().multiply(subTotal);
            orderDto.getTaxesAmounts().put(tax.getName(), subTotalTax);
            subTotal = subTotal.add(subTotalTax);
        }
        orderDto.setTotalTaxes(orderDto.getTaxesAmounts().values().stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO));

        return orderDto;
    }

    /*
        Completes product information (name and unit price) for the web dto
     */
    private OrderDto fillProducts(OrderDto orderDto) {
        Optional.of(orderDto.getProducts()).orElseGet(ArrayList::new)
                .forEach(productOrder -> productRepository.findById(productOrder.getId())
                        .ifPresent(product -> {
                            productOrder.setName(product.getName());
                            productOrder.setPrice(product.getPrice());
                        }));
        return orderDto;
    }


    /*
        MAPPERS:
        Maps Order to OrderDto, and the other way round
     */

    private Order toOrder(OrderDto dto) {
        var o = new Order();
        BeanUtils.copyProperties(dto, o);
        o.setOrderDate(dto.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        o.setProducts(new ArrayList<>());
        dto.getProducts().forEach(productOrder -> {
            var po = new Order.ProductOrder();
            po.setId(productOrder.getId());
            po.setQuantity(productOrder.getQuantity());
            o.getProducts().add(po);
        });
        return o;
    }

    private OrderDto toOrderDto(Order o) {
        var dto = new OrderDto();
        BeanUtils.copyProperties(o, dto);
        if (o.getOrderDate() != null)
            dto.setDate(LocalDate.parse(o.getOrderDate(), DateTimeFormatter.ISO_LOCAL_DATE));
        dto.setProducts(new ArrayList<>());
        o.getProducts().forEach(productOrder -> {
            var po = new OrderDto.ProductOrder();
            po.setId(productOrder.getId());
            po.setQuantity(productOrder.getQuantity());
            dto.getProducts().add(po);
        });
        return dto;
    }
}
