import Sidebar from "../components/Sidebar.jsx";
import {Navbar} from "../components/Navbar.jsx";
import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {EditMatchPopUp} from "../components/EditMatchPopUp.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const SeriesDetail = () => {
    const {id} = useParams(); // gets seriesId from the URL
    const [series, setSeries] = useState(null);
    const [matches, setMatches] = useState([]);
    const [selectedMatch, setSelectedMatch] = useState(null);
    const [reload, setReload] = useState(false);
    const navigate = useNavigate();

    // Fetch series details
    useEffect(() => {
        fetch(`${apiBaseUrl}/api/series/${id}`)
            .then(res => res.json())
            .then(data => {
                setSeries(data);

                // Then fetch all matches
                if (data.matchIds?.length) {
                    Promise.all(data.matchIds.map(matchId =>
                        fetch(`${apiBaseUrl}/api/matches/${matchId}`).then(res => res.json())
                    )).then(matches => {
                        const sorted = matches.sort((a, b) => a.id - b.id); // sort ascending
                        setMatches(sorted);
                    });
                }
            });
    }, [id, reload]);

    if (!series) return <div className="text-white">Loading...</div>;

    return (
        <Sidebar>
            <Navbar>
                <div className="series-bg p-4 text-white">
                    <div className="container mt-4 text-white d-flex flex-column align-items-center">
                        <div className="d-flex gap-5 align-items-center mb-4 justify-content-center">
                            <div onClick={() => navigate(`/series`)}
                                 style={{
                                     cursor: 'pointer',
                                 }}>
                                <i className="bi bi-arrow-left fs-1"></i>
                            </div>
                            <h2 className="m-0">Series Details #{id}</h2>
                        </div>
                        <div className="finals"
                             style={{
                                 display: 'flex', flexDirection: 'column', height: '100vh', width: '100vw',
                             }}>
                            <div style={{flexGrow: 1, overflowY: 'auto', paddingBottom: '250px', width: '100%'}}>
                                <div className="series-details">
                                    <div className="mb-3">
                                        <strong>Created:</strong>{' '}
                                        {new Date(series.createdAt).toLocaleDateString('en-GB')}
                                    </div>
                                    <div className="d-flex gap-4 align-items-center mb-4">
                                        <div className="text-center">
                                            <h5>{series.playerOneNickName}</h5>
                                            <p>{series.teamOneName.name}</p>
                                            <img src={series.teamOneName.logo} alt="Team 1" width={60}/>
                                        </div>
                                        <strong>vs</strong>
                                        <div className="text-center">
                                            <h5>{series.playerTwoNickName}</h5>
                                            <p>{series.teamTwoName.name}</p>
                                            <img src={series.teamTwoName.logo} alt="Team 2" width={60}/>
                                        </div>
                                    </div>
                                    {series.winner && <h2 className="text-white pb-3">🏆 {series.winner} 🏆</h2>}
                                    <div className="bg-light mb-3 d-flex flex-row justify-content-center align-items-center rounded-2 gap-3 px-3 p-1">
                                        <h4 className="mb-0 text-dark fw-bold">{series.playerOneWins}</h4>
                                        <h5 className="mb-0 text-dark">:</h5>
                                        <h4 className="mb-0 text-dark fw-bold">{series.playerTwoWins}</h4>
                                    </div>
                                    <h3 className="pb-3">Matches</h3>
                                    {matches.length > 0 ? (
                                        <div className=" rounded-4 overflow-hidden border border-light">
                                            <div style={{
                                                maxHeight: '75vh',
                                                overflowY: 'auto',
                                                backgroundColor: 'rgba(0, 0, 0, 0.80)',
                                                boxShadow: '0 4px 12px rgba(0, 0, 0, 0.5)',
                                            }}>
                                                <div className="table-responsive table-width-responsive">
                                                    <table className="table custom-striped table-light text-center align-middle m-0">
                                                        <thead>
                                                        <tr>
                                                            <th>Date</th>
                                                            <th>Home</th>
                                                            <th>Away</th>
                                                            <th>Score</th>
                                                            <th>OT</th>
                                                            <th></th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        {matches.map(match => (
                                                            <tr key={match.id}>
                                                                <td className="bg-transparent px-4 text-white">{new Date(match.matchDate).toLocaleDateString('en-GB')}</td>
                                                                <td className="bg-transparent px-4"
                                                                    style={{
                                                                        color: match.htScore > match.atScore ? '#B0DB9C' : match.htScore == null ? 'white' : '#FE5D26',
                                                                    }}
                                                                >{match.homeTeam.name}</td>
                                                                <td className="bg-transparent px-4"
                                                                    style={{
                                                                        color: match.htScore < match.atScore ? '#B0DB9C' : match.atScore == null ? 'white' : '#FE5D26',
                                                                    }}>
                                                                    {match.awayTeam.name}</td>
                                                                <td className="bg-transparent px-4 text-white w-score">{match.htScore} : {match.atScore}</td>
                                                                <td className="bg-transparent px-4 text-white">{match.overTime ?
                                                                    <i className="bi bi-check"></i> : ""}</td>
                                                                <td className="bg-transparent px-4 text-white">
                                                                    <i
                                                                        className="bi bi-pencil-square"
                                                                        role="button"
                                                                        onClick={() => setSelectedMatch(match)}
                                                                    ></i>
                                                                </td>
                                                            </tr>
                                                        ))}
                                                        </tbody>
                                                    </table>
                                                    {selectedMatch && (
                                                        <EditMatchPopUp
                                                            match={selectedMatch}
                                                            onClose={() => setSelectedMatch(null)}
                                                            onSubmit={() => setReload(!reload)}
                                                        />
                                                    )}
                                                </div>
                                            </div>
                                        </div>
                                    ) : (
                                        <h3 className="text-center text-white bg-dark p-3 rounded-5">Loading matches... ⏳</h3>
                                    )}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </Navbar>
        </Sidebar>
    )
}
