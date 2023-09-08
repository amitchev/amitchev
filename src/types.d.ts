interface Candidate {
  name: string;
  party: string;
}

interface ContestChoice {
  candidate: Candidate;
}

interface Contest {
  contestName: string;
  contestVoteFor: number;
  contestWriteIn: boolean;
  contestChoices: ContestChoice[];
}

interface PropositionChoice {
  option: string;
}

interface Proposition {
  propName: string;
  propDescription: string;
  propChoices: PropositionChoice[];
}

interface RankedChoice {
  rankedChoiceName: string;
  rankedChoiceVoteFor: number;
  rankedChoiceWriteIn: boolean;
  rankedChoices: ContestChoice[];
}

interface Approval {
  approvalName: string;
  approvalChoices: ContestChoice[];
}

interface Item {
  contest?: Contest;
  proposition?: Proposition;
  rankedChoice?: RankedChoice;
  approval?: Approval;
}

interface Section {
  sectionName: string;
  items: Item[];
}

interface Header {
  date: string;
  title: string;
  instructions: string;
}

interface Ballot {
  header: Header;
  sections: Section[];
}