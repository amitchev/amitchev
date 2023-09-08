import { useState, useEffect, useRef } from 'react';
import { BrowserRouter, Link, Route, useNavigate } from 'react-router-dom';
import * as React from 'react';
import logo from './Images/logo.png';

console.log('[App.tsx]', `Hello world from Electron ${process.versions.electron}!`);

function App() {
  const [selectedButton, setSelectedButton] = useState<number | null>(null);
  const buttonRefs = useRef<(HTMLLIElement | null)[]>([null, null]);
  const navigate = useNavigate();

  useEffect(() => {
    const handleKeydown = (e: KeyboardEvent) => {
      const { key } = e;
      if (['ArrowUp', 'ArrowDown', 'Enter'].includes(key)) {
        e.preventDefault();

        if (selectedButton === null) {
          setSelectedButton(0);
          return;
        }

        if (key === 'ArrowUp') {
          setSelectedButton((prev) => (prev === 0 ? 1 : 0));
        } else if (key === 'ArrowDown') {
          setSelectedButton((prev) => (prev === 1 ? 0 : 1));
        } else if (key === 'Enter' && buttonRefs.current[selectedButton]) {
          if (selectedButton === 0) {
            navigate('/gethash');
          } else if (selectedButton === 1) {
            navigate('/ballot');
          }
        }
      }
    };

    window.addEventListener('keydown', handleKeydown);
    return () => window.removeEventListener('keydown', handleKeydown);
  }, [selectedButton, navigate]);

  useEffect(() => {
    if (selectedButton !== null && buttonRefs.current[selectedButton]) {
      buttonRefs.current[selectedButton]!.focus();
    }
  }, [selectedButton]);

  return (
    <div className="bg-gradient-to-tr from-green-500 to-blue-300 min-h-screen flex flex-col justify-center items-center">
      <img src={logo} alt="Logo" className="w-1/3 h-auto mb-4" />
      <ul className="w-full h-full flex flex-col justify-center items-center">
        <li
          ref={(el) => (buttonRefs.current[0] = el)}
          className={`bg-white rounded-xl shadow-md w-1/3 h-[12%] m-4 text-xl flex justify-center items-center transition duration-300 transform ${
            selectedButton === 0 ? 'bg-gray-300 shadow-lg scale-110' : ''
          }`}
          tabIndex={0}
          onFocus={() => setSelectedButton(0)}
          onBlur={() => setSelectedButton(null)}
          onClick={() => navigate('/gethash')}
          style={{ cursor: 'pointer' }}
        >
          <Link to="/gethash" className="block w-full h-full text-center py-4">
            Get Hash
          </Link>
        </li>
        <li
          ref={(el) => (buttonRefs.current[1] = el)}
          className={`bg-white rounded-xl shadow-md w-1/3 h-[12%] m-4 text-xl flex justify-center items-center transition duration-300 transform ${
            selectedButton === 1 ? 'bg-gray-300 shadow-lg scale-110' : ''
          }`}
          tabIndex={0}
          onFocus={() => setSelectedButton(1)}
          onBlur={() => setSelectedButton(null)}
          onClick={() => navigate('/ballot')}
          style={{ cursor: 'pointer' }}
        >
          <Link to="/ballot" className="block w-full h-full text-center py-4">
            Ballot
          </Link>
        </li>
      </ul>
    </div>
  );
}

export default App;
