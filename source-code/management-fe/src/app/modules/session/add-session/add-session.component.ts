import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Candidate } from '@models/Candidate.model';
import { Session } from '@models/Session.model';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CandidateService } from '@services/candidate.service';
import { SessionService } from '@services/session.service';
import { forkJoin, of } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import Swal from 'sweetalert2';

@Component({
  selector: "app-addsession",
  templateUrl: "./add-session.component.html",
  styleUrls: [`./add-session.component.scss`],
})
export class AddsessionComponent implements OnInit {
  page = 1;
  pageSize = 5;
  collectionSize: number;
  public selectedRow: Session = new Session("", new Date(), "", "");
  public currentSession: Session = new Session("", new Date(), "", "");
  addReactiveForm: UntypedFormGroup = this.formBuilder.group({
    sessionName: [
      "",
      Validators.compose([Validators.required, Validators.maxLength(20)]),
    ],
    technology: [
      "",
      Validators.compose([Validators.required, Validators.maxLength(20)]),
    ],
    sessionDate: ["", Validators.compose([Validators.required])],
  });
  errMessage: string;
  formSubmited: boolean;
  public technologies: string[] = [];
  public searchText: string;
  public sessionDate: Date;
  public technology: string;
  private sessions: Session[] = [];
  public sessionsFiltered: Session[];
  waitingList: Candidate[] = [];
  public isInternship: boolean;

  constructor(
    private activeModal: NgbActiveModal,
    private sessionService: SessionService,
    private router: Router,

    private formBuilder: UntypedFormBuilder,
    private candidateService: CandidateService
  ) {}

  ngOnInit(): void {
    this.sessionService.getAllTechnologies().subscribe((data: string[]) => {
      this.technologies = data;
    });
    if (
      this.router.routerState.snapshot.root.children[0].url[0].path ==
      "internship"
    ) {
      this.isInternship = true;
      this.addReactiveForm.get("technology").clearValidators();
      this.addReactiveForm.get("technology").setValue("Internship");
      this.addReactiveForm.get("technology").disable();
    } else {
      this.isInternship = false;
      this.sessionService
        .getSessionsByStatus("current")
        .subscribe((session) => {
          if (session){
            this.candidateService.getWaitingList().subscribe({
              next: (res) => {
                this.waitingList = res;                
              },
            });
          }
        });
    }
  }

  public addSession(): void {
    this.errMessage = "";
    this.formSubmited = true;
    if (this.addReactiveForm.valid) {
      let session: Session = {
        sessionName: this.addReactiveForm.value.sessionName,
        sessionDate: this.addReactiveForm.value.sessionDate,
        admittedNumber: null,
        dayInterviewNumber: null,
        technology: this.isInternship
          ? "Internship"
          : this.addReactiveForm.value.technology,
        sessionStatus: "current",
        recruiter: null,
      };
      session.dayInterviewNumber =
        this.currentSession?.dayInterviewNumber || 100;
      session.admittedNumber = this.currentSession?.admittedNumber || 20;
      forkJoin({
        updateSessionPrevious: this.sessionService.updateSessionPrevious(),
        updateSessionClose: this.sessionService.updateSessionClose(),
        add: this.sessionService.add(session),
      })
       .pipe(
        switchMap(res => {
          if(this.waitingList && this.waitingList.length > 0){
            return this.sessionService.getSessionsByStatus("current").pipe(
            switchMap(sessions => {
              const updateObservables = this.waitingList.map(candidate =>
                this.candidateService.moveWaitingListToSession(candidate, sessions.sessionId)
              );
              return forkJoin(updateObservables);
            })
          );
          } else {
            return of(null); 
          }
        })
      ) .subscribe({
        next: res => {
          this.refreshCandidates();
          if(this.router.routerState.snapshot.root.children[0].url[0].path === 'beca'){
            Swal.fire({
              title: 'Succes!',
              text:'Session created successfully. Please navigate to the settings page to fix your session settings.',
              icon: 'success',
              iconColor: '#6485C1',
              showCancelButton: true,
              confirmButtonText: 'Go to Settings',
              cancelButtonText: 'Cancel',
            }).then((result) => {
              if(result.isConfirmed){
                this.router.navigate(['/beca/settings']);
              }
            });
          }else{
            Swal.fire({
              title: "Succes!",
              icon: "success",
              iconColor: "#6485C1",
              showConfirmButton: false,
              timer: 1500,
            });
          }
          this.errMessage = "";
          this.addReactiveForm.reset();
          document.getElementById('cheat').click();
          this.formSubmited = false;
        },
        error: err => {
          this.errMessage = err.error.message
          this.formSubmited = false;
        } 
      });
    }

  }
  private refreshCandidates() {
    this.sessionService.getSessionsByStatus('current').subscribe(
      data => this.currentSession = data
    );
    this.sessionService.getAll().subscribe((data: Session[]) => {
      this.collectionSize = data.length;
      this.sessions = data;
      this.sessionsFiltered = data.map((candidates, i) => ({ id: i + 1, ...candidates }))
        .slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
    })
  }

  closeModal() {
    this.activeModal.close(true);
  }
}
