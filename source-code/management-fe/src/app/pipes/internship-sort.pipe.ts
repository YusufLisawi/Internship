import { Pipe, PipeTransform } from "@angular/core";
import { Internship } from "@models/Internship.model";

@Pipe({
  name: "internshipSort",
})
export class InternshipSortPipe implements PipeTransform {
  transform(
    list: Internship[],
    column: string,
    ascending: boolean
  ): Internship[] {
    if (!list || !column) return list;
    return list.sort((a, b) => {
      switch (column) {
        case "name":
          return this.compare(
            (a.candidate.lastName + " " + a.candidate.firstName).toLowerCase(),
            (b.candidate.lastName + " " + b.candidate.firstName).toLowerCase(),
            ascending
          );
        case "email":
          return this.compare(
            a.candidate.email.toLowerCase(),
            b.candidate.email.toLowerCase(),
            ascending
          );
        case "university":
          return this.compare(
            a.candidate.university.toLowerCase(),
            b.candidate.university.toLowerCase(),
            ascending
          );
        case "diploma":
          return this.compare(
            a.candidate.diplome.toLowerCase(),
            b.candidate.diplome.toLowerCase(),
            ascending
          );
        case "subject":
          return this.compare(
            a.subject.toLowerCase(),
            b.subject.toLowerCase(),
            ascending
          );
        case "supervisor":
          return this.compare(
            a.supervisor.toLowerCase(),
            b.supervisor.toLowerCase(),
            ascending
          );
        case "startDate":
          return this.compare(a.startDate, b.startDate, ascending);
        case "endDate":
          return this.compare(a.endDate, b.endDate, ascending);
        case "status":
          return this.compare(
            a.internshipStatus.toLowerCase(),
            b.internshipStatus.toLowerCase(),
            ascending
          );
        default:
          return 0;
      }
    });
  }

  compare(
    a: number | string | Date,
    b: number | string | Date,
    asc: boolean
  ): number {
    if (!b) return -1;
    if (!a) return 1;
    return (a < b ? -1 : 1) * (asc ? 1 : -1);
  }
}
