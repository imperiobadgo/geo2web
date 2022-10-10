import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-algebra-input',
  templateUrl: './algebra-input.component.html',
  styleUrls: ['./algebra-input.component.scss']
})
export class AlgebraInputComponent implements OnInit {

  input: string = '';
  floatLabel: string = 'Enter input';

  constructor() {
  }

  ngOnInit(): void {
  }

  onEnter(): void {

  }

}
