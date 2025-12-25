import React, {useState} from 'react';
import ValidationPopup from "./ValidationPopUp.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const EditMatchPopUp = ({match, onClose, onSubmit}) => {
    const [htScore, setHtScore] = useState(match.htScore);
    const [atScore, setAtScore] = useState(match.atScore);
    const [overTime, setOverTime] = useState(match.overTime || false);
    const [showValidationPopUp, setShowValidationPopup] = useState(false);
    const [validationError, setValidationError] = useState("");
    const [error, setError] = useState(null);

    const handleSubmit = (e) => {
        e.preventDefault();

        if (htScore === null) {
            setError("Add a Home score.");
            return;
        }

        if (atScore === null) {
            setError("Add an Away score.");
            return;
        }

        if (htScore === atScore) {
            setError("Match cannot end in a draw");
            return;
        }

        if (overTime) {
            if (htScore - atScore !== 1 && htScore - atScore !== -1) {
                setError("In overtime the score difference must be 1.");
                return;
            }
        }

        setShowValidationPopup(true);
    };

    const handleCodeConfirm = (code) => {
        fetch(`${apiBaseUrl}/api/matches/${match.id}/finish?htScore=${htScore}&atScore=${atScore}&overTime=${overTime}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                htScore,
                atScore,
                overTime,
                validationCode: code
            })
        }).then(async res => {
            if (res.ok) {
                onSubmit(); // trigger refetch or update
                setShowValidationPopup(false);
                onClose();  // close popup
            } else {
                const errorData = await res.json();
                setValidationError(errorData.error || "Failed to add update match.");
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
        <div className="popup bg-white p-4 shadow position-absolute top-50 start-50 translate-middle rounded-5"
             style={{zIndex: 999, width: '400px'}}>
            <h4 className="mb-3 text-dark">Edit Match #{match.id}</h4>

            <input type="number" className="form-control mb-2 rounded-5" placeholder="Home Score" value={htScore ?? ""} min="0"
                   onChange={e => setHtScore(Math.max(0, +e.target.value))}/>
            <input type="number" className="form-control mb-2 rounded-5" placeholder="Away Score" value={atScore ?? ""} min="0"
                   onChange={e => setAtScore(Math.max(0, +e.target.value))}/>

            <div className="form-check mb-3">
                <input className="form-check-input rounded-5" type="checkbox" checked={overTime ?? ""} id="otCheck"
                       onChange={e => setOverTime(e.target.checked)}/>
                <label className="form-check-label text-dark" htmlFor="otCheck">Overtime</label>
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
    );
};