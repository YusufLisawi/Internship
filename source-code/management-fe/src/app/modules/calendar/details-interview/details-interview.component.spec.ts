import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsInterviewComponent } from './details-interview.component';

describe('DetailsInterviewComponent', () => {
  let component: DetailsInterviewComponent;
  let fixture: ComponentFixture<DetailsInterviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailsInterviewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailsInterviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
