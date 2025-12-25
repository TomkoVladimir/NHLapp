import React, {useEffect, useState} from 'react';
import Sidebar from "../components/Sidebar.jsx";
import {Navbar} from "../components/Navbar.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

const Teams = () => {
    const [teams, setTeams] = useState([]);
    const [randomTeam, setRandomTeam] = useState(null);
    const [spinning, setSpinning] = useState(false);

    useEffect(() => {
        fetch(`${apiBaseUrl}/api/teams`)
            .then(res => res.json())
            .then(setTeams)
            .catch(err => console.error("Failed to fetch teams:", err));
    }, []);

    const generateRandomTeam = () => {
        if (!teams.length) return;

        setSpinning(true);
        let intervalCount = 0;
        const spinInterval = setInterval(() => {
            const random = teams[Math.floor(Math.random() * teams.length)];
            setRandomTeam(random);
            intervalCount++;
            if (intervalCount >= 20) { // ~2 seconds if interval is 100ms
                clearInterval(spinInterval);
                setSpinning(false);
            }
        }, 100);
    };

    return (
        <Sidebar>
            <Navbar>
                <div className="teams-bg p-5 d-flex flex-column align-items-center justify-content-center">
                    <h2 className="text-white mb-4">Team Standings 24/25</h2>
                    <div className="d-flex flex-column flex-md-row">
                        <button className="btn m-4 rounded-5" style={{backgroundColor: 'white'}} onClick={generateRandomTeam} disabled={spinning}>
                            🎲 Generate Random Team
                        </button>
                        {randomTeam && (
                            <div
                                className="d-flex flex-row text-center text-white m-4 rounded justify-content-center align-items-center rounded-5 gap-2"
                                style={{
                                    backgroundColor: 'rgba(0,0,0,0.9)',
                                    width: '280px'
                                }}>
                                <h5 style={{margin: '8px'}}>{randomTeam.name}</h5>
                                <img src={randomTeam.logo} alt={randomTeam.name} style={{width: '40px'}}/>
                            </div>
                        )}
                    </div>
                    {teams.length > 0 ? (
                        <div
                            className="responsive-width table-container"
                            style={{
                                maxHeight: '700px',
                                overflowY: 'auto',
                                backgroundColor: 'rgba(255, 255, 255, 0.80)',
                                borderRadius: '16px',
                                boxShadow: '0 4px 12px rgba(0, 0, 0, 0.5)'
                            }}
                        >
                            <table className="table table-hover text-center align-middle mb-0" style={{backgroundColor: 'transparent'}}>
                                <thead className="table-dark" style={{position: 'sticky', top: 0, zIndex: 1}}>
                                <tr>
                                    <th>#</th>
                                    <th>Name</th>
                                    <th>Logo</th>
                                </tr>
                                </thead>
                                <tbody>
                                {teams.map(team => (
                                    <tr key={team.id}>
                                        <td className="fw-bold bg-transparent">{team.id}</td>
                                        <td className="bg-transparent">{team.name}</td>
                                        <td className="bg-transparent">
                                            <img src={team.logo} alt={team.name} style={{width: '50px', borderRadius: '8px'}}/>
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        </div>
                    ) : (
                        <h3 className="text-center text-white bg-dark p-3 rounded-5">Loading teams... ⏳</h3>
                    )}
                </div>
            </Navbar>
        </Sidebar>
    );
};

export default Teams;