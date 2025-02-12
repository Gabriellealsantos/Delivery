package com.portproject.delivery.entities;

import com.portproject.delivery.entities.enums.OrderStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private Double latitude;
    private Double longitude;
    private Instant moment;
    private OrderStatus status;

    @ManyToMany
    @JoinTable(name = "tb_order_product", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();


    public Order() {
    }

    public Order(String address, Double latitude, Double longitude, Instant moment, OrderStatus status) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.moment = moment;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Double getTotal() {
        double sum = 0.0;
        for (Product product : products) {
            sum += product.getPrice();
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
