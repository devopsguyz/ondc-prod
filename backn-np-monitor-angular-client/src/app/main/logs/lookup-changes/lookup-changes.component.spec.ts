import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LookupChangesComponent } from './lookup-changes.component';

describe('LookupChangesComponent', () => {
  let component: LookupChangesComponent;
  let fixture: ComponentFixture<LookupChangesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LookupChangesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LookupChangesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
