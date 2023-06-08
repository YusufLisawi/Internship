export class Score {
    scoreId?: number;
  
    constructor( 
      public technicalDesc: string,
      public technicalTestScore: number,
      public technicalInterviewScore: number,
      public peopleDesc: string,
      public peopleScore: number,
      public softSkillsDesc: string,
      public softSkillsScore: number,
      public englishLevel: string,
      public spanishLevel: string,
      public finalScore: number,
      public isAccepted?: Boolean) { }
  }
  