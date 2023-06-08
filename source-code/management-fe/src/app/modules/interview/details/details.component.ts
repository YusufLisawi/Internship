import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { Candidate } from '@models/Candidate.model';
import { Score } from '@models/Score.model';
import { ScoreConfig } from '@models/ScoreConfig.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CandidateService } from '@services/candidate.service';
import { ScoreService } from '@services/score.service';
import { SettingsService } from '@services/settings.service';
import { environment } from 'src/environments/environment';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss']
})
export class DetailsComponent implements OnInit {

  scoreForm: UntypedFormGroup;
  score: Score = new Score('', 0, 0, '', 0, '', 0, '', '', 0);
  currentConfig: ScoreConfig;
  @Input() public candidate : Candidate;
  @Output() public isSaved : EventEmitter<any> = new EventEmitter();
  public src = "";

  constructor(private formBuilder: UntypedFormBuilder,private candidateService: CandidateService,private scoreService: ScoreService, private settingService: SettingsService, private activeModal: NgbActiveModal) { }

  ngOnInit(): void {
    this.scoreForm = this.formBuilder.group({
      'technicalDesc': [''],
      'technicalTestScore': [''],
      'technicalInterviewScore': [''],
      'peopleDesc': [''],
      'peopleScore': [''],
      'softSkillsDesc': [''],
      'softSkillsScore': [''],
      'englishLevel': [''],
      'spanishLevel': ['']
    });
    this.src = environment.apiUrl + 'file/find/'+ this.candidate.cvname;
    if(this.candidate.score !== null){
      this.fillScore(this.candidate.score);
    }
    this.settingService.getCurrent().subscribe({
      next: (data) => {
        this.currentConfig = data;
      }
    })
  }

  fillScore(score : Score) {
    if (score !== null) {
      this.scoreForm.get('technicalDesc').setValue(score.technicalDesc);
      this.scoreForm.get('technicalTestScore').setValue(score.technicalTestScore);
      this.scoreForm.get('technicalInterviewScore').setValue(score.technicalInterviewScore);
      this.scoreForm.get('peopleDesc').setValue(score.peopleDesc);
      this.scoreForm.get('peopleScore').setValue(score.peopleScore);
      this.scoreForm.get('softSkillsDesc').setValue(score.softSkillsDesc);
      this.scoreForm.get('softSkillsScore').setValue(score.softSkillsScore);
      this.scoreForm.get('englishLevel').setValue(score.englishLevel);
      this.scoreForm.get('spanishLevel').setValue(score.spanishLevel);
    }
  }

  addScore(score : Score, candidateId: number) {
    if (this.candidate.score === null) {
      score.finalScore = this.getFinalScore(score, this.currentConfig);
      this.candidateService.addScore(score, candidateId).subscribe({
        next: (res) => {
          this.isSaved.emit(true);
          this.closeModal();
          Swal.fire({
            title: 'Succes!',
            text: "Score has been added Successfully",
            icon: 'success',
            iconColor: '#6485C1',
            showConfirmButton: false,
            timer: 1500
          }).then((result)=>{
            score.scoreId = res.score.scoreId;
            this.getFinalScore(score, this.currentConfig);
          });
        },
        error: (error) => {
          console.log(error);
        }
      });  
    } else {
      score.scoreId = this.candidate.score.scoreId;
      score.finalScore = this.getFinalScore(score, this.currentConfig);
      this.scoreService.updateScore(score).subscribe({
        next: (res: any) => {
          this.isSaved.emit(true);
          this.closeModal()
          Swal.fire({
            title: 'Succes!',
            text: "Score has been updated Successfully",
            icon: 'success',
            iconColor: '#6485C1',
            showConfirmButton: false,
            timer: 1500
          });
        },
        error: (error) => {
          console.log(error);
        }
      });
    }
  }

  getFinalScore(score: Score, config : ScoreConfig) {
    this.score = score;
    if ((score.technicalTestScore !== null && score.technicalTestScore !== 0) && (score.technicalInterviewScore !== null && score.technicalInterviewScore !== 0) && (score.peopleScore !== null && score.peopleScore !== 0) && (score.softSkillsScore !== null && score.softSkillsScore !== 0)) {
      score.finalScore = ((score.technicalTestScore * config.technicalTestWeight) + (score.technicalInterviewScore * config.technicalInterviewWeight) + (score.peopleScore * config.peopleWeight) + (score.softSkillsScore * config.softSkillsWeight)) / 4;
      return score.finalScore;
    }
  }

  closeModal(){
    this.activeModal.close();
  }
}
