import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdmittedListComponent } from './admitted-list.component';

describe('AdmittedListComponent', () => {
  let component: AdmittedListComponent;
  let fixture: ComponentFixture<AdmittedListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdmittedListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdmittedListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
