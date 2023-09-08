import type { FunctionComponent } from "react";
import { useContext, useState, useEffect, useRef } from "react";
import { BallotContext } from "@/Ballot";

interface RankedChoiceProps {
    rankedChoice: RankedChoice
}

const RankedChoice: FunctionComponent<RankedChoiceProps> = ({ rankedChoice }) => {

    const { currentChoice, setCurrentChoice } = useContext(BallotContext);
    const inputRef = useRef<HTMLElement | null | any>(null);

    useEffect(() => {
        if (inputRef.current) {
            inputRef.current.focus();
        }
    }, []);
    
    const handleCurrChoice = (choice: any) => {
        console.log(choice);
        setCurrentChoice(choice);
    }


    return(
        <>
            <h2 className="text-2xl font-bold mb-4">{rankedChoice.rankedChoiceName}</h2>
            <form>
            <ul>
                {rankedChoice.rankedChoices.map((choice, index) => {
                    var curRef;
                    var checked;
                    if (choice.candidate.name === currentChoice) {
                        curRef = inputRef;
                        checked = true;
                    }
                    else {
                        curRef = null;
                        checked = false;
                    }
                    console.log("[CONTEST] candidate name: ", choice.candidate.name)
                    return(
                    <li onClick = {() => handleCurrChoice(choice.candidate.name)} key={index} className="mb-2">
                        <input type="radio" name={`option`} value={index} className="form-radio h-4 w-4 text-indigo-600" />
                        <span> {choice.candidate.name} - {choice.candidate.party}</span>
                    </li>
                )})}
            </ul>
            </form>
        </>

    );
}

export default RankedChoice;