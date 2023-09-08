import * as React from 'react';
import { useContext, useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import Contest from './components/Contest';
import Proposition from './components/Proposition';
import RankedChoice from './components/RankedChoice';
import Approval from './components/Approval';
import { encryptData } from './utils/crypto';
import PrinterDriver from './components/PrinterDriver';
import logo from './Images/logo.png';
import {getDataFromFile} from './InputParser'

type BallotContextType = {
  contextChoices: string[],
  currentChoice: any,
  encryptedContextChoices: any[],
  setContextChoices: React.Dispatch<React.SetStateAction<any[]>>,
  setCurrentChoice: React.Dispatch<React.SetStateAction<any[]>>
};

export const BallotContext = React.createContext<BallotContextType>({
  contextChoices: [],
  currentChoice: null,
  encryptedContextChoices: [],
  setContextChoices: (prevState) => {},
  setCurrentChoice: (currChoice) => {},
});


export default function BallotComp() {
  const [currentSectionIndex, setCurrentSectionIndex] = useState(0);
  const [currentItemIndex, setCurrentItemIndex] = useState(0);
  const [currentContextIndex, setCurrentContextIndex] = useState(0);
  const [ballotData, setBallotData] = useState<Ballot | null>(null);
  const [currentChoice, setCurrentChoice] = useState("");
  const [contextChoices, setContextChoices] = useState<any[]>([]);
  const [ballotItems, setBallotItems] = useState<string[]>([]);
  const [encryptedContextChoices, setEncryptedContextChoices] = useState<string[]>([]);
  var endOfBallot = false;

  const handleCurrChoice = (currChoice: any) =>  {
    console.log(currChoice);
    setCurrentChoice(currChoice);
  };

  useEffect(() => {
    const fetchBallot = async () => {
      const response = await getDataFromFile();
      const data = await response.json();
      setBallotData(data);
    };

    fetchBallot()

  }, []);

  if (!ballotData) {
    return <div>Loading...</div>;
  }

  const currentSection = ballotData.sections[currentSectionIndex];
  const currentItem = currentSection.items[currentItemIndex];

  const handleNext = () => {
    if (currentItemIndex < currentSection.items.length - 1) {
      setCurrentItemIndex(currentItemIndex + 1);
      setCurrentContextIndex(currentContextIndex + 1);
      handleAddChoice();
      //setCurrentChoice('na');
    } else if (currentSectionIndex < ballotData.sections.length - 1) {
      setCurrentSectionIndex(currentSectionIndex + 1);
      setCurrentContextIndex(currentContextIndex + 1);
      setCurrentItemIndex(0);
      handleAddChoice();
      //setCurrentChoice('na');
    } else {
      endOfBallot = true;
      ballotData.header.title = 'Ballot Review';
      currentSection.sectionName = 'Review';
      ballotData.header.instructions = 'Please review your ballot and ensure the selections are accurate, press ENTER to confirm your ballot is accurate. Press the left arrow key to go back to your ballot.';
      console.log('Made it to else in handleNext')
      handleAddChoice();
      setCurrentChoice('na');
      setCurrentItemIndex(currentItemIndex + 1);
      setCurrentContextIndex(currentContextIndex + 1);
      //renderCurrentItem();
    }
  };

  const handlePrev = () => {
    if (currentItemIndex > 0) {
      setCurrentItemIndex(currentItemIndex - 1);
      setCurrentContextIndex(currentContextIndex - 1);
      setCurrentChoice(contextChoices[currentContextIndex - 1]);
    } else if (currentSectionIndex > 0) {
      setCurrentSectionIndex(currentSectionIndex - 1);
      setCurrentItemIndex(ballotData.sections[currentSectionIndex - 1].items.length - 1);
      setCurrentContextIndex(currentContextIndex - 1);
      setCurrentChoice(contextChoices[currentContextIndex - 1]);
    }
  };

  const renderCurrentItem = () => {
    if (currentItemIndex == currentSection.items.length) {
      console.log("Made it to renderCurrentItem()");
      return <PrinterDriver ballotItems={ballotItems} ballotChoices={contextChoices} />;
    } else if (currentItem.contest) {
      if (ballotItems.indexOf(currentItem.contest.contestName) === -1)
        handleAddBallotItem(currentItem.contest.contestName);
      return <Contest contestData={currentItem.contest} />;
    } else if (currentItem.proposition) {
      if (ballotItems.indexOf(currentItem.proposition.propName) === -1)
        handleAddBallotItem(currentItem.proposition.propName);
      return <Proposition proposition={currentItem.proposition} />;
    } else if (currentItem.rankedChoice) {
      if (ballotItems.indexOf(currentItem.rankedChoice.rankedChoiceName) === -1)
        handleAddBallotItem(currentItem.rankedChoice.rankedChoiceName);
      return <RankedChoice rankedChoice={currentItem.rankedChoice} />;
    } else if (currentItem.approval) {
      if (ballotItems.indexOf(currentItem.approval.approvalName) === -1)
        handleAddBallotItem(currentItem.approval.approvalName);
      return <Approval approval={currentItem.approval} />;
    } else {
      //console.log("Made it to renderCurrentItem()");
      //return <BallotReview ballotItems={ballotItems} ballotChoices={contextChoices} />;
      return <div>Invalid item type</div>;
    }
  };

  const handleAddBallotItem = (newItem: string) => {
    setBallotItems(() => {
      console.log([...ballotItems, newItem]);
      return [...ballotItems, newItem];
    });
  };

  const handleAddChoice = () => {
      setContextChoices(() => {
      if ((!contextChoices[currentContextIndex] || currentChoice == "") &&  contextChoices.length < 6) {
              console.log(currentChoice);
              console.log([...contextChoices, currentChoice]);
              setCurrentChoice(currentChoice);
              return [...contextChoices, currentChoice];
            } else {
              contextChoices[currentContextIndex] = currentChoice;
              console.log(currentChoice);
              console.log([...contextChoices]);
              setCurrentChoice(currentChoice);
              return contextChoices;
            }
    });
  };

  const handleKeyPress = (event: React.KeyboardEvent) => {
    event.preventDefault();
    if (event.key === "ArrowLeft") {
      handlePrev();
    } else if (event.key === "ArrowRight") {
      handleNext();
    }
  };

  // const handleAddChoice = (choice: any) => {
  //   setContextChoices((prevState) => {
  //     let updatedState;
  //     if (contextChoices.indexOf(choice) === -1) {
  //       updatedState = [...prevState, choice];
  //     } else {
  //       const index = prevState.indexOf(choice);
  //       prevState[index] = choice;
  //       updatedState = [...prevState];
  //     }
  //     const encryptedData = encryptData(updatedState);
  //     setEncryptedContextChoices(encryptedData);
  //     return updatedState;
  //   });
  // };



  return (
    <div
      className="bg-gradient-to-tr from-green-500 to-blue-300 min-h-screen flex items-center justify-center"
      tabIndex={-1}
      onKeyDown={handleKeyPress}
      // onMouseDown={(e) => {e.preventDefault()}}
    >
      <img src={logo} alt="Logo" className="fixed bottom-4 right-4 h-16 w-auto fixed" />
        <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-2xl">
          <div className="mb-4">
            <p className="font-bold text-black text-center text-2xl">{ballotData.header.title}</p>
            <p className="text-black text-center text-lg">{`Current section: ${currentSection.sectionName}`}</p>
            <p className="text-black text-center text-lg">{ballotData.header.instructions}</p>
          </div>
          <hr className="border-t-2 border-gray-200 mb-4 mx-auto w-2/3" />
          <div className="flex-grow w-full h-full flex flex-col items-center">
          <BallotContext.Provider
            value={{
              contextChoices,
              currentChoice,
              encryptedContextChoices,
              setContextChoices: handleAddChoice,
              setCurrentChoice: handleCurrChoice,
            }}
          >
            {renderCurrentItem()}
          </BallotContext.Provider>
        </div>
        <div className="text-center mt-4">
          <p className="text-gray-400">
            To navigate the voting machine, please use the left and right arrow keys to move between pages, the up and down arrow keys to highlight your preferred candidate, and press the Enter key to confirm your selection.
          </p>
        </div>
      </div>
    </div>
  );
}