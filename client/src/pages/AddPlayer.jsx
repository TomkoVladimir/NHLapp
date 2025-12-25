import React, {useState} from 'react'
import Sidebar from "../components/Sidebar.jsx";
import {Navbar} from "../components/Navbar.jsx";
import ValidationPopup from "../components/ValidationPopUp.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

const AddPlayer = () => {
    const [confirmation, setConfirmation] = useState(false);
    const [showValidationPopUp, setShowValidationPopup] = useState(false);
    const [validationError, setValidationError] = useState("");
    const [error, setError] = useState(null);
    const [player, setPlayer] = useState({
        firstName: '',
        lastName: '',
        nickName: ''
    });

    const handleCodeConfirm = (code) => {
        fetch(`${apiBaseUrl}/api/players`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({...player, validationCode: code})
        })
            .then(async res => {
                if (res.ok) {
                    setPlayer({firstName: '', lastName: '', nickName: ''});
                    setShowValidationPopup(false);
                    setConfirmation(true);
                    setTimeout(() => setConfirmation(false), 3000);
                } else {
                    // Try to parse JSON error message from backend
                    const errorData = await res.json();
                    setValidationError(errorData.error || "Failed to add player.");
                }
            })
            .catch(err => {
                console.error("Error:", err);
            });
    };

    const handleChange = (e) => {
        const {id, value} = e.target;
        setPlayer(prev => ({...prev, [id]: value}));
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (player.nickName.length > 15) {
            setError("Nickname cannot be longer than 15 characters.");
            return;
        }
        setShowValidationPopup(true); // Open validation code input
    };

    const handleCodeCancel = () => {
        setShowValidationPopup(false);
    };

    return (<>
            <Sidebar>
                <Navbar>
                    <div className="add-player-bg d-flex flex-column align-items-center justify-content-center">
                        <div className="container responsive-width-addPlayer">
                            <h2 className="text-center text-white p-4">Add New Player</h2>
                            <form className="bg-white p-4 rounded-5 shadow" onSubmit={handleSubmit}>
                                <div className="mb-3">
                                    <label htmlFor="playerName" className="form-label">First Name*</label>
                                    <input
                                        type="text"
                                        maxLength={20}
                                        className="form-control rounded-5"
                                        id="firstName"
                                        value={player.firstName}
                                        onChange={handleChange}
                                        required
                                        placeholder="Enter first name"/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="playerName" className="form-label">Last Name*</label>
                                    <input
                                        type="text"
                                        maxLength={20}
                                        className="form-control rounded-5"
                                        id="lastName"
                                        value={player.lastName}
                                        onChange={handleChange}
                                        required
                                        placeholder="Enter last name"/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="playerNickName" className="form-label">Nickname*</label>
                                    <input
                                        type="text"
                                        maxLength={12}
                                        className="form-control rounded-5"
                                        id="nickName"
                                        value={player.nickName}
                                        onChange={handleChange}
                                        required
                                        placeholder="Enter player nickname"/>
                                </div>
                                {error && <p className="text-danger mt-3 text-center">{error}</p>}
                                {confirmation ?
                                    <p className="text-success text-center">Player added successfully!</p>
                                    :
                                    <div className="d-flex justify-content-center">
                                        <button type="submit" className="btn btn-primary rounded-5 ">Add Player</button>
                                    </div>
                                }
                            </form>
                            {showValidationPopUp && (
                                <ValidationPopup onConfirm={handleCodeConfirm} onCancel={handleCodeCancel} error={validationError}/>
                            )}
                        </div>
                    </div>
                </Navbar>
            </Sidebar>
        </>
    )
}

export default AddPlayer