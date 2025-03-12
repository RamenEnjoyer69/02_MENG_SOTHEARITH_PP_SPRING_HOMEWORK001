package com.homework.model.request;

import com.homework.model.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentStatusRequest {
    private Long ticketId;
    private Boolean paymentStatus;
}