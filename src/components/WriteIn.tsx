import { useState } from "react";

interface WriteInProps {
  index: number;
  value: string;
  onChange: (index: number, value: string) => void;
}

const WriteIn = ({ index, value, onChange }: WriteInProps) => {
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    onChange(index, event.target.value);
  };

  return (
    <label
      key={`write-in-${index}`}
      className="block py-2"
    >
      <input
        type="radio"
        name="option"
        value={`write-in-${index}`}
        checked={value !== ""}
        onChange={() => onChange(index, "")}
      />
      Write-In
      <input
        type="text"
        name="write-in"
        value={value}
        onChange={handleInputChange}
      />
    </label>
  );
};

export default WriteIn;
