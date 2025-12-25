import React, {useState} from "react";
import ValidationPopup from "./ValidationPopUp.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const DeleteMatchPopUp = ({matchId, onClose, setReload}) => {
    const [showValidationPopUp, setShowValidationPopup] = useState(false);
    const [validationError, setValidationError] = useState("");

    const handleDelete = (e) => {
        e.preventDefault();

        setShowValidationPopup(true);
    };

    const handleCodeConfirm = async (validationCode) => {
        try {
            const response = await fetch(`${apiBaseUrl}/api/matches/${matchId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({validationCode})
            });

            if (response.status === 204) {
                alert("Match deleted.");
                setReload(prev => !prev); // trigger refetch
                onClose(false);           // close popup
            } else {
                const data = await response.json();
                setValidationError(data.error || "Failed to delete match.");
            }
        } catch (err) {
            console.error(err);
            setValidationError("Error deleting match.");
        }
    };

    const handleCodeCancel = () => {
        setShowValidationPopup(false);
    };

    return (
        <>
            <div className="popup bg-white p-4 rounded-5 shadow position-absolute top-50 start-50 translate-middle"
                 style={{zIndex: 999, width: '300px'}}>
                <h4 className="mb-3 text-dark pb-3">Are you sure ?</h4>
                <div className="d-flex justify-content-between">
                    <button className="btn btn-danger rounded-5" onClick={handleDelete}>Delete</button>
                    <button className="btn btn-secondary rounded-5" onClick={() => onClose(false)}>Cancel</button>
                </div>
                {showValidationPopUp && (
                    <ValidationPopup onConfirm={handleCodeConfirm} onCancel={handleCodeCancel} error={validationError}/>
                )}
            </div>
        </>
    )
}
