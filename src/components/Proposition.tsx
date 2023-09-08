import type { FunctionComponent } from "react";
import { useContext, useState, useRef, useEffect } from "react";
import { BallotContext } from "@/Ballot";

interface PropositionProps {
  proposition: Proposition;
}

const Proposition: FunctionComponent<PropositionProps> = ({ proposition }) => {
  const { currentChoice, setCurrentChoice } = useContext(BallotContext);

  const [selectedOption, setSelectedOption] = useState<string | null>(null);
  const [focusedIndex, setFocusedIndex] = useState<number>(0);
  const formRef = useRef<HTMLFormElement | null>(null);

  const handleKeyDown = (event: React.KeyboardEvent<HTMLFormElement>) => {
    const { key } = event;
    if (key === "ArrowUp" && focusedIndex > 0) {
      setFocusedIndex((prevIndex) => prevIndex - 1);
    } else if (
      key === "ArrowDown" &&
      focusedIndex < proposition.propChoices.length - 1
    ) {
      setFocusedIndex((prevIndex) => prevIndex + 1);
    } else if (key === "Enter") {
      event.preventDefault();
      if (focusedIndex < proposition.propChoices.length) {
        const selectedChoiceIndex = focusedIndex;
        handleCurrChoice(proposition.propChoices[selectedChoiceIndex].option)
      }
    }
  };

  const handleCurrChoice = (choice: string) => {
    setSelectedOption(choice);
    setCurrentChoice(choice);
  };

  useEffect(() => {
    console.log(currentChoice)
    setSelectedOption(currentChoice)
    formRef.current?.focus();
  }, [currentChoice]);

  return (
    <form onKeyDown={handleKeyDown} ref={formRef} tabIndex={0}>
      <h2 className="text-2xl font-bold mb-4">{proposition.propName}</h2>
      <p className="mb-4">{proposition.propDescription}</p>
      <ul>
        {proposition.propChoices.map((choice, index) => (
          <li
            onClick={(event) => {
              event.preventDefault();
              handleCurrChoice(choice.option);
            }}
            key={index}
            className={`mb-2 ${focusedIndex === index ? "bg-gray-200" : ""}`}
          >
            <label className="inline-flex items-center">
              <input
                type="radio"
                name={`proposition_${index}`}
                value={index}
                className="form-radio h-4 w-4 text-indigo-600"
                checked={selectedOption === choice.option}
                onChange={(event) => setSelectedOption(choice.option)}
              />
              <span className="ml-2">{choice.option}</span>
            </label>
          </li>
        ))}
      </ul>
    </form>
  );
};

export default Proposition;
