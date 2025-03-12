package com.homework.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    private Long ticketId;
    private String passengerName;
    private ZonedDateTime travelDate;
    private String sourceStation;
    private String destinationStation;
    private BigDecimal price;
    private Boolean paymentStatus;
    private Status ticketStatus;
    private String seatNumber;
}
