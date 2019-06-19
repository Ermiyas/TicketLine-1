import {Component, OnInit} from '@angular/core';
import {Customer} from '../../dtos/customer';
import {CustomerService} from '../../services/customer.service';
import {AuthService} from '../../services/auth/auth.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss'],
  styles: ['#errorMessage{z-index: 9999; }']
})
export class CustomerComponent implements OnInit {

  private page: number = 0;
  private pages: Array<number>;
  private dataReady: boolean = false;

  private customers: Customer[];
  private customerFiltered: Customer;
  private activeCustomer: Customer;
  private error: boolean = false;
  private errorMessage: string;

  constructor(private customerService: CustomerService, private authService: AuthService) {
  }

  /**
   * Returns true if user is logged in as admin
   * determines whether user is allowed to add customers or update existing customer
   */
  private isAdmin() {
    return this.authService.getUserRole() === 'ADMIN';
  }

  /**
   * uses CustomerService to load all customers from backend
   */
  private loadCustomers() {
    console.log('Queries customerService to load all customers');
    this.customerService.findAllCustomers(this.page).subscribe(
        result => {
          this.customers = result['content'];
          this.pages = new Array(result['totalPages']);
      },
      error => this.defaultServiceErrorHandling(error),
      () => { this.dataReady = true; }
    );
  }

  /**
   * Resets all set parameters for customer and page number and calls 'loadCustomers'
   */
  private resetCustomerFilter() {
    this.customerFiltered = undefined;
    this.page = 0;
    this.loadCustomers();
  }

  /**
   * uses CustomerService to search for customers matching parameter
   * @param page number of the page to get
   * @param customer dto that includes search parameters for customerService
   */
  private searchCustomers(customer: Customer, page: number) {
    console.log('Queries customerService to search for customers resembling ' + JSON.stringify(customer));
    this.customerFiltered = customer;
    this.page = page;
    this.customerService.searchCustomers(this.customerFiltered, page).subscribe(
      result => {
        this.customers = result['content'];
        this.pages = new Array(result['totalPages']);
      },
      error => this.defaultServiceErrorHandling(error),
      () => { this.dataReady = true; }
    );
  }

  /**
   * Sets page number to the chosen i
   * @param i number of the page to get
   * @param event to handle
   */
  private setPage(i, event: any) {
    event.preventDefault();
    this.page = i;
    if (this.customerFiltered !== undefined) {
      this.searchCustomers(this.customerFiltered, this.page);
    } else {
      this.loadCustomers();
    }
  }

  /**
   * Sets page number to the previous one and calls the last method
   * @param event o handle
   */
  private previousPage(event: any) {
    event.preventDefault();
    if (this.page > 0 ) {
      this.page--;
      if (this.customerFiltered !== undefined) {
        this.searchCustomers(this.customerFiltered,  this.page);
      } else {
        this.loadCustomers();
      }
    }
  }

  /**
   * Sets page number to the next one and calls the last method
   * @param event to handle
   */
  private nextPage(event: any) {
    event.preventDefault();
    if (this.page < this.pages.length - 1) {
      this.page++;
      if (this.customerFiltered !== undefined) {
        this.searchCustomers(this.customerFiltered , this.page);
      } else {
        this.loadCustomers();
      }
    }
  }

  /**
   * sets specific customer as variable activeCustomer
   * @param customer to be set as activeCustomer
   */
  private setActiveCustomer(customer: Customer) {
    this.activeCustomer = customer;
  }

  /**
   * uses CustomerService to update customer passed as param, should be activeCustomer
   * @param customer to be updated
   */
  private updateCustomer(customer: Customer) {
    console.log('Updates custoemr with id ' + customer.id + ' to ' + JSON.stringify(customer));
    Object.assign(this.activeCustomer, customer);
    this.customerService.updateCustomer(customer);
  }

  /**
   * adds newly created customer to backend
   * @param customer to be added
   */
  private addCustomer(customer: Customer) {
    console.log('Adding customer: ' + JSON.stringify(customer));
    this.customerService.createCustomer(customer).subscribe(
      addedCustomer => this.customers.push(addedCustomer),
      error => this.defaultServiceErrorHandling(error)
    );
  }

  /**
   * activates error flag and sets error message to display to user
   * @param error that was encountered, includes error message
   */
  /* PINO: extended if clause in order to get into else if no error.error.news is available*/
  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.news !== 'No message available' && error.error.news !== '' && error.error.news) {
      this.errorMessage = error.error.news;
    } else {
      this.errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
  }

  /**
   * deactivates error flag, stops displaying error to user
   */
  private vanishError() {
    this.error = false;
  }

  /**
   * runs after component initialization
   * loads all customers from backend for tutorial purposes
   * TODO: either implement lazy loading or stop loading all customers
   */
  ngOnInit() {
    console.log('Customer Component was initialized!');
    this.loadCustomers();
  }
}
