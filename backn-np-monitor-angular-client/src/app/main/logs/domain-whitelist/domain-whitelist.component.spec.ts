import { SimpleChange } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { switchAll } from 'rxjs/operators';

import { DomainWhitelistComponent } from './domain-whitelist.component';

describe('DomainWhitelistComponent', () => {
  let component: DomainWhitelistComponent;
  let fixture: ComponentFixture<DomainWhitelistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DomainWhitelistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DomainWhitelistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  
});
