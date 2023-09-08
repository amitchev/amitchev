import CryptoJS from 'crypto-js';

const SECRET_KEY = 'Team T02 Rocks!';

export const encryptData = (data: any): string => {
  const ciphertext = CryptoJS.AES.encrypt(JSON.stringify(data), SECRET_KEY);
  return ciphertext.toString();
};

export const decryptData = (ciphertext: string): any => {
  const bytes = CryptoJS.AES.decrypt(ciphertext, SECRET_KEY);
  const decryptedData = JSON.parse(bytes.toString(CryptoJS.enc.Utf8));
  return decryptedData;
};