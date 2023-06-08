import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
// import { Sort } from '@angular/material/sort';
import { ActivatedRoute } from '@angular/router';
import { Candidate } from '@models/Candidate.model';
import { Technology } from '@models/Technologie.model';
import { CandidateService } from '@services/candidate.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-candidate',
  templateUrl: './candidate.component.html',
  styleUrls: ['./candidate.component.scss']
})
export class CandidateComponent implements OnInit {

  public backUrl: string = environment.apiUrl;

  page = 1;
  pageSize = 15;
  collectionSize: number;
  closeResult: string;
  level: string;
  city: string;
  searchText: string;
  sessionId: number;
  public isFilterActive: boolean = false;
  private candidates: Candidate[] = [];
  public candidatesFiltered: Candidate[] = [];
  public techs: string[] = Object.values(Technology).filter(value => typeof value === 'string' && value !== Technology.Default);
  candidatForm: UntypedFormGroup = this.formbuil.group({
    FIRSTNAME: ['', Validators.required],
    LASTNAME: ['', Validators.required],
    PHONE: ['', Validators.required],
    MAIL: ['', Validators.required],
    UNIVERSITY: ['', Validators.required],
    STUDIES: ['', Validators.required],
    CITY: ['', Validators.required],
    ALLSESSION: ['', Validators.required],
    CV: ['', Validators.required],
  });
  public asc :boolean=true;
  public orderBy: string;
  levelOfStudy: string;
  university: string;
  public universities: string[] = [];
  public cities: string[] = [];
  lvls: string[] = ["BAC+2", "BAC+3", "BAC+5", "BAC+8"];

  constructor(private route: ActivatedRoute, private candidateService: CandidateService, private formbuil: UntypedFormBuilder, private location: Location) { }

  ngOnInit() {
    this.sessionId = Number(this.route.snapshot.paramMap.get('id'));
    this.refreshCandidates();
    this.candidateService.getUniversities().subscribe((data) =>{
      this.universities=data;
    });
    this.candidateService.getCities().subscribe((data) =>{
      this.cities=data;
    });
  }

  sortData(sort: any) {
    const data = this.candidates.slice();
    if (!sort.active || sort.direction === '') {
      this.candidates = data;
      return;
    }

    this.candidates = data.sort((a, b) => {
      const isDesc = sort.direction === 'desc';
      switch (sort.active) {
        case 'firstName':
          return this.compare(a.firstName, b.firstName, isDesc);
        case 'lastName':
          return this.compare(a.lastName, b.lastName, isDesc);
        case 'email':
          return this.compare(a.email, b.email, isDesc);
        case 'city':
          return this.compare(a.city, b.city, isDesc);
        default:
          return 0;
      }
    });
  }

  private refreshCandidates() {
    this.candidateService.getBySession(this.sessionId).subscribe(
      (data: Candidate[]) => {
        this.collectionSize = data.length;
        this.candidates = data;
        this.candidatesFiltered = data.map((candidates, i) => ({ id: i + 1, ...candidates }))
          .slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
      }
    );
  }

  public changeNavigation() {
    this.candidatesFiltered = this.candidates.map((candidates, i) => ({ id: i + 1, ...candidates }))
      .slice((this.page - 1) * this.pageSize, (this.page - 1) * this.pageSize + this.pageSize);
  }

  goBack(){
    this.location.back();
  }

  private compare(a: number | string, b: number | string, isDesc: boolean) {
    return (a < b ? -1 : 1) * (isDesc ? 1 : -1);
  }

  changeOrder(column: string){
    if(this.orderBy==column)
      this.asc=!this.asc;
    else
      this.orderBy=column;
  }

}