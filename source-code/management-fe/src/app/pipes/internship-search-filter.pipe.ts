import { Pipe, PipeTransform } from "@angular/core";
import { Internship } from "@models/Internship.model";

@Pipe({
  name: "internshipSearchFilter",
})
export class InternshipSearchFilterPipe implements PipeTransform {
  transform(
    list: Internship[],
    keys: string,
    levelOfStudies: string,
    status: string,
    university: string,
    city: string,
    filterActive: boolean
  ): Internship[] {
    if (!list) return null;
    if (!filterActive) return list;
    if (keys)
      list = list.filter(
        (internship) =>
          internship.candidate.lastName
            .toLowerCase()
            .indexOf(keys.toLowerCase()) !== -1 ||
          internship.candidate.firstName
            .toLowerCase()
            .indexOf(keys.toLowerCase()) !== -1 ||
          internship.subject
            .toLowerCase()
            .indexOf(keys.toLowerCase()) !== -1 ||
          (internship.candidate.email
            ?.toLowerCase()
            .indexOf(keys.toLowerCase()) || -1) !== -1
      );
    if (!list) return null;
    if (levelOfStudies)
      list = list.filter(
        (internship) =>
          internship.candidate.diplome.indexOf(levelOfStudies) !== -1
      );
    if (!list) return null;
    if (university)
      list = list.filter(
        (internship) =>
          internship.candidate.university.indexOf(university) !== -1
      );
    if (!list) return null;
    if (city)
      list = list.filter(
        (internship) => internship.candidate.city.indexOf(city) !== -1
      );
    if (status)
      list = list.filter(
        (internship) => internship.internshipStatus.indexOf(status) !== -1
      );
    if (!list) return null;
    return list;
  }
}
