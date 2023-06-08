import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchivedUsersModalComponent } from './archived-users-modal.component';

describe('ArchivedUsersModalComponent', () => {
  let component: ArchivedUsersModalComponent;
  let fixture: ComponentFixture<ArchivedUsersModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArchivedUsersModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArchivedUsersModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
