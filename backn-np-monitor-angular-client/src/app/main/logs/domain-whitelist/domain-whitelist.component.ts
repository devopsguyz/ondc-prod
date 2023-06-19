import {
  Component,
  OnInit,
  TemplateRef,
  ViewChild,
  ViewEncapsulation,
} from "@angular/core";
import { locale as en } from "../i18n/en";
import { locale as fr } from "../i18n/fr";
import { locale as de } from "../i18n/de";
import { locale as pt } from "../i18n/pt";
import { CoreTranslationService } from "@core/services/translation.service";
import { ColumnMode } from "@swimlane/ngx-datatable";
import {
  ModalDismissReasons,
  NgbDateParserFormatter,
  NgbModal,
} from "@ng-bootstrap/ng-bootstrap";
import { FormBuilder, FormGroup } from "@angular/forms";
import { ApiAuditLogsService } from "app/main/service/api.audit.logs.servce";
import { ToastrService } from "ngx-toastr";
import { ApiOnboardingServiceService } from "app/main/service/api-onboarding-service.service";
import { DomainDto } from "app/main/model/response.model";
import { Domain } from "domain";
import { STATUS_CODES } from "http";
import { HttpErrorResponse } from "@angular/common/http";

@Component({
  selector: "app-domain-whitelist",
  templateUrl: "./domain-whitelist.component.html",
  styleUrls: ["./domain-whitelist.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class DomainWhitelistComponent implements OnInit {
  public ColumnMode = ColumnMode;
  closeResult: string = "";
  form: FormGroup;

  rows: any = [];
  crows: any = [];
  totalCount: Number = 0;
  dataParams: any = {
    page_num: "",
    page_size: "",
  };
  domainDto: DomainDto;
  @ViewChild("closebutton") closebutton;
  @ViewChild("mymodal") templateRef: TemplateRef<any>;
  /**
   *
   * @param {CoreTranslationService} _coreTranslationService
   */
  constructor(
    private _coreTranslationService: CoreTranslationService,
    public formatter: NgbDateParserFormatter,
    private formBuilder: FormBuilder,
    private apiAuditLogsService: ApiAuditLogsService,
    private apiOnboardingServiceService: ApiOnboardingServiceService,
    private toastr: ToastrService,
    private modalService: NgbModal
  ) {
    this._coreTranslationService.translate(en, fr, de, pt);
    this.form = formBuilder.group({
      uuid: "",
      domain: "",
      allow: false,
    });
  }

  @ViewChild("tableRowDetails")
  tableRowDetails: any;

  onGetRowClass = (row) => {
    if (row.flag) {
      return "color-transaction-row-red";
    } else {
      // return "color-transaction-row-green";
    }
  };
  ngOnInit(): void {
    this.dataParams.page_num = 1;
    this.dataParams.page_size = 5;
    this.getAllRows();
  }

  getAllRows() {
    console.log("Calling api");
    this.apiOnboardingServiceService.getAllDomain().subscribe((item) => {
      console.log("get response from the api");
      console.log(item);
      this.rows = [];
      if (item) {
      this.rows=item;
      }
      this.crows=this.rows;
      console.log(this.rows);
    });
    this.totalCount = this.rows.length;
  }

  filterUpdate(event) {
    const val = event.target.value.toLowerCase();

    let tempData: Array<Map<string, any>> = this.crows;

    // filter our data
    const temp = tempData.filter((d: any) => {
      let flag = !val;
      //console.log(d.toString());
      if (!flag) {
        Object.keys(d).forEach((item) => {
          try {
            flag = flag || d[item].toLowerCase().indexOf(val) !== -1;
          } catch (error) {}
        });
      }
      return flag;
    });

    // update the rows
   
      this.rows = temp;
      this.tableRowDetails.offset = 0;
    
    // Whenever the filter changes, always go back to the first page
  }

  /**
   * Write code on Method
   *
   * @return response()
   */
  open(content: TemplateRef<any>) {
    this.form.controls["domain"].setValue("");
    this.form.controls["allow"].setValue(false);
    this.modalService
      .open(content, { ariaLabelledBy: "staticBackdrop" })
      .result.then(
        (result) => {
          console.log(result);
          this.closeResult = `Closed with: ${result}`;
        },
        (reason) => {
          this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
        }
      );
  }
  /**
   * Write code on Method
   *
   * @return response()
   */
  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return "by pressing ESC";
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return "by clicking on a backdrop";
    } else {
      return `with: ${reason}`;
    }
  }
  showToaster() {
    //debugger;
    let value = this.form.controls.domain.value;
    console.log(this.form.controls.uuid.value);
    if (value.trim() != "") {
      this.domainDto = this.form.value;
      this.apiOnboardingServiceService.saveDomain(this.domainDto).subscribe(
        (response: any) => {
          console.log("Save Domain " + response.status);
          this.toastr.success("Save Sucessfully.", "Success!", {
            progressBar: true,
          });
          this.getAllRows();
          this.form.reset();
          this.modalService.dismissAll();
        },
        (err: HttpErrorResponse) => {
          this.toastr.error(err.error, "Error!", {
            closeButton: true,
            progressBar: true,
          });
        }
      );

      //
    } else {
      this.toastr.error("Please enter Domain Name.", "Error!", {
        closeButton: true,
        progressBar: true,
      });
    }
  }

  edit(content) {
    console.log("Add Delete Action code here" + content.uuid);
    this.form.controls["uuid"].setValue(content.uuid);
    this.form.controls["domain"].setValue(content.domain);
    this.form.controls["allow"].setValue(content.allow);
    this.modalService.open(this.templateRef, {});
  }
}
