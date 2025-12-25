import dayjs from "dayjs";
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';

dayjs.extend(utc);
dayjs.extend(timezone);

import React, {useEffect, useState} from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import ValidationPopup from "./ValidationPopUp.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const AddMatchPopUp = ({onClose, setReload}) => {
    const [players, setPlayers] = useState([]);
    const [teams, setTeams] = useState([]);
    const [showValidationPopUp, setShowValidationPopup] = useState(false);
    const [validationError, setValidationError] = useState("");
    const [error, setError] = useState(null);
    const [newMatch, setNewMatch] = useState({
        matchDate: dayjs().tz('Europe/Bratislava').format('YYYY-MM-DDTHH:mm:ss'),
        htPlayerNickName: '',
        atPlayerNickName: '',
        homeTeam: '',
        awayTeam: '',
        htScore: 0,
        atScore: 0,
        overTime: false,
        seriesId: null
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

        if (newMatch.htPlayerNickName === newMatch.atPlayerNickName) {
            setError("Home and Away players can not be the same.");
            return;
        }

        if (!newMatch.htPlayerNickName) {
            setError("Please select a Home Player.");
            return;
        }

        if (!newMatch.atPlayerNickName) {
            setError("Please select a Away Player.");
            return;
        }

        if (!newMatch.homeTeam) {
            setError("Please select a Home Team.");
            return;
        }

        if (!newMatch.awayTeam) {
            setError("Please select a Away Team.");
            return;
        }

        if (newMatch.htScore === newMatch.atScore ) {
            setError("Match cannot end in a draw");
            return;
        }

        if (newMatch.overTime){
            if (newMatch.htScore - newMatch.atScore !== 1 && newMatch.htScore - newMatch.atScore !== -1) {
                setError("In overtime the score difference must be 1.");
                return;
            }
        }

        setShowValidationPopup(true);
    };

    const handleCodeConfirm = (code) => {
        fetch(`${apiBaseUrl}/api/matches`, {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({...newMatch, validationCode: code})
        })
            .then(async res => {
                if (res.ok) {
                    setReload(prev => !prev);
                    setShowValidationPopup(false);
                    onClose(false);
                } else {
                    const errorData = await res.json();
                    setValidationError(errorData.error || "Failed to add match.");
                }
            })
            .catch(err => {
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
                <h4 className="mb-3 text-center">New Match</h4>
                <div className="mb-3">
                    <label className="form-label m-2">Match Date</label>
                    <DatePicker
                        selected={newMatch.matchDate}
                        onChange={(date) => setNewMatch({ ...newMatch, matchDate: dayjs(date).tz('Europe/Bratislava').format('YYYY-MM-DDTHH:mm:ss')})}
                        className="form-control rounded-5"
                        dateFormat="dd/MM/yyyy"
                    />
                </div>

                <select className="form-select mb-2 rounded-5" value={newMatch.htPlayerNickName}
                        onChange={e => setNewMatch({...newMatch, htPlayerNickName: e.target.value})}>
                    <option value="">Select Home Player</option>
                    {players.map((player, index) => (
                        <option key={index} value={player.nickName}>{player.nickName}</option>
                    ))}
                </select>

                <select className="form-select mb-2 rounded-5" value={newMatch.atPlayerNickName}
                        onChange={e => setNewMatch({...newMatch, atPlayerNickName: e.target.value})}>
                    <option value="">Select Away Player</option>
                    {players.map((player, index) => (
                        <option key={index} value={player.nickName}>{player.nickName}</option>
                    ))}
                </select>

                <select className="form-select mb-2 rounded-5" value={newMatch.homeTeam} onChange={e => setNewMatch({...newMatch, homeTeam: e.target.value})}>
                    <option value="">Select Home Team</option>
                    {teams.map(team => (
                        <option key={team.id} value={team.name}>{team.name}</option>
                    ))}
                </select>

                <select className="form-select mb-2 rounded-5" value={newMatch.awayTeam} onChange={e => setNewMatch({...newMatch, awayTeam: e.target.value})}>
                    <option value="">Select Away Team</option>
                    {teams.map(team => (
                        <option key={team.id} value={team.name}>{team.name}</option>
                    ))}
                </select>

                <input type="number" className="form-control mb-2 rounded-5" placeholder="Home Score" value={newMatch.htScore} min="0"
                       onChange={e => setNewMatch({...newMatch, htScore: Math.max(0, +e.target.value)})}/>
                <input type="number" className="form-control mb-2 rounded-5" placeholder="Away Score" value={newMatch.atScore} min="0"
                       onChange={e => setNewMatch({...newMatch, atScore: Math.max(0, +e.target.value)})}/>

                <div className="form-check mb-3">
                    <input className="form-check-input rounded-5" type="checkbox" checked={newMatch.overTime}
                           onChange={e => setNewMatch({...newMatch, overTime: e.target.checked})}/>
                    <label className="form-check-label">Overtime</label>
                </div>
                {error && <div className="rounded-5 alert alert-danger mt-3 text-center">{error}</div>}
                <div className="d-flex justify-content-between">
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
