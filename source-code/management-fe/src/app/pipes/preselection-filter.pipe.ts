import { Pipe, PipeTransform } from "@angular/core";
import { Candidate } from "@models/Candidate.model";

@Pipe({
  name: "PreselectionPipe"
})
export class PreselectionPipe implements PipeTransform {
  transform(list: Candidate[], name: string, preselectionStatus: string): Candidate[] {
    if (!list) {
      return null;
    }

    if (name) {
      list = list.filter(candidate => ((candidate.lastName.toLowerCase().indexOf(name.toLowerCase()) !== -1) || (candidate.firstName.toLowerCase().indexOf(name.toLowerCase()) !== -1) || ((candidate.email?.toLowerCase().indexOf(name.toLowerCase()) || -1) !== -1)));
    }

    if (preselectionStatus != "") {
      if (preselectionStatus == "Pending") {
        list = list.filter(candidate => candidate.preselectionStatus == null);
      }
      else {
        list = list.filter(candidate => candidate.preselectionStatus == preselectionStatus);
      }
    }
    return list;
  }
}
