import React, {useEffect, useState} from 'react'
import logo from '../assets/nhlLogo.png'
import {Navbar} from "../components/Navbar.jsx";
import Sidebar from "../components/Sidebar.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

const Homepage = () => {
    const [allStats, setAllStats] = useState([])

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const response = await fetch(`${apiBaseUrl}/api/stats/efficiency`);
                if (!response.ok) {
                    throw new Error("Failed to fetch stats");
                }
                const data = await response.json();
                setAllStats(data);
            } catch (error) {
                console.error("Error fetching all stats:", error);
            }
        };

        fetchStats();
    }, []);

    return (
        <Sidebar>
            <Navbar>
                <div className="homepage-bg">
                    <div className="topPlayers p-5 rounded-5"
                         style={{marginLeft: '100px', marginBottom: '30px', backgroundColor: 'rgba(0, 0, 0, 0.8)'}}>
                        <h2 className="mb-4"> Top players</h2>
                        {allStats.length > 0 ? (
                            <table style={{width: '100%'}}>
                                <thead>
                                <tr>
                                    <th style={{padding: '8px'}}>#</th>
                                    <th style={{padding: '8px'}}>Player</th>
                                    <th style={{padding: '8px'}}>Efficiency</th>
                                </tr>
                                </thead>
                                <tbody>
                                {allStats.slice(0, 5).map((player, index) => (
                                    <tr key={index}>
                                        <td style={{padding: '8px'}}>{index + 1}</td>
                                        <td style={{padding: '8px'}}>{player.playerName}</td>
                                        <td style={{padding: '8px', textAlign: 'right'}}>{player.efficiency.toFixed(3)}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        ) : (
                            <p className="text-center">Loading player stats... ⏳</p>
                        )}
                    </div>
                    <img className="logo"
                         style={{marginRight: '200px', marginTop: '50px'}}
                         src={logo}
                         alt="Logo"
                    ></img>
                    <div className="email-paragraph text-center">
                        <h4>If you have any questions or need help, please contact support.</h4>
                        <a href="mailto:tomko.vladimir1@gmail.com" className="text-white">
                            <i className="bi bi-envelope-at fs-1" style={{ cursor: 'pointer' }}></i>
                        </a>
                    </div>
                </div>
            </Navbar>
        </Sidebar>

    )
}

export default Homepage