import { Component, Input, OnInit } from '@angular/core';
import { Score } from '@models/Score.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-score-details',
  templateUrl: './score-details.component.html',
  styleUrls: ['./score-details.component.scss']
})
export class ScoreDetailsComponent implements OnInit {

  @Input() score : Score;

  constructor(private activeModal : NgbActiveModal) { }

  ngOnInit(): void {
    console.log(this.score);
    
  }
 
  closeModal(){
    this.activeModal.close();
  } 

}
