import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SellerBuyerComponent } from './seller-buyer.component';

describe('SellerBuyerComponent', () => {
  let component: SellerBuyerComponent;
  let fixture: ComponentFixture<SellerBuyerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SellerBuyerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SellerBuyerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
