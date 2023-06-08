import { Pipe, PipeTransform } from "@angular/core";
import { Candidate } from "@models/Candidate.model";

@Pipe({
  name: "searchFilter"
})
export class SearchFilterPipe implements PipeTransform {
  transform(list: Candidate[], name: string, levelOfStudies: string, university: string, city: string, filterActive: boolean, anapec?:string): Candidate[]{
    if (!list) return null;
    if (!filterActive) return list;
    if(name)
      list=list.filter(candidate => ((candidate.lastName.toLowerCase().indexOf(name.toLowerCase()) !== -1) || (candidate.firstName.toLowerCase().indexOf(name.toLowerCase()) !== -1) || ((candidate.email?.toLowerCase().indexOf(name.toLowerCase())||-1) !== -1)));
    if (!list) return null;
    if(levelOfStudies)
      list=list.filter(candidate => candidate.diplome.indexOf(levelOfStudies) !== -1);
    if (!list) return null;
    if(university)
      list=list.filter(candidate => candidate.university.indexOf(university) !== -1);
    if (!list) return null;
    if(city)
      list=list.filter(candidate => candidate.city.indexOf(city) !== -1);
    if (!list) return null;
    if(anapec)
      list=list.filter(candidate => candidate.anapec==anapec);
    return list;
  }
}
