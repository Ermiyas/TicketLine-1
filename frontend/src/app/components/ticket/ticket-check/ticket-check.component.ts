import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {AuthService} from '../../../services/auth/auth.service';

import {Customer} from '../../../dtos/customer';
import {Show} from '../../../dtos/show';
import {Seat} from '../../../dtos/seat';
import {Sector} from '../../../dtos/sector';
import {TicketService} from '../../../services/ticket/ticket.service';
import {TicketStatus} from '../../../datatype/ticket_status';
import {TicketPost} from '../../../dtos/ticket-post';
import {Ticket} from '../../../dtos/ticket';

@Component({
  selector: 'app-ticket-check',
  templateUrl: './ticket-check.component.html',
  styleUrls: ['./ticket-check.component.scss']
})
export class TicketCheckReservationComponent implements OnInit {
  error: boolean = false;
  errorMessage: string = '';

  private ticket_seats: Seat[] = [];
  private ticket_sectors: Sector[] = [];
  private ticket_show: Show;
  private ticket_customer: Customer;
  private ticket_status: TicketStatus;
  private submitted: boolean;

  private priceTotal: number;

  private tickets: TicketPost[] = [];
  private createdTickets: Ticket[];
  private seatsStr: String[] = [];
  private rowStr: String[] = [];
  private sectorStr: String [] = [];
  private statusStr: String;
  private idx: number;
  private amtTickets: number;

  constructor(private ticketService: TicketService, private ngbPaginationConfig: NgbPaginationConfig,
              private cd: ChangeDetectorRef, private authService: AuthService) {}

  ngOnInit() {
    this.ticket_seats = [];
    this.ticket_sectors = [];
    this.submitted = false;

    if (this.ticket_status === TicketStatus.RESERVATED) {
      this.statusStr = 'Reservation';
    } else {
      this.statusStr = 'Purchase';
    }
    this.idx = 0;
    this.priceTotal = 0;
    if (this.ticket_seats && this.ticket_seats.length > 0) {
      this.amtTickets = this.ticket_seats.length;
      for (const entry of this.ticket_seats) {
        this.seatsStr.push(entry.seatNumber.toString());
        this.rowStr.push(entry.seatRow.toString());
        this.priceTotal += entry.price;
      }
    }
    if (this.ticket_sectors && this.ticket_sectors.length > 0) {
      this.amtTickets = this.ticket_sectors.length;
      for (const entry of this.ticket_sectors) {
        this.sectorStr.push(entry.sectorNumber.toString());
        this.priceTotal += entry.price;
      }
    }
  }

  /**
   * Create all tickets given as TicketPost list.
   * @param tickets tickets to be created
   */
  createTicket() {
    this.tickets = [];
    if (this.ticket_seats && this.ticket_seats.length > 0) {
      for (const entry of this.ticket_seats) {
        const currentTicket = new TicketPost(null, this.ticket_show.id, this.ticket_customer.id, entry.price, entry.id, null,
          this.ticket_status);
        this.tickets.push(currentTicket);
      }
    }

    if (this.ticket_sectors && this.ticket_sectors.length > 0) {
      for (const entry of this.ticket_sectors) {
        const currentTicket = new TicketPost(null, this.ticket_show.id, this.ticket_customer.id, entry.price, null, entry.id,
          this.ticket_status);
        this.tickets.push(currentTicket);
      }
    }
    this.vanishError();
    this.addTicket(this.tickets);
  }

  /**
   * Sends news creation request
   * @param news the news which should be created
   */
  addTicket(tickets: TicketPost[]) {
    if (tickets.length > 0) {
      this.ticketService.createTicket(tickets).subscribe(
        (newTickets: Ticket[]) => {
          this.createdTickets = newTickets;
          this.submitted = true;
        },
        error => {
          this.defaultServiceErrorHandling(error);
        }
      );
    } else {
      console.log('No item in tickets');
    }
  }


  /**
   * Returns true if the authenticated user is an admin
   */
  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.ticketCheckReservation !== 'No message available') {
      this.errorMessage = error.error.ticketCheckReservation;
    } else {
      this.errorMessage = error.error.error;
    }
    if (error.error.status === 'BAD_REQUEST') {
      this.errorMessage = error.error.message.replace('400 BAD_REQUEST \"', '');
      this.errorMessage = this.errorMessage.replace('\"', '');
    } else if (error.error.status === 'NOT_FOUND') {
      this.errorMessage = error.error.message.replace('404 NOT_FOUND \"', '');
      this.errorMessage = this.errorMessage.replace('\"', '');
    } else if (error.error.status === 'INTERNAL_SERVER_ERROR') {
      this.errorMessage = error.error.message.replace('404 INTERNAL_SERVER_ERROR \"', '');
      this.errorMessage = this.errorMessage.replace('\"', '');
    }
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }
}
