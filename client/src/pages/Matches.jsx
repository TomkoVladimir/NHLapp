import React, {useEffect, useState} from 'react'
import {AddMatchPopUp} from "../components/AddMatchPopUp.jsx";
import Sidebar from "../components/Sidebar.jsx";
import {Navbar} from "../components/Navbar.jsx";
import {DeleteMatchPopUp} from "../components/DeleteMatchPopUp.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

const Matches = () => {
    const [matches, setMatches] = useState([]);
    const [matchToDelete, setMatchToDelete] = useState(null);
    const [showPopup, setShowPopup] = useState(false);
    const [loading, setLoading] = useState(true);
    const [reload, setReload] = useState(false);
    const [offset, setOffset] = useState(0);
    const limit = 15;

    useEffect(() => {
        fetchMatches(0); // on mount, load first batch
    }, [reload]);

    const fetchMatches = (offsetValue) => {
        setLoading(true);
        fetch(`${apiBaseUrl}/api/matches?limit=${limit}&offset=${offsetValue}`)
            .then(res => res.json())
            .then(data => {
                if (offsetValue === 0) {
                    setMatches(data); // initial load
                } else {
                    setMatches(prev => [...prev, ...data]); // append
                }
                setOffset(offsetValue);
            })
            .catch(err => console.error("Error fetching matches", err))
            .finally(() => setLoading(false));
    };

    const handleLoadMore = () => {
        fetchMatches(offset + limit);
    };

    return (
        <Sidebar>
            <Navbar>
                <div className="overall-bg" style={{height: '100vh'}}>
                    <div className="d-flex flex-column align-items-center justify-content-center h-100">
                        <h2 className="text-white mb-4 text-center p-3">Match Results</h2>
                        <button className="btn btn-primary mb-4 justify-content-center rounded-5" onClick={() => setShowPopup(true)}>
                            + Add Match
                        </button>
                        {showPopup && <AddMatchPopUp onClose={setShowPopup} setReload={setReload}></AddMatchPopUp>}
                        {loading ? (
                            <h3 className="text-center text-white bg-dark p-3 rounded-5">
                                Loading matches...⏳
                            </h3>
                        ) : (
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
                                                <th className="px-3">Home Player</th>
                                                <th className="px-3">Away Player</th>
                                                <th>Home Team</th>
                                                <th>Away Team</th>
                                                <th>Score</th>
                                                <th title="Overtime" className="px-3">OT</th>
                                                <th title="Home Team Points">HT PTS</th>
                                                <th title="Away Team Points">AT PTS</th>
                                                <th className="px-3">Action</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            {matches.map((match, index) => (
                                                <tr key={index}>
                                                    <td className="text-light bg-transparent px-4">{new Date(match.matchDate).toLocaleDateString('en-GB')}</td>
                                                    <td className="bg-transparent px-4"
                                                        style={{
                                                            color: match.htPoints > 1 ? '#B0DB9C' : match.htPoints == null ? 'white' : '#FE5D26',
                                                            fontWeight: match.htPoints > 1 ? 'bold' : '',
                                                        }}>
                                                        {match.htPlayerNickName}
                                                    </td>
                                                    <td className="bg-transparent px-4"
                                                        style={{
                                                            color: match.atPoints > 1 ? '#B0DB9C' : match.atPoints == null ? 'white' : '#FE5D26',
                                                            fontWeight: match.atPoints > 1 ? 'bold' : '',
                                                        }}>
                                                        {match.atPlayerNickName}
                                                    </td>
                                                    <td className="text-light bg-transparent px-4">
                                                        <div className="d-flex align-items-center justify-content-center gap-2">
                                                            <img src={match.homeTeam.logo}
                                                                 alt={match.homeTeam.name}
                                                                 style={{width: '30px', height: '30px'}}
                                                                 loading="lazy"/>
                                                            <span>{match.homeTeam.name}</span>
                                                        </div>
                                                    </td>
                                                    <td className="text-light bg-transparent px-4">
                                                        <div className="d-flex align-items-center justify-content-center gap-2">
                                                            {match.awayTeam.name}
                                                            <img src={match.awayTeam.logo}
                                                                 alt={match.awayTeam.name}
                                                                 style={{width: '30px', height: '30px'}}
                                                                 loading="lazy"/>
                                                        </div>
                                                    </td>
                                                    <td className="text-light bg-transparent w-score">{match.htScore} : {match.atScore}</td>
                                                    <td className="text-light bg-transparent">
                                                        {match.overTime ? <i className="bi bi-check"></i> : ""}
                                                    </td>
                                                    <td className="text-light bg-transparent">{match.htPoints}</td>
                                                    <td className="text-light bg-transparent">{match.atPoints}</td>
                                                    <td className="text-light bg-transparent">
                                                        {match.seriesId ? <i title="Series match can not be deleted"
                                                                             className="bi bi-x-square-fill text-secondary"></i> :
                                                            <div title="Delete match" onClick={() => setMatchToDelete(match.id)}
                                                                 style={{cursor: 'pointer'}}>
                                                                <i className="bi bi-x-square-fill text-danger"></i>
                                                            </div>}
                                                        {matchToDelete === match.id && (
                                                            <DeleteMatchPopUp
                                                                matchId={match.id}
                                                                onClose={() => setMatchToDelete(null)}
                                                                setReload={setReload}
                                                            />
                                                        )}
                                                    </td>
                                                </tr>
                                            ))}
                                            </tbody>
                                        </table>
                                        {matches.length % limit === 0 && (
                                            <div className="d-flex justify-content-center">
                                                <button className=" btn btn-outline-light m-2 p-1 px-3 rounded-5"
                                                        onClick={handleLoadMore}>
                                                    Load More
                                                </button>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </Navbar>
        </Sidebar>
    )
}

export default Matches