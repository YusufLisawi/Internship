import { Pipe, PipeTransform } from "@angular/core";
import { Session } from "@models/Session.model";

@Pipe({
  name: "sessionFilter"
})
export class SessionFilter implements PipeTransform {
  transform(list: Session[], name: string, technology: string, date: Date, filterActive: boolean): Session[]{
    if (!list) return null;
    if (!filterActive) return list;
    if(name)
      list=list.filter(session => session.sessionName.toLowerCase().indexOf(name.toLowerCase()) !== -1);
    if (!list) return null;
    if(date)
      list=list.filter(session => (session.sessionDate==date));
    if (!list) return null;
    if(technology)
      list=list.filter(session => session.technology== technology);
    return list;
  }
}
