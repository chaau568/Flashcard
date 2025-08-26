import './Register.css'
import React, { useState, useEffect } from 'react';
import forge from 'node-forge';
import CryptoJS from 'crypto-js';

const Register: React.FC = () => {
    const [publicKeyPem, setPublicKeyPem] = useState<string>('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [msg, setMsg] = useState('');

    useEffect(() => {
        fetch("http://localhost:8080/flashcard/encrypt/public-key")
            .then(res => res.text())
            .then(base64 => {
                const der = forge.util.decode64(base64);
                const asn1 = forge.asn1.fromDer(der);
                const pub = forge.pki.publicKeyFromAsn1(asn1);
                const pem = forge.pki.publicKeyToPem(pub);
                setPublicKeyPem(pem);
            });
    }, []);

    const handleTest = () => {
        setMsg("send to backend")
    }

    const handleRegister = async () => {
        // 1. Generate AES key (16 bytes)
        const aesKey = CryptoJS.lib.WordArray.random(16);
        // const aesKeyBytes = forge.util.createBuffer(aesKey.toString(CryptoJS.enc.Hex), 'hex').bytes();
        // แปลง WordArray เป็น Uint8Array แล้วเข้ารหัส
        const aesKeyHex = aesKey.toString(CryptoJS.enc.Hex);
        const aesKeyBytes = forge.util.hexToBytes(aesKeyHex); // ✅ OK กับ forge

        // 2. AES encrypt user data
        const data = JSON.stringify({ username, password });
        const iv = CryptoJS.enc.Hex.parse(aesKey.toString(CryptoJS.enc.Hex).substring(0, 32));
        const encryptedData = CryptoJS.AES.encrypt(data, aesKey, {
            iv: iv,
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7
        }).toString();

        // 3. RSA encrypt AES key
        const pubKey = forge.pki.publicKeyFromPem(publicKeyPem);
        const encryptedAesKey = forge.util.encode64(pubKey.encrypt(aesKeyBytes, 'RSAES-PKCS1-V1_5'));

        // 4. ส่งไป backend
        const response = await fetch("http://localhost:8080/flashcard/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                encryptedKey: encryptedAesKey,
                encryptedData: encryptedData
            })
        });

        const text = await response.text();
        setMsg(text);
    };

    return (
        <>
            <div className='registor-container'>
                <div>
                    Register
                </div>
                <div>
                    <label>Username:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />
                </div>
                <div>
                    <button onClick={handleTest}>
                        Register
                    </button>
                </div>
            </div>
            <div className='warring'>
                {msg && <p>{msg}</p>}
            </div>
        </>
    );

};

export default Register;
