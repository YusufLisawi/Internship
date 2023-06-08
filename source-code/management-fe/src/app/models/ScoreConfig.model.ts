export class ScoreConfig {
    scoreConfigId?: number;
  
    constructor( 
      public peopleDenominator: number,
      public softSkillsDenominator: number,
      public technicalTestDenominator: number,
      public technicalInterviewDenominator: number,
      public peopleWeight: number,
      public softSkillsWeight: number,
      public technicalTestWeight : number,
      public technicalInterviewWeight : number,
      public statut : string) { }
  }
  