import { Pipe, PipeTransform } from '@angular/core';
import { Session } from '@models/Session.model';

@Pipe({
  name: 'sessionOrder'
})
export class SessionOrder implements PipeTransform {

  transform(list: Session[], column: string, ascending: boolean): Session[] {
    if((!list)||(!column)) return list;
    return list.sort((a, b) => {
      switch (column) {
        case "name":
            return this.compare(a.sessionName.toLowerCase(), b.sessionName.toLowerCase(), ascending);
        case "technology":
          return this.compare(a.technology.toLowerCase(), b.technology.toLowerCase(), ascending);
        case "date":
          return this.compare(a.sessionDate, b.sessionDate, ascending);
        default:
          return 0;
      }
    });
  }
  compare(a: number | string | Date, b: number | string | Date, asc: boolean): number {
    if(!b)
      return -1;
    if(!a)
      return 1;
    return (a < b ? -1 : 1) * (asc ? 1 : -1);
  }

} 