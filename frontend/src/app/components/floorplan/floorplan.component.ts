import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Seat} from '../../dtos/seat';
import {Sector} from '../../dtos/sector';
import {PriceCategory} from '../../dtos/priceCategory';

@Component({
  selector: 'app-floorplan',
  templateUrl: './floorplan.component.html',
  styleUrls: ['./floorplan.component.scss']
})
export class FloorplanComponent implements OnInit {
  @Input() private seats?: Seat[];
  @Input() private sectors?: Sector[];
  @Output() private selectedSeat: EventEmitter<{eventTarget: HTMLElement, seat: Seat}> = new EventEmitter();
  @Output() private selectedSector: EventEmitter<{eventTarget: HTMLElement, sector: Sector}> = new EventEmitter();
  private viewboxPosX: number = 0;
  private viewboxPosY: number = 0;
  private viewboxWidth: number = 500;
  private viewbox: string = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;

  constructor() {
  }

  private selectSeat(event: Event, seat: Seat): void {
    this.selectedSeat.emit({eventTarget: <HTMLElement> event.target, seat: seat});
  }

  private selectSector(event: Event, sector: Sector): void {
    this.selectedSector.emit({eventTarget: <HTMLElement> event.target, sector: sector});
  }


  /**
   * gets string for d attribute of svg path element depicting a seat
   * @param seat for which to return path attribute
   */
  private getSeatPath(seat: Seat): string {
    const xPos: number = (seat.seatNumber - 1) * 1.2 * 10 + Math.floor(seat.seatNumber / 15) * 10;
    const yPos: number = seat.seatRow * 1.2 * 10 + Math.floor(seat.seatRow / 10) * 10;
    return 'M' + xPos + ' ' + yPos + 'h 10 v 10 h -10 Z';
  }

  /**
   * gets string for d attribut of svg path element depicting a sector
   * @param sector for which to return path attribute
   */
  private getSectorPath(sector: Sector): string {
    const width = this.viewboxWidth / 3.2;
    const gap = this.viewboxWidth / 32;
    const xPos: number = ((sector.sectorNumber - 1) % 3) * (width + gap);
    const yPos: number = Math.floor((sector.sectorNumber - 1) / 3) * (50 + gap);
    return 'M' + xPos + ' ' + yPos + 'h ' + width + ' v 50 h ' + (-width) + ' Z';
  }

  /**
   * get color of seat or sector assed as parameter
   * color depends on the priceCategory of the seat
   * @param elem for which to return color
   */
  private getColor(elem: Seat | Sector): string {
    return elem.priceCategory === PriceCategory.Cheap ? '#2fb207' : (elem.priceCategory === PriceCategory.Average ? '#129ded' : '#db0611');
  }

  /**
   * allows users to zoom in by adjusting svg viewbox
   * @param event mousewheel event used to zoom
   */
  /*private zoom(event) {
    event.preventDefault();
    this.viewboxWidth = event.deltaY > 0 ? this.viewboxWidth * 1.05 : this.viewboxWidth / 1.05;
    this.viewboxWidth = this.viewboxWidth < 300 ? 300 : (this.viewboxWidth > 1310 ? 1310 : this.viewboxWidth);
    const rect = document.getElementById('floorplan').getBoundingClientRect();
    this.viewboxPosX = event.pageX - rect.left - this.viewboxWidth / 2;
    this.viewboxPosY = event.pageY - rect.top - this.viewboxWidth / 2;
    this.viewboxPosX = this.viewboxPosX < 0 ? 0 : this.viewboxPosX;
    this.viewboxPosY = this.viewboxPosY < 0 ? 0 : this.viewboxPosX;
    this.viewbox = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;
  }*/

  ngOnInit() {
  }
}