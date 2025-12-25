import React, { useState } from "react";

const ValidationPopup = ({ onConfirm, onCancel, error }) => {
    const [code, setCode] = useState("");

    const handleSubmit = (e) => {
        e.preventDefault();
        onConfirm(code);
    };

    return (
        <div className="validation-overlay rounded-5">
            <div className="validation-modal rounded-5 m-3">
                <h4 className="text-dark pb-3 text-center">Enter Code</h4>
                <form onSubmit={handleSubmit}>
                    <input
                        type="password"
                        className="form-control mb-3 rounded-5 text-center"
                        placeholder="Validation Code"
                        value={code}
                        onChange={(e) => setCode(e.target.value)}
                        required
                    />
                    {error && <p className="text-danger">{error}</p>}
                    <div className="d-flex justify-content-between">
                        <button type="submit" className="btn btn-primary rounded-5">Confirm</button>
                        <button type="button" className="btn btn-secondary rounded-5" onClick={onCancel}>Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default ValidationPopup;