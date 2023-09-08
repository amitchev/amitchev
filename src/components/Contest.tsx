import { BallotContext } from "@/Ballot";
import { useState, useRef, useEffect, useContext } from "react";

interface ContestProps {
  contestData: Contest;
}

const Contest = ({ contestData }: ContestProps) => {
  const [selectedOption, setSelectedOption] = useState<String | null>(null);
  const [focusedIndex, setFocusedIndex] = useState(0);
  const { currentChoice, setCurrentChoice } = useContext(BallotContext)
  const formRef = useRef<HTMLFormElement | null>(null);

  const handleKeyDown = (event: React.KeyboardEvent<HTMLFormElement>) => {
    const { key } = event;
    if (key === "ArrowUp" && focusedIndex > 0) {
      setFocusedIndex((prevIndex) => prevIndex - 1);
    } else if (
      key === "ArrowDown" &&
      focusedIndex < contestData.contestChoices.length - 1
    ) {
      setFocusedIndex((prevIndex) => prevIndex + 1);
    } else if (key === "Enter") {
      event.preventDefault();
      if (focusedIndex < contestData.contestChoices.length) {
        const selectedChoiceIndex = focusedIndex;
        setSelectedOption(
          contestData.contestChoices[selectedChoiceIndex].candidate.name
        );
        console.log(contestData.contestChoices[selectedChoiceIndex].candidate.name)
        handleCurrChoice(contestData.contestChoices[selectedChoiceIndex].candidate.name);
      }
    }
  };
  const handleCurrChoice = (choice: any) => {
    console.log(choice);
    setCurrentChoice(choice);
  }

  useEffect(() => {
    setSelectedOption(currentChoice)
    formRef.current?.focus();
  }, [currentChoice]);

  return (
    <form
      onKeyDown={handleKeyDown}
      ref={formRef}
      tabIndex={0}
      className="outline-none"
    >
      <h2 className="text-2xl font-bold mb-4">{contestData.contestName}</h2>
      {contestData.contestChoices.map((choice, index) => (
        <label
          key={choice.candidate.name}
          className={`${
            index === focusedIndex ? "bg-gray-200" : ""
          } block py-2`}
        >
          <input
            type="radio"
            name="option"
            value={choice.candidate.name}
            checked={selectedOption === choice.candidate.name}
            onChange={(event) => setSelectedOption(event.target.value)}
          />
          {choice.candidate.name} ({choice.candidate.party})
        </label>
      ))}
    </form>
  );
};

export default Contest;
