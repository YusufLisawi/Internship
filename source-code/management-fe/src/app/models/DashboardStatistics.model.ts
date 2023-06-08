export class DashboardStatistics {
  constructor(
    public sessionId: number,
    public candidateNumber: number,
    public preselectedCandidateNumber: number,
    public admittedCandidateNumber: number,
    public maleCandidateNumber: number,
    public femaleCandidateNumber: number,
    public candidatesByCities: Map<string, number>,
    public candidatesByUniversities: Map<string, number>,
    public candidatesByDiplome: Map<string, number>
  ) {}
}
