import { Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';

import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { CoreConfigService } from '@core/services/config.service';
import { AuthenticationService } from 'app/auth/service';
import { environment } from 'environments/environment';

@Component({
  selector: 'horizontal-layout',
  templateUrl: './horizontal-layout.component.html',
  styleUrls: ['./horizontal-layout.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HorizontalLayoutComponent implements OnInit, OnDestroy {
  coreConfig: any;
  menuFlag= environment.uiRole === 'domain' ? false:true;
  // Private
  private _unsubscribeAll: Subject<any>;

  /**
   * Constructor
   *
   * @param {CoreConfigService} _coreConfigService
   */
  constructor(private _coreConfigService: CoreConfigService,public authService:AuthenticationService) {
    // Set the private defaults
    this._unsubscribeAll = new Subject();
  }

  // Lifecycle Hooks
  // -----------------------------------------------------------------------------------------------------

  /**
   * On init
   */
  ngOnInit(): void {
    // Subscribe to config changes
    this._coreConfigService.config.pipe(takeUntil(this._unsubscribeAll)).subscribe(config => {
      this.coreConfig = config;
    });
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    // Unsubscribe from all subscriptions
    this._unsubscribeAll.next();
    this._unsubscribeAll.complete();
  }
}
