import React, {useEffect, useState} from "react";
import ValidationPopup from "./ValidationPopUp.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const AddPlayOffPopUp = ({onClose, setReload}) => {
    const [players, setPlayers] = useState([]);
    const [teams, setTeams] = useState([]);
    const [showValidationPopUp, setShowValidationPopup] = useState(false);
    const [validationError, setValidationError] = useState("");
    const [error, setError] = useState(null);
    const [newPlayOff, setNewPlayOff] = useState({
        playoffTitle: '',
        playerOneNickName: '',
        teamOneName: '',
        playerTwoNickName: '',
        teamTwoName: '',
        playerThreeNickName: '',
        teamThreeName: '',
        playerFourNickName: '',
        teamFourName: '',
    });

    // Fetch players and teams for dropdowns
    useEffect(() => {
        fetch(`${apiBaseUrl}/api/players`)
            .then(res => res.json())
            .then(setPlayers);

        fetch(`${apiBaseUrl}/api/teams`)
            .then(res => res.json())
            .then(setTeams);
    }, []);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (newPlayOff.playerOneNickName === newPlayOff.playerTwoNickName ||
            newPlayOff.playerOneNickName === newPlayOff.playerThreeNickName ||
            newPlayOff.playerOneNickName === newPlayOff.playerFourNickName ||
            newPlayOff.playerTwoNickName === newPlayOff.playerThreeNickName ||
            newPlayOff.playerTwoNickName === newPlayOff.playerFourNickName ||
            newPlayOff.playerThreeNickName === newPlayOff.playerFourNickName) {
            setError("Players can not be the same.");
            return;
        }

        if (!newPlayOff.playoffTitle) {
            setError("Please enter a Play-Off Title.");
            return;
        }

        if (!newPlayOff.playerOneNickName && !newPlayOff.playerTwoNickName && !newPlayOff.playerThreeNickName && !newPlayOff.playerFourNickName) {
            setError("Please select all players.");
            return;
        }

        if (!newPlayOff.teamOneName && !newPlayOff.teamTwoName && !newPlayOff.teamThreeName && !newPlayOff.teamFourName) {
            setError("Please select all teams.");
            return;
        }

        setShowValidationPopup(true);
    };

    const handleCodeConfirm = (code) => {
        fetch(`${apiBaseUrl}/api/playoffs`, {
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({...newPlayOff, validationCode: code})
        })
            .then(async res => {
                if (res.ok) {
                    setReload(prev => !prev);
                    setShowValidationPopup(false);
                    onClose(false);
                } else {
                    const errorData = await res.json();
                    setValidationError(errorData.error || "Failed to add new Play-Off.");
                }
            }).catch(err => {
            console.error("Error:", err);
        });
    };

    const handleCodeCancel = () => {
        setShowValidationPopup(false);
    };

    return (
        <>
            <div className="popup bg-white p-4 rounded-5 shadow position-absolute top-50 start-50 translate-middle"
                 style={{zIndex: 999, width: '400px'}}>
                <h4 className="mb-3 text-dark pb-3 text-center">New Play-Off 🏆</h4>

                <div className="mb-3">
                    <label htmlFor="playerName" className="form-label text-dark">Title</label>
                    <input
                        type="text"
                        maxLength={20}
                        className="form-control rounded-5"
                        id="playOffTitle"
                        value={newPlayOff.playoffTitle}
                        onChange={e => setNewPlayOff({...newPlayOff, playoffTitle: e.target.value})}
                        required
                        placeholder="Enter Play-Off Title"/>
                </div>

                <select className="form-select mb-2 rounded-5" value={newPlayOff.playerOneNickName}
                        onChange={e => setNewPlayOff({...newPlayOff, playerOneNickName: e.target.value})}>
                    <option value="">Select Player 1</option>
                    {players.map(((player, index) => (
                        <option key={index} value={player.nickName}>{player.nickName}</option>
                    )))}
                </select>

                <select required className="form-select mb-2 rounded-5" value={newPlayOff.teamOneName}
                        onChange={e => setNewPlayOff({...newPlayOff, teamOneName: e.target.value})}>
                    <option value="">Select Team 1</option>
                    {teams.map(team => (
                        <option key={team.id} value={team.name}>{team.name}</option>
                    ))}
                </select>

                <select className="form-select mb-2 rounded-5" value={newPlayOff.playerTwoNickName}
                        onChange={e => setNewPlayOff({...newPlayOff, playerTwoNickName: e.target.value})}>
                    <option value="">Select Player 2</option>
                    {players.map(((player, index) => (
                        <option key={index} value={player.nickName}>{player.nickName}</option>
                    )))}
                </select>

                <select required className="form-select mb-2 rounded-5" value={newPlayOff.teamTwoName}
                        onChange={e => setNewPlayOff({...newPlayOff, teamTwoName: e.target.value})}>
                    <option value="">Select Team 2</option>
                    {teams.map(team => (
                        <option key={team.id} value={team.name}>{team.name}</option>
                    ))}
                </select>

                <select className="form-select mb-2 rounded-5" value={newPlayOff.playerThreeNickName}
                        onChange={e => setNewPlayOff({...newPlayOff, playerThreeNickName: e.target.value})}>
                    <option value="">Select Player 3</option>
                    {players.map(((player, index) => (
                        <option key={index} value={player.nickName}>{player.nickName}</option>
                    )))}
                </select>

                <select required className="form-select mb-2 rounded-5" value={newPlayOff.teamThreeName}
                        onChange={e => setNewPlayOff({...newPlayOff, teamThreeName: e.target.value})}>
                    <option value="">Select Team 3</option>
                    {teams.map(team => (
                        <option key={team.id} value={team.name}>{team.name}</option>
                    ))}
                </select>

                <select className="form-select mb-2 rounded-5" value={newPlayOff.playerFourNickName}
                        onChange={e => setNewPlayOff({...newPlayOff, playerFourNickName: e.target.value})}>
                    <option value="">Select Player 4</option>
                    {players.map(((player, index) => (
                        <option key={index} value={player.nickName}>{player.nickName}</option>
                    )))}
                </select>

                <select required className="form-select mb-2 rounded-5" value={newPlayOff.teamFourName}
                        onChange={e => setNewPlayOff({...newPlayOff, teamFourName: e.target.value})}>
                    <option value="">Select Team 4</option>
                    {teams.map(team => (
                        <option key={team.id} value={team.name}>{team.name}</option>
                    ))}
                </select>
                {error && <div className="rounded-5 alert alert-danger mt-3 text-center">{error}</div>}
                <div className="d-flex justify-content-between pt-3">
                    <button className="btn btn-primary rounded-5" onClick={handleSubmit}>Submit</button>
                    <button className="btn btn-secondary rounded-5" onClick={() => onClose(false)}>Cancel</button>
                </div>
                {showValidationPopUp && (
                    <ValidationPopup onConfirm={handleCodeConfirm} onCancel={handleCodeCancel} error={validationError}/>
                )}
            </div>
        </>
    )
}
