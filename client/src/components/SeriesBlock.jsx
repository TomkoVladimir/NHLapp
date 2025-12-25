import React from "react";

export const SeriesBlock = ({createdAt, playerOne, teamOne, teamOneLogo, playerTwo, teamTwo, teamTwoLogo, completed, winner}) => {
    return (
        <div className="d-flex flex-column align-items-center">
            <h6 className="text-white">{new Intl.DateTimeFormat('en-GB').format(new Date(createdAt))}</h6>
            <div className="d-flex flex-row justify-content-center gap-4">
                <div className="d-flex flex-column justify-content-center align-items-center text-center">
                    <h5 className="text-white mb-4 mt-3">{playerOne}</h5>
                    <p className="text-white text-center">{teamOne}</p>
                    <img src={teamOneLogo}
                         alt={teamOne}
                         style={{width: '60px', height: '60px'}}
                         loading="lazy"/>
                </div>
                <div className="d-flex flex-column justify-content-center align-items-center mx-4">
                    <h2>:</h2>
                </div>
                <div className="d-flex flex-column justify-content-center align-items-center text-center">
                    <h5 className="text-white mb-4 mt-3">{playerTwo}</h5>
                    <p className="text-white">{teamTwo}</p>
                    <img src={teamTwoLogo}
                         alt={teamTwo}
                         style={{width: '60px', height: '60px'}}
                         loading="lazy"/>
                </div>
            </div>
            <p className="text-white mt-3">{completed ? winner + " 🏆" : "In progress ⌛️"}</p>
        </div>
    )
}
