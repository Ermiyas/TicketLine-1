import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../../global/globals';
import {Observable} from 'rxjs';
import {User} from '../../dtos/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userBaseUri: string = this.globals.backendUri + '/users';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  /**
   * Loads all users from the backend
   */
  getAllUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(this.userBaseUri);
  }

  /**
   * Loads specific user from the backend
   * @param userId of user to load
   */
  getUserById(userId: number): Observable<User> {
    console.log('Load user details for ' + userId);
    return this.httpClient.get<User>(this.userBaseUri + '/' + userId);
  }

  /**
   * Persists user to the backend
   * @param user to persist
   */
  createUser(user: User): Observable<User> {
    console.log('Create user with username ' + user.username);
    return this.httpClient.post<User>(this.userBaseUri, user);
  }

  /**
   * Deletes the user with the specified id
   * @param userId id of user to delete
   */
  deleteUser(userId: number): Observable<{}> {
    console.log('Delete user with id ' + userId);
    return this.httpClient.delete(this.userBaseUri + '/' + userId);
  }

}
