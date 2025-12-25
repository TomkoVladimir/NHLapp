import React, {useEffect, useState} from "react";
import DatePicker from "react-datepicker";
import ValidationPopup from "./ValidationPopUp.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const AddSeriesPopUp = ({onClose, setReload}) => {
    const [players, setPlayers] = useState([]);
    const [teams, setTeams] = useState([]);
    const [showValidationPopUp, setShowValidationPopup] = useState(false);
    const [validationError, setValidationError] = useState("");
    const [error, setError] = useState(null);
    const [newSeries, setNewSeries] = useState({
        playerOneNickName: '',
        playerTwoNickName: '',
        teamOneName: '',
        teamTwoName: '',
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

        if (newSeries.playerOneNickName === newSeries.playerTwoNickName) {
            setError("Players One and Two can not be the same.");
            return;
        }

        if (!newSeries.playerOneNickName) {
            setError("Please select a Home Player.");
            return;
        }

        if (!newSeries.playerTwoNickName) {
            setError("Please select a Away Player.");
            return;
        }

        if (!newSeries.teamOneName) {
            setError("Please select a Home Team.");
            return;
        }

        if (!newSeries.teamTwoName) {
            setError("Please select a Away Team.");
            return;
        }

        setShowValidationPopup(true);
    };

    const handleCodeConfirm = (code) => {
        fetch(`${apiBaseUrl}/api/series`, {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({...newSeries, validationCode: code})
        })
            .then(async res => {
                if (res.ok) {
                    setReload(prev => !prev);
                    setShowValidationPopup(false);
                    onClose(false);
                } else {
                    const errorData = await res.json();
                    setValidationError(errorData.error || "Failed to add series.");
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
                <h4 className="mb-3 text-dark pb-3 text-center">New Series</h4>

                <select className="form-select mb-2 rounded-5" value={newSeries.playerOneNickName}
                        onChange={e => setNewSeries({...newSeries, playerOneNickName: e.target.value})}>
                    <option value="">Select Home Player</option>
                    {players.map(((player, index) => (
                        <option key={index} value={player.nickName}>{player.nickName}</option>
                    )))}
                </select>

                <select className="form-select mb-2 rounded-5" value={newSeries.playerTwoNickName}
                        onChange={e => setNewSeries({...newSeries, playerTwoNickName: e.target.value})}>
                    <option value="">Select Away Player</option>
                    {players.map(((player, index) => (
                        <option key={index} value={player.nickName}>{player.nickName}</option>
                    )))}
                </select>

                <select required className="form-select mb-2 rounded-5" value={newSeries.teamOneName} onChange={e => setNewSeries({...newSeries, teamOneName: e.target.value})}>
                    <option value="">Select Home Team</option>
                    {teams.map(team => (
                        <option key={team.id} value={team.name}>{team.name}</option>
                    ))}
                </select>

                <select required className="form-select mb-2 rounded-5" value={newSeries.teamTwoName} onChange={e => setNewSeries({...newSeries, teamTwoName: e.target.value})}>
                    <option value="">Select Away Team</option>
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
