import { Pipe, PipeTransform } from "@angular/core";
import { Candidate } from "@models/Candidate.model";

@Pipe({
  name: "finalListFilter"
})
export class FinalListFilter implements PipeTransform {
  transform(list: Candidate[], anapec: string, technology: string, preselection: string, english: string, spanish: string, filterActive: boolean): Candidate[]{
    if (!list) return null;
    if (!filterActive) return list;
    if(anapec)
        list=list.filter(candidate => candidate.anapec==anapec);
    if (!list) return null;
    if(technology)
      list=list.filter(candidate => candidate.session.technology==technology);
    if (!list) return null;
    if(preselection)
      list=list.filter(candidate => candidate.preselectionStatus==preselection);

    if (!list) return null;
    if(english)
      list=list.filter(candidate => candidate.score.englishLevel>=english);
    if (!list) return null;
    if(spanish)
        list=list.filter(candidate => candidate.score.spanishLevel>=spanish);
    if (!list) return null;
    return list;
  }
}
