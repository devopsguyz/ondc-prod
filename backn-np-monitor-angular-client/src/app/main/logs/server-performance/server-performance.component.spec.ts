import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServerPerformanceComponent } from './server-performance.component';

describe('ServerPerformanceComponent', () => {
  let component: ServerPerformanceComponent;
  let fixture: ComponentFixture<ServerPerformanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ServerPerformanceComponent ]
    })
    .compileComponents();
  });
 
  beforeEach(() => {
    fixture = TestBed.createComponent(ServerPerformanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
