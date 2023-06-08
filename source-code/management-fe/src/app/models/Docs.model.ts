import { Internship } from "./Internship.model";

export class Docs {
  docsId?: number;

  constructor(
    public type: String,
    public path: String,
    public internship?: Internship
  ) {}
}
