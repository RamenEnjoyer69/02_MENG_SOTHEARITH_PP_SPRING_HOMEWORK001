package com.homework.model.request;

import com.homework.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequest {
    private String passengerName;
    private ZonedDateTime travelDate;
    private String sourceStation;
    private String destinationStation;
    private BigDecimal price;
    private Boolean paymentStatus;
    private Status ticketStatus;
    private String seatNumber;
}
