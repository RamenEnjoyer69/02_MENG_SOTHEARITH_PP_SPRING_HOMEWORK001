package com.homework.controller;

import com.homework.model.entity.Status;
import com.homework.model.entity.Ticket;
import com.homework.model.request.TicketRequest;
import com.homework.model.request.UpdatePaymentStatusRequest;
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
    public List<Ticket> getAllTickets(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        int start = (page - 1) * size;
        int end = Math.min(start + size, TICKETS.size());

        if (start >= TICKETS.size()) {
            return new ArrayList<>(); // return empty list if page is out of bounds
        }
        return TICKETS.subList(start, end);
    }

    @PostMapping("/add")
    public Ticket saveTicket(@RequestBody TicketRequest request) {
        Ticket ticket = new Ticket(TICKET_ID.getAndIncrement(), request.getPassengerName(), request.getTravelDate(), request.getSourceStation(), request.getDestinationStation(), request.getPrice(), request.getPaymentStatus(), request.getTicketStatus(), request.getSeatNumber());
        TICKETS.add(ticket);
        return ticket;
    }
    @GetMapping("/{ticket-id}")
    public Ticket getTicketById(@PathVariable("ticket-id") Long ticketId) {
        for (Ticket ticket : TICKETS) {
            if (ticket.getTicketId().equals(ticketId)) {
                return ticket;
            }
        }
        return null;
    }
    @PutMapping("/{ticket-id}")
    public Ticket updateTicketById(@PathVariable("ticket-id") Long ticketId, @RequestBody TicketRequest request) {
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
                return ticket;
            }
        }

        return null;
    }
    @DeleteMapping("/{ticket-id}")
    public String deleteTicketById(@PathVariable("ticket-id") Long ticketId) {
        for (Ticket ticket : TICKETS) {
            if (ticket.getTicketId().equals(ticketId)) {
                TICKETS.remove(ticket);
                return "Deleted successfully";
            }
        }
        return null;
    }
    @GetMapping("/search")
    public List<Ticket> getTicketByPassengerId(@RequestParam String name) {
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket : TICKETS) {
            if (ticket.getPassengerName().toLowerCase().contains(name.toLowerCase())) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }
    @PostMapping("/bulk")
    public List<Ticket> bulkCreate(@RequestBody List<TicketRequest> ticketRequests) {
        List<Ticket> tickets = new ArrayList<>();
        for (TicketRequest ticketRequest : ticketRequests) {
            Ticket ticket = new Ticket(TICKET_ID.getAndIncrement(), ticketRequest.getPassengerName(), ticketRequest.getTravelDate(), ticketRequest.getSourceStation(), ticketRequest.getDestinationStation(), ticketRequest.getPrice(), ticketRequest.getPaymentStatus(), ticketRequest.getTicketStatus(), ticketRequest.getSeatNumber());
            TICKETS.add(ticket);
        }
        return TICKETS;
    }
    @PutMapping("/bulk")
    public List<Ticket> bulkUpdate(@RequestBody List<UpdatePaymentStatusRequest> ticketRequests) {
        List<Ticket> tickets = new ArrayList<>();
        for (UpdatePaymentStatusRequest ticketRequest : ticketRequests) {
            for (Ticket ticket : TICKETS) {
                if (ticket.getTicketId().equals(ticketRequest.getTicketId())) {
                    ticket.setPaymentStatus(ticketRequest.getPaymentStatus());
                }
            }
        }
        return TICKETS;
    }
    @GetMapping("/filter")
    public List<Ticket> filterTicketStatus(@RequestParam Status ticketStatus, @RequestParam String travelDate) {
        List<Ticket> tickets = new ArrayList<>();
        LocalDate parsedDate = LocalDate.parse(travelDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")); //gotta use formatter cuz im using zoned date time

        for (Ticket ticket : TICKETS) {
            // convert ZonedDateTime to LocalDate for comparison
            if (ticket.getTicketStatus() == ticketStatus && ticket.getTravelDate().toLocalDate().equals(parsedDate)) {
                tickets.add(ticket);
            }
        }
        return tickets;
    }
}
