import * as React from 'react';

export const getDataFromFile = () => {
    return fetch('src/assets/example_ballot.json');
}