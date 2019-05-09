import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Customer} from '../dtos/customer';
import {Globals} from '../global/globals';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private customerBaseUri: string = this.globals.backendUri + '/customers';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Persists customer to the backend
   * @param customer to persist
   */
  createCustomer(customer: Customer): Observable<Customer> {
    console.log('Create customer with name ' + customer.name);
    return this.httpClient.post<Customer>(this.customerBaseUri, customer);
  }

  /**
   * updates specified customer in backend
   * @param customer updated customer dto
   */
  updateCustomer(customer: Customer): Observable<Customer> {
    console.log('Update customer with id ' + customer.id + ' to ' + JSON.stringify(customer));
    return this.httpClient.put<Customer>(this.customerBaseUri + '/' + customer.id, customer);
  }

  /**
   * returns all customers found in the backend database as Observable
   */
  findAllCustomers(): Observable<Customer[]> {
    console.log('Getting all customers from backend');
    return this.httpClient.get<Customer[]>(this.customerBaseUri);
  }

  /**
   * Gets all customers from backend that fit search parameters
   * @param customer dto containing search parameters
   */
  searchCustomers(customer: Customer): Observable<Customer[]> {
    console.log('Getting all customers from search parameters: ' + JSON.stringify(customer));
    let parameters = new HttpParams();
    parameters = customer.id ? parameters.append('id', customer.id.toString()) : parameters;
    parameters = customer.name ? parameters.append('name', customer.name.trim()) : parameters;
    parameters = customer.firstname ? parameters.append('firstname', customer.firstname.trim()) : parameters;
    parameters = customer.email ? parameters.append('email', customer.email.trim()) : parameters;
    if (customer.birthday) {
      const date: string[] = customer.birthday.split('-');
      parameters = parameters.append('birthday', date[2] + '.' + date[1] + '.' + date[0]);
    }
    return this.httpClient.get<Customer[]>(this.customerBaseUri, {params: parameters});
  }
}
