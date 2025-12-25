import React, {useEffect, useState} from 'react'
import Sidebar from "../components/Sidebar.jsx";
import {Navbar} from "../components/Navbar.jsx";
import {useNavigate} from "react-router-dom";
import {AddSeriesPopUp} from "../components/AddSeriesPopUp.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

const Series = () => {
    const [seriesList, setSeriesList] = useState([]);
    const [showPopup, setShowPopup] = useState(false);
    const [loading, setLoading] = useState(true);
    const [reload, setReload] = useState(false);
    const navigate = useNavigate();
    const [offset, setOffset] = useState(0);
    const limit = 8;

    useEffect(() => {
        fetchSeries(0); // on mount, load first batch
    }, [reload]);

    const fetchSeries = (offsetValue) => {
        setLoading(true);
        fetch(`${apiBaseUrl}/api/series?limit=${limit}&offset=${offsetValue}`)
            .then(res => res.json())
            .then(data => {
                if (offsetValue === 0) {
                    setSeriesList(data); // initial load
                } else {
                    setSeriesList(prev => [...prev, ...data]); // append
                }
                setOffset(offsetValue);
            })
            .catch(err => console.error("Error fetching series", err))
            .finally(() => setLoading(false));
    };

    const handleLoadMore = () => {
        fetchSeries(offset + limit);
    };

    return (
        <Sidebar>
            <Navbar>
                <div className="series-bg text-white">
                    <div className="d-flex flex-column align-items-center justify-content-center h-100">
                        <h2 className="m-5 pt-4 text-center">Series</h2>
                        <button className="btn btn-primary mb-4 justify-content-center rounded-5" onClick={() => setShowPopup(true)}>
                            + Add Series
                        </button>
                        {showPopup && <AddSeriesPopUp onClose={setShowPopup} setReload={setReload}></AddSeriesPopUp>}
                        {loading ? (
                            <h3 className="text-center text-white bg-dark p-3 rounded-5">
                                Loading series... ⏳
                            </h3>
                        ) : (
                            <div className="d-flex flex-column align-items-center gap-4 pt-4 pb-5"
                                 style={{
                                     height: '100vh',
                                     overflowY: 'auto',
                                     width: '100%',
                                 }}>
                                {seriesList.map(series => (
                                    <div key={series.seriesId}
                                         onClick={() => navigate(`/series/${series.seriesId}`)}
                                         className="responsive-width-series mb-4 rounded-5 p-3 text-white"
                                         style={{
                                             cursor: 'pointer',
                                             backgroundColor: series.completed ? 'rgba(30, 130, 76, 0.90)' : 'rgba(0, 0, 0, 0.90)',
                                         }}
                                    >
                                        <div className="d-flex flex-column align-items-center">
                                            <h6 className="text-white">{new Intl.DateTimeFormat('en-GB').format(new Date(series.createdAt))}</h6>
                                            <div className="d-flex flex-row justify-content-center gap-3">
                                                <div className="d-flex flex-column justify-content-center align-items-center text-center">
                                                    <h5 className="text-white mb-4 mt-3">{series.playerOneNickName}</h5>
                                                    <p className="text-white text-center">{series.teamOneName.name}</p>
                                                    <img src={series.teamOneName.logo}
                                                         alt={series.teamOneName.name}
                                                         style={{width: '60px', height: '60px'}}
                                                         loading="lazy"/>
                                                </div>
                                                <div className="d-flex flex-row justify-content-center align-items-center gap-3 mx-4">
                                                    <h2 className="mb-1">{series.playerOneWins}</h2>
                                                    <h4>:</h4>
                                                    <h2 className="mb-1">{series.playerTwoWins}</h2>
                                                </div>
                                                <div className="d-flex flex-column justify-content-center align-items-center text-center">
                                                    <h5 className="text-white mb-4 mt-3">{series.playerTwoNickName}</h5>
                                                    <p className="text-white">{series.teamTwoName.name}</p>
                                                    <img src={series.teamTwoName.logo}
                                                         alt={series.teamTwoName.name}
                                                         style={{width: '60px', height: '60px'}}
                                                         loading="lazy"/>
                                                </div>
                                            </div>
                                            <p className="text-white mt-3">{series.completed ? series.winner + " 🏆" : "In progress ⌛️"}</p>
                                        </div>
                                    </div>
                                ))}
                                {seriesList.length % limit === 0 && (
                                    <div className="d-flex justify-content-center">
                                        <button className=" btn btn-outline-light m-2 p-1 px-3 rounded-5"
                                                onClick={handleLoadMore}>
                                            Load More
                                        </button>
                                    </div>
                                )}
                            </div>
                        )}
                    </div>
                </div>
            </Navbar>
        </Sidebar>
    )
}

export default Series