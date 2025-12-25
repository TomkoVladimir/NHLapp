import React, {useEffect, useState} from 'react'
import Sidebar from "../components/Sidebar.jsx";
import {Navbar} from "../components/Navbar.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

const Stats = () => {
    const [stats, setStats] = useState([]);
    const [favTeams, setFavTeams] = useState([]);
    const [mergedData, setMergedData] = useState([]);

    useEffect(() => {
        // Fetch stats
        fetch(`${apiBaseUrl}/api/stats`)
            .then(res => res.json())
            .then(data => setStats(data))
            .catch(console.error);

        // Fetch favorite teams
        fetch(`${apiBaseUrl}/api/player-team-counters/favouriteTeams`)
            .then(res => res.json())
            .then(data => setFavTeams(data))
            .catch(console.error);
    }, []);

    useEffect(() => {
        if (stats.length && favTeams.length) {
            // Merge by playerId
            const merged = stats.map(stat => {
                const favTeam = favTeams.find(team => team.nickName === stat.player);
                return {
                    ...stat,
                    favTeam: favTeam ? favTeam.team : null,
                };
            });
            setMergedData(merged);
        }
    }, [stats, favTeams]);

    return (
        <Sidebar>
            <Navbar>
                <div className="stats-bg" >
                    <div className="d-flex flex-column align-items-center justify-content-center h-100">
                        <h2 className="text-dark mb-4 text-center px-4 py-2 rounded-5 bg-light">Statistics of Players</h2>
                        {stats.length > 0 ? (
                            <div className=" rounded-4 overflow-hidden border border-light">
                                <div style={{
                                    maxHeight: '75vh',
                                    overflowY: 'auto',
                                    overflowX: 'auto',
                                    backgroundColor: 'rgba(0, 0, 0, 0.80)',
                                }}>
                                    <div className="table-responsive table-width-responsive">
                                        <table className="table custom-striped table-dark text-center align-middle m-0">
                                            <thead>
                                            <tr>
                                                <th>Nickname</th>
                                                <th>Favorite Team</th>
                                                <th title="Games Played">GP</th>
                                                <th title="Wins">W</th>
                                                <th title="Losses">L</th>
                                                <th title="Overtime Wins">OTW</th>
                                                <th title="Overtime Losses">OTL</th>
                                                <th title="Points">PTS</th>
                                                <th title="Goals For">GF</th>
                                                <th title="Goals Against">GA</th>
                                                <th title="Goal Difference">DIFF</th>
                                                <th title="Points per Game">PPG</th>
                                                <th title="Goals For per Game">GFPG</th>
                                                <th title="Goals Against per Game">GAPG</th>
                                                <th title="Win percentage">W%</th>
                                                <th title="Loss percentage">L%</th>

                                            </tr>
                                            </thead>
                                            <tbody>
                                            {[...mergedData]
                                                .sort((a, b) => (b.points / b.matchesPlayed) - (a.points / a.matchesPlayed))
                                                .map((player, index) => (
                                                    <tr key={index}>
                                                        <td className="text-light bg-transparent">{player.player}</td>
                                                        <td className="text-light bg-transparent">
                                                            {player.favTeam ? (
                                                                <div className="d-flex align-items-center justify-content-center gap-2">
                                                                    <img
                                                                        src={player.favTeam.logo}
                                                                        alt={player.favTeam.name}
                                                                        style={{width: '30px', height: '30px'}}
                                                                        loading="lazy"
                                                                    />
                                                                    <span>{player.favTeam.name}</span>
                                                                </div>
                                                            ) : (
                                                                'N/A'
                                                            )}
                                                        </td>
                                                        <td className="text-light bg-transparent">{player.matchesPlayed}</td>
                                                        <td className="text-light bg-transparent">{player.wins}</td>
                                                        <td className="text-light bg-transparent">{player.losses}</td>
                                                        <td className="text-light bg-transparent">{player.extraTimeWins}</td>
                                                        <td className="text-light bg-transparent">{player.extraTimeLosses}</td>
                                                        <td className="text-light bg-transparent">{player.points}</td>
                                                        <td className="text-light bg-transparent">{player.scoredGoals}</td>
                                                        <td className="text-light bg-transparent">{player.concededGoals}</td>
                                                        <td className="text-light bg-transparent">{player.scoredGoals - player.concededGoals}</td>
                                                        <td className="bg-light text-dark">{(player.points / player.matchesPlayed).toFixed(2)}</td>
                                                        <td className="text-light bg-transparent">{(player.scoredGoals / player.matchesPlayed).toFixed(2)}</td>
                                                        <td className="text-light bg-transparent">{(player.concededGoals / player.matchesPlayed).toFixed(2)}</td>
                                                        <td className="text-light bg-transparent">{((player.wins + player.extraTimeWins) / player.matchesPlayed).toFixed(2)}</td>
                                                        <td className="text-light bg-transparent">{((player.losses + player.extraTimeLosses) / player.matchesPlayed).toFixed(2)}</td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        ) : (
                            <h3 className="text-center text-white bg-dark p-3 rounded-5">Loading stats... ⏳</h3>
                        )}
                    </div>
                </div>
            </Navbar>
        </Sidebar>
    )
}

export default Stats