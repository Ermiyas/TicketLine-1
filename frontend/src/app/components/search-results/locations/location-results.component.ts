import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {LocationResultsService} from '../../../services/search-results/locations/location-results.service';
import {Location} from '../../../dtos/location';

// TODO remove all console.log()

@Component({
  selector: 'app-locations',
  templateUrl: './location-results.component.html',
  styleUrls: ['./location-results.component.scss']
})
export class LocationResultsComponent implements OnInit {

  private page: number = 1;
  private pageSize: number = 10;
  private dataReady: boolean = false;
  private error: boolean = false;
  private errorMessage: string = '';
  private resultsFor: string;
  private locations: Location[];
  private headers: string[] = [
    'Country',
    'City',
    'Postcode',
    'Street',
    'Description'
  ];

  constructor(private route: ActivatedRoute, private locationsService: LocationResultsService) { }

  ngOnInit() {
    this.resultsFor = this.route.snapshot.queryParamMap.get('resultsFor');
    if (this.resultsFor === 'ATTRIBUTES') {
      this.loadLocationsFiltered(
        this.route.snapshot.queryParamMap.get('country'),
        this.route.snapshot.queryParamMap.get('city'),
        this.route.snapshot.queryParamMap.get('street'),
        this.route.snapshot.queryParamMap.get('postalCode'),
        this.route.snapshot.queryParamMap.get('description')
      );
    } else {
      this.defaultServiceErrorHandling('No results for this type');
    }
  }

  private loadLocationsFiltered(country, city, street, postalCode, description) {
    console.log('Location Component: loadLocationsFiltered');
    this.locationsService.findLocationsFiltered(country, city, street, postalCode, description).subscribe(
      (locations: Location[]) => { this.locations = locations; },
      error => {this.defaultServiceErrorHandling(error); },
          () => { this.dataReady = true; }
    );
  }

  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (error.error.news !== 'No message available') {
      this.errorMessage = error.error.news;
    } else {
      this.errorMessage = error.error.error;
    }
  }
}
