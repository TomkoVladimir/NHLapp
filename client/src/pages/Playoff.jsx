import React, {useEffect, useState} from 'react'
import Sidebar from "../components/Sidebar.jsx";
import {Navbar} from "../components/Navbar.jsx";
import {useNavigate} from "react-router-dom";
import {AddPlayOffPopUp} from "../components/AddPlayOffPopUp.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

const Playoff = () => {
    const [playOffList, setPlayOffList] = useState([]);
    const [showPopup, setShowPopup] = useState(false);
    const [loading, setLoading] = useState(true);
    const [reload, setReload] = useState(false);
    const navigate = useNavigate();
    const [offset, setOffset] = useState(0);
    const limit = 5;

    useEffect(() => {
        fetchPlayOffs(0); // on mount, load first batch
    }, [reload]);

    const fetchPlayOffs = (offsetValue) => {
        setLoading(true);
        fetch(`${apiBaseUrl}/api/playoffs?limit=${limit}&offset=${offsetValue}`)
            .then(res => res.json())
            .then(data => {
                if (offsetValue === 0) {
                    setPlayOffList(data); // initial load
                } else {
                    setPlayOffList(prev => [...prev, ...data]); // append
                }
                setOffset(offsetValue);
            })
            .catch(err => console.error("Error fetching playoffs", err))
            .finally(() => setLoading(false));
    };

    const handleLoadMore = () => {
        fetchPlayOffs(offset + limit);
    };

    return (
        <Sidebar>
            <Navbar>
                <div className="playoff-bg p-4 text-white">
                    <div className="d-flex flex-column align-items-center justify-content-center h-100">
                        <h2 className="m-5 text-center">Play-Off</h2>
                        <button className="btn btn-primary mb-4 justify-content-center rounded-5" onClick={() => setShowPopup(true)}>
                            + New Play-Off
                        </button>
                        {showPopup && <AddPlayOffPopUp onClose={setShowPopup} setReload={setReload}></AddPlayOffPopUp>}
                        {loading ? (
                            <h3 className="text-center text-white bg-dark p-3 rounded-5">
                                Loading play-offs... ⏳
                            </h3>
                        ) : (
                            <div className="d-flex flex-column align-items-center"
                                 style={{
                                     height: '100vh',
                                     overflowY: 'auto',
                                     padding: '20px',
                                     width: '100%',
                                 }}>
                                {playOffList.map(playOff => (
                                    <div key={playOff.playoffId}
                                         onClick={() => navigate(`/playoff/${playOff.playoffId}`)}
                                         className="responsive-width-series mb-4 rounded-5 p-3 text-white"
                                         style={{
                                             cursor: 'pointer',
                                             backgroundColor: playOff.finalsCompleted ? 'rgba(30, 130, 76, 0.90)' : 'rgba(0, 0, 0, 0.90)',
                                         }}
                                    >
                                        <div className="d-flex flex-column align-items-center">
                                            <h6 className="text-white">{new Intl.DateTimeFormat('en-GB').format(new Date(playOff.createdAt))}</h6>
                                            <h4 className="text-white mb-4 mt-3">{playOff.playoffTitle}</h4>
                                            <div className="d-flex flex-row justify-content-center gap-4">
                                                <div className="d-flex flex-column justify-content-center align-items-center text-center">
                                                    <h5 className="text-white mt-3">{playOff.series[0].playerOneNickName}</h5>
                                                    <p className="text-white text-center m-0">{playOff.series[0].teamOneName.name}</p>
                                                    <img src={playOff.series[0].teamOneName.logo}
                                                         alt={playOff.series[0].teamOneName.name}
                                                         style={{width: '60px', height: '60px'}}
                                                         loading="lazy"/>
                                                </div>
                                                <div className="d-flex flex-column justify-content-center align-items-center text-center">
                                                    <h5 className="text-white mt-3">{playOff.series[0].playerTwoNickName}</h5>
                                                    <p className="text-white text-center m-0">{playOff.series[0].teamTwoName.name}</p>
                                                    <img src={playOff.series[0].teamTwoName.logo}
                                                         alt={playOff.series[0].teamTwoName.name}
                                                         style={{width: '60px', height: '60px'}}
                                                         loading="lazy"/>
                                                </div>
                                            </div>
                                            <div className="d-flex flex-row justify-content-center gap-5">
                                                <div className="d-flex flex-column justify-content-center align-items-center text-center">
                                                    <h5 className="text-white mt-3">{playOff.series[1].playerOneNickName}</h5>
                                                    <p className="text-white text-center m-0">{playOff.series[1].teamOneName.name}</p>
                                                    <img src={playOff.series[1].teamOneName.logo}
                                                         alt={playOff.series[1].teamOneName.name}
                                                         style={{width: '60px', height: '60px'}}
                                                         loading="lazy"/>
                                                </div>
                                                <div className="d-flex flex-column justify-content-center align-items-center text-center">
                                                    <h5 className="text-white mt-3">{playOff.series[1].playerTwoNickName}</h5>
                                                    <p className="text-white text-center m-0">{playOff.series[1].teamTwoName.name}</p>
                                                    <img src={playOff.series[1].teamTwoName.logo}
                                                         alt={playOff.series[1].teamTwoName.name}
                                                         style={{width: '60px', height: '60px'}}
                                                         loading="lazy"/>
                                                </div>
                                            </div>
                                            {playOff.finalsCompleted && (
                                                <div className="p-2 pt-4">
                                                    <table>
                                                        <tbody>
                                                        <tr>
                                                            <td className="text-white text-start px-1">1st</td>
                                                            <td className="text-white text-end px-1">🥇</td>
                                                            <td className="text-white px-1">{playOff.winner}</td>
                                                        </tr>
                                                        <tr>
                                                            <td className="text-white text-start px-1">2nd</td>
                                                            <td className="text-white text-end px-1">🥈</td>
                                                            <td className="text-white px-1">{playOff.secondPlace}</td>
                                                        </tr>
                                                        <tr>
                                                            <td className="text-white text-start px-1">3rd</td>
                                                            <td className="text-white text-end px-1">🥉</td>
                                                            <td className="text-white px-1">{playOff.thirdPlace}</td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            )}
                                            <p className="text-white fw-bold mt-3">{playOff.finalsCompleted ? "Congratulations " + playOff.winner + " 🏆" : "In" +
                                                " progress ⌛️"}</p>
                                        </div>
                                    </div>
                                ))}
                                {playOffList.length > 0 && playOffList.length % limit === 0 && (
                                    <div className="d-flex justify-content-center">
                                        <button className=" btn btn-outline-light m-2 p-1 px-3 rounded-5"
                                                onClick={handleLoadMore}>
                                            Load More
                                        </button>
                                    </div>
                                )}
                            </div>)}
                    </div>
                </div>
            </Navbar>
        </Sidebar>
    )
}

export default Playoff