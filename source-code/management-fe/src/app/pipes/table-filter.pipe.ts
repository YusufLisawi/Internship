import { Pipe, PipeTransform } from '@angular/core';
import { Candidate } from '@models/Candidate.model';

@Pipe({
  name: 'tableFilter'
})
export class TableFilterPipe implements PipeTransform {

  transform(list: Candidate[], column: string, ascending: boolean): Candidate[] {
    if((!list)||(!column)) return list;
    return list.sort((a, b) => {
      switch (column) {
        case "fName":
          return this.compare(a.firstName.toLowerCase(), b.firstName.toLowerCase(), ascending);
        case "lName":
          return this.compare(a.lastName.toLowerCase(), b.lastName.toLowerCase(), ascending);
        case "email":
          return this.compare(a.email?.toLowerCase(), b.email?.toLowerCase(), ascending);
        case "university":
          return this.compare(a.university, b.university, ascending);
        case "levelOfStudy":
          return this.compare(a.diplome, b.diplome, ascending);
        case "city":
          return this.compare(a.city, b.city, ascending);
        case "Preselection":
          return this.compare((a.preselectionStatus=="true"? 3:(a.preselectionStatus=="false"? 2:1)), (b.preselectionStatus=="true"? 3:(b.preselectionStatus=="false"? 2:1)), ascending);
        
        //for final list
        case "anapec":
          return this.compare((a.anapec=="true"? 3:(a.anapec=="false"? 2:1)), (b.anapec=="true"? 3:(b.anapec=="false"? 2:1)), ascending);
        case "Techno":
          return this.compare(a.session.technology, b.session.technology, ascending);
        case "presence":
          return this.compare((a.presence=="true"? 3:(a.presence=="false"? 2:1)), (b.presence=="true"? 3:(b.presence=="false"? 2:1)), ascending);

        //for intervie<
        case "SessionName":
          return this.compare(a.session.sessionName, b.session.sessionName, ascending);


        //score
        case "English":
          return this.compare(a.score.englishLevel, b.score.englishLevel, ascending);
        case "Spanish":
          return this.compare(a.score.spanishLevel, b.score.spanishLevel, ascending);
        default:
          return 0;
      }
    });
  }
  compare(a: number | string, b: number | string, asc: boolean): number {
    if(!b)
      return -1;
    if(!a)
      return 1;
    return (a < b ? -1 : 1) * (asc ? 1 : -1);
  }

} 