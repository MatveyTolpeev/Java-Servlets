package ru.service.db.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Order {
    private Integer orderId;
    private Integer transportId;
    private Integer clientId;
    private Integer employeeId;
    private Integer serviceId;
    private LocalDateTime startDate;
    private LocalDateTime planningEndDate;
}
