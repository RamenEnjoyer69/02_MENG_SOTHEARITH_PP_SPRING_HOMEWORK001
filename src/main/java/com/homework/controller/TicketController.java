package com.homework.controller;

import com.homework.model.entity.ApiResponse;
import com.homework.model.entity.Status;
import com.homework.model.entity.Ticket;
import com.homework.model.request.TicketRequest;
import com.homework.model.request.UpdatePaymentStatusRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final static List<Ticket> TICKETS = new ArrayList<>();
    private final static AtomicLong TICKET_ID = new AtomicLong(6L);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TicketController() {
        TICKETS.add(new Ticket(1L, "Ana de Armas", ZonedDateTime.now(ZoneId.of("America/New_York")), "New York", "Los Angeles", new BigDecimal("200.50"), true, Status.COMPLETED, "A1"));
        TICKETS.add(new Ticket(2L, "Scarlett Johansson", ZonedDateTime.now(ZoneId.of("America/Chicago")), "Chicago", "Houston", new BigDecimal("150.75"), true, Status.COMPLETED, "A2"));
        TICKETS.add(new Ticket(3L, "Jon Snow", ZonedDateTime.now(ZoneId.of("America/Denver")), "The North", "Seattle", new BigDecimal("120.00"), false, Status.COMPLETED, "A3"));
        TICKETS.add(new Ticket(4L, "Harry Potter", ZonedDateTime.now(ZoneId.of("America/New_York")), "Boston", "Washington DC", new BigDecimal("180.25"), true, Status.CANCELLED, "A4"));
        TICKETS.add(new Ticket(5L, "Jack Sparrow", ZonedDateTime.now(ZoneId.of("America/New_York")), "Miami", "Orlando", new BigDecimal("90.00"), true, Status.BOOKED, "A5"));
    }

    @GetMapping("/getAll")
    public ApiResponse<List<Ticket>> getAllTickets(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        int start = (page - 1) * size;
        int end = Math.min(start + size, TICKETS.size());

        if (start >= TICKETS.size()) {
            return new ApiResponse<>(true, "No tickets found", HttpStatus.OK, new ArrayList<>(), ZonedDateTime.now());
        }

        return new ApiResponse<>(true, "Tickets retrieved successfully", HttpStatus.OK, TICKETS.subList(start, end), ZonedDateTime.now());
    }

    @PostMapping("/add")
    public ApiResponse<Ticket> saveTicket(@RequestBody TicketRequest request) {
        Ticket ticket = new Ticket(TICKET_ID.getAndIncrement(), request.getPassengerName(), request.getTravelDate(), request.getSourceStation(), request.getDestinationStation(), request.getPrice(), request.getPaymentStatus(), request.getTicketStatus(), request.getSeatNumber());
        TICKETS.add(ticket);
        return new ApiResponse<>(true, "Ticket added successfully", HttpStatus.CREATED, ticket, ZonedDateTime.now());
    }

    @GetMapping("/{ticket-id}")
    public ApiResponse<Ticket> getTicketById(@PathVariable("ticket-id") Long ticketId) {
        for (Ticket ticket : TICKETS) {
            if (ticket.getTicketId().equals(ticketId)) {
                return new ApiResponse<>(true, "Ticket found", HttpStatus.OK, ticket, ZonedDateTime.now());
            }
        }
        return new ApiResponse<>(false, "Ticket not found", HttpStatus.NOT_FOUND, null, ZonedDateTime.now());
    }

    @PutMapping("/{ticket-id}")
    public ApiResponse<Ticket> updateTicketById(@PathVariable("ticket-id") Long ticketId, @RequestBody TicketRequest request) {
        for (Ticket ticket : TICKETS) {
            if (ticket.getTicketId().equals(ticketId)) {
                ticket.setPassengerName(request.getPassengerName());
                ticket.setTravelDate(request.getTravelDate());
                ticket.setSourceStation(request.getSourceStation());
                ticket.setDestinationStation(request.getDestinationStation());
                ticket.setPrice(request.getPrice());
                ticket.setPaymentStatus(request.getPaymentStatus());
                ticket.setTicketStatus(request.getTicketStatus());
                ticket.setSeatNumber(request.getSeatNumber());
                return new ApiResponse<>(true, "Ticket updated successfully", HttpStatus.OK, ticket, ZonedDateTime.now());
            }
        }
        return new ApiResponse<>(false, "Ticket not found", HttpStatus.NOT_FOUND, null, ZonedDateTime.now());
    }

    @DeleteMapping("/{ticket-id}")
    public ApiResponse<String> deleteTicketById(@PathVariable("ticket-id") Long ticketId) {
        for (Ticket ticket : TICKETS) {
            if (ticket.getTicketId().equals(ticketId)) {
                TICKETS.remove(ticket);
                return new ApiResponse<>(true, "Deleted successfully", HttpStatus.OK, "Deleted successfully", ZonedDateTime.now());
            }
        }
        return new ApiResponse<>(false, "Ticket not found", HttpStatus.NOT_FOUND, null, ZonedDateTime.now());
    }

    @GetMapping("/search")
    public ApiResponse<List<Ticket>> getTicketByPassengerId(@RequestParam String name) {
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket : TICKETS) {
            if (ticket.getPassengerName().toLowerCase().contains(name.toLowerCase())) {
                tickets.add(ticket);
            }
        }
        return new ApiResponse<>(true, "Search results retrieved", HttpStatus.OK, tickets, ZonedDateTime.now());
    }

    @PostMapping("/bulk")
    public ApiResponse<List<Ticket>> bulkCreate(@RequestBody List<TicketRequest> ticketRequests) {
        for (TicketRequest ticketRequest : ticketRequests) {
            Ticket ticket = new Ticket(TICKET_ID.getAndIncrement(), ticketRequest.getPassengerName(), ticketRequest.getTravelDate(), ticketRequest.getSourceStation(), ticketRequest.getDestinationStation(), ticketRequest.getPrice(), ticketRequest.getPaymentStatus(), ticketRequest.getTicketStatus(), ticketRequest.getSeatNumber());
            TICKETS.add(ticket);
        }
        return new ApiResponse<>(true, "Tickets created successfully", HttpStatus.CREATED, TICKETS, ZonedDateTime.now());
    }

    @PutMapping("/bulk")
    public ApiResponse<List<Ticket>> bulkUpdate(@RequestBody List<UpdatePaymentStatusRequest> ticketRequests) {
        for (UpdatePaymentStatusRequest ticketRequest : ticketRequests) {
            for (Ticket ticket : TICKETS) {
                if (ticket.getTicketId().equals(ticketRequest.getTicketId())) {
                    ticket.setPaymentStatus(ticketRequest.getPaymentStatus());
                }
            }
        }
        return new ApiResponse<>(true, "Payment statuses updated", HttpStatus.OK, TICKETS, ZonedDateTime.now());
    }

    @GetMapping("/filter")
    public ApiResponse<List<Ticket>> filterTicketStatus(@RequestParam Status ticketStatus, @RequestParam String travelDate) {
        List<Ticket> tickets = new ArrayList<>();
        ZonedDateTime parsedDate = ZonedDateTime.parse(travelDate + "T00:00:00Z");

        for (Ticket ticket : TICKETS) {
            if (ticket.getTicketStatus() == ticketStatus && ticket.getTravelDate().toLocalDate().equals(parsedDate.toLocalDate())) {
                tickets.add(ticket);
            }
        }
        return new ApiResponse<>(true, "Filtered tickets retrieved", HttpStatus.OK, tickets, ZonedDateTime.now());
    }
}
