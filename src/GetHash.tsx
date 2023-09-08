import React, { useState, useRef, useEffect } from 'react';
import logo from './Images/logo.png';
import back_arrow from './Images/go-back.png';
import { useNavigate } from 'react-router-dom';
import crypto from 'crypto';
const { ipcRenderer } = window.require('electron');


const GetHash: React.FC = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [hash, setHash] = useState('');
  const [isSubmitFocused, setIsSubmitFocused] = useState(false);

  const firstNameRef = useRef<HTMLInputElement>(null);
  const lastNameRef = useRef<HTMLInputElement>(null);
  const submitButtonRef = useRef<HTMLButtonElement>(null);
  const backButtonRef = useRef<HTMLButtonElement>(null);

  const navigate = useNavigate();

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const shasum = crypto.createHash('sha1')
    const hash = shasum.update(firstName + lastName).digest('hex');
    setHash(hash);
    try {
      await navigator.clipboard.writeText(hash);
    } catch (err) {
      console.error('Failed to copy hash to clipboard:', err);
    }
  };

  const handleKeyDown = (event: React.KeyboardEvent) => {
    if (event.key === 'Enter') {
      event.preventDefault();
      if (event.currentTarget === backButtonRef.current) {
        handleGoBack();
      } else if (event.currentTarget === submitButtonRef.current) {
        handleSubmit(event as unknown as React.FormEvent<HTMLFormElement>);
      }
    } else if (event.key === 'ArrowDown') {
      event.preventDefault();
      if (event.currentTarget === firstNameRef.current) {
        lastNameRef.current?.focus();
      } else if (event.currentTarget === lastNameRef.current) {
        submitButtonRef.current?.focus();
      } else if (event.currentTarget === submitButtonRef.current) {
        backButtonRef.current?.focus();
      } else if (event.currentTarget === backButtonRef.current) {
        firstNameRef.current?.focus();
      }
    } else if (event.key === 'ArrowUp') {
      event.preventDefault();
      if (event.currentTarget === lastNameRef.current) {
        firstNameRef.current?.focus();
      } else if (event.currentTarget === submitButtonRef.current) {
        lastNameRef.current?.focus();
      } else if (event.currentTarget === backButtonRef.current) {
        submitButtonRef.current?.focus();
      } else if (event.currentTarget === firstNameRef.current) {
        backButtonRef.current?.focus();
      }
    }
  };

  const handleGoBack = () => {
    navigate(-2);
  };

  React.useEffect(() => {
    firstNameRef.current?.focus();
  }, []);

  return (
    <div className="bg-gradient-to-tr from-green-500 to-blue-300 min-h-screen flex items-center justify-center">
      <img src={logo} alt="Logo" className="absolute bottom-4 right-4 h-16 w-auto" />
      <button
        onClick={handleGoBack}
        className="absolute top-4 left-4"
        ref={backButtonRef}
        onKeyDown={handleKeyDown}
      >
        <img src={back_arrow} alt="back_arrow" className="h-16 w-auto" />
      </button>
      <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
        <h2 className="text-3xl font-semibold text-center mb-8">Login</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-6">
            <label className="block text-gray-600 text-sm mb-2" htmlFor="first-name">
              First Name:
            </label>
            <input
              className="w-full p-2 text-lg border-b-4 border-gray-300 focus:border-green-500 focus:outline-none focus:ring-0 transition duration-500"
              id="first-name"
              type="text"
              placeholder="Enter your first name"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              ref={firstNameRef}
              onKeyDown={handleKeyDown}
            />
          </div>
          <div className="mb-6">
            <label className="block text-gray-600 text-sm mb-2" htmlFor="last-name">
              Last Name:
            </label>
            <input
              className="w-full p-2 text-lg border-b-4 border-gray-300 focus:border-green-500 focus:outline-none focus:ring-0 transition duration-500"
              id="last-name"
              type="text"
              placeholder="Enter your last name"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              ref={lastNameRef}
              onKeyDown={handleKeyDown}
            />
          </div>
          <button
            type="submit"
            className={`w-full text-lg ${
              isSubmitFocused
                ? 'bg-gradient-to-r from-green-600 to-blue-400'
                : 'bg-gradient-to-r from-green-500 to-blue-300'
            } text-white font-semibold py-3 px-4 rounded-full focus:outline-none transition duration-300`}
            ref={submitButtonRef}
            onKeyDown={handleKeyDown}
            onFocus={() => setIsSubmitFocused(true)}
            onBlur={() => setIsSubmitFocused(false)}
          >
            Submit
          </button>
        </form>
        {hash && (
          <div className="mt-6 text-center">
            <p className="text-gray-500 font-bold text-lg">Your hash value has been successfully copied to the clipboard for future use:</p>
            <p className="text-gray-700 text-lg">{hash}</p>
          </div>
        )}
      </div>
    </div>
  );
        }

export default GetHash;