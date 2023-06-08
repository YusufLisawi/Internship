import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-new-option',
  templateUrl: './new-option.component.html',
  styleUrls: ['./new-option.component.scss']
})
export class NewOptionComponent implements OnInit {

  @Input() public optionName:string;
  constructor(private activeModal: NgbActiveModal) { }

  ngOnInit(): void {
  }
  public closeModal() {
    this.activeModal.close(null);
  }
  public save(newvalue:string){
    this.activeModal.close(newvalue);
  }
}
