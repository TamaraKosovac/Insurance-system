import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-insurances',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './insurances.component.html',
  styleUrls: ['./insurances.component.css']
})
export class InsurancesComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {

  }
}
