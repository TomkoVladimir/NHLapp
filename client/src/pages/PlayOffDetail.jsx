import {Navbar} from "../components/Navbar.jsx";
import Sidebar from "../components/Sidebar.jsx";
import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {SeriesBlock} from "../components/SeriesBlock.jsx";

const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const PlayOffDetail = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const [playOffDetails, setPlayOffDetails] = useState(null);

    // Fetch series details
    useEffect(() => {
        fetch(`${apiBaseUrl}/api/playoffs/${id}`)
            .then(res => res.json())
            .then(data => {
                setPlayOffDetails(data);
            });
    }, [id]);

    if (!playOffDetails) return <div className="text-white">Loading...</div>;

    return (
        <Sidebar>
            <Navbar>
                <div className="playoff-bg p-4 text-white">
                    <div className="container mt-4 text-white d-flex flex-column align-items-center">
                        <div className="d-flex gap-5 align-items-center mb-4 justify-content-center">
                            <div onClick={() => navigate(`/playoff`)}
                                 style={{
                                     cursor: 'pointer',
                                 }}>
                                <i className="bi bi-arrow-left fs-1"></i>
                            </div>
                            <h2 className="m-0">{playOffDetails.playoffTitle}</h2>
                        </div>
                        <div className="mb-5">
                            <strong>Created:</strong>{' '}
                            {new Date(playOffDetails.createdAt).toLocaleDateString('en-GB')}
                        </div>
                        {playOffDetails.series.length > 3 ? (

                            <div className="finals"
                                 style={{
                                     display: 'flex', flexDirection: 'column', height: '100vh', width: '100vw',
                                 }}>
                                <div style={{flexGrow: 1, overflowY: 'auto', paddingBottom: '250px', width: '100%'}}>
                                    <div className="semifinals">
                                        <h3 className="fw-bold text-info bg-dark p-2 px-5 rounded-5">Semifinals</h3>
                                        <div key={playOffDetails.series[0].seriesId}
                                             onClick={() => navigate(`/series/${playOffDetails.series[0].seriesId}`)}
                                             className="responsive-width-series mb-4 rounded-5 p-3 text-white"
                                             style={{
                                                 cursor: 'pointer',
                                                 backgroundColor: playOffDetails.series[0].completed ? 'rgba(30, 130, 76, 0.90)' : 'rgba(0, 0, 0, 0.90)',
                                             }}>
                                            <SeriesBlock
                                                createdAt={playOffDetails.series[0].createdAt}
                                                playerOne={playOffDetails.series[0].playerOneNickName}
                                                teamOne={playOffDetails.series[0].teamOneName.name}
                                                teamOneLogo={playOffDetails.series[0].teamOneName.logo}
                                                playerTwo={playOffDetails.series[0].playerTwoNickName}
                                                teamTwo={playOffDetails.series[0].teamTwoName.name}
                                                teamTwoLogo={playOffDetails.series[0].teamTwoName.logo}
                                                completed={playOffDetails.series[0].completed}
                                                winner={playOffDetails.series[0].winner}></SeriesBlock>
                                        </div>
                                        <div key={playOffDetails.series[1].seriesId}
                                             onClick={() => navigate(`/series/${playOffDetails.series[1].seriesId}`)}
                                             className="responsive-width-series mb-4 rounded-5 p-3 mb-5 text-white"
                                             style={{
                                                 cursor: 'pointer',
                                                 backgroundColor: playOffDetails.series[1].completed ? 'rgba(30, 130, 76, 0.90)' : 'rgba(0, 0, 0, 0.90)',
                                             }}>
                                            <SeriesBlock
                                                createdAt={playOffDetails.series[1].createdAt}
                                                playerOne={playOffDetails.series[1].playerOneNickName}
                                                teamOne={playOffDetails.series[1].teamOneName.name}
                                                teamOneLogo={playOffDetails.series[1].teamOneName.logo}
                                                playerTwo={playOffDetails.series[1].playerTwoNickName}
                                                teamTwo={playOffDetails.series[1].teamTwoName.name}
                                                teamTwoLogo={playOffDetails.series[1].teamTwoName.logo}
                                                completed={playOffDetails.series[1].completed}
                                                winner={playOffDetails.series[1].winner}></SeriesBlock>
                                        </div>
                                    </div>
                                    <div className="semifinals">
                                        <h3 className="fw-bold text-dark bg-info p-2 px-5 w-score rounded-5">3rd Place</h3>
                                        <div key={playOffDetails.series[2].seriesId}
                                             onClick={() => navigate(`/series/${playOffDetails.series[2].seriesId}`)}
                                             className="responsive-width-series mb-4 rounded-5 p-3 text-white"
                                             style={{
                                                 cursor: 'pointer',
                                                 backgroundColor: playOffDetails.series[2].completed ? 'rgba(77, 168, 218, 0.90)' : 'rgba(0, 0, 0,' +
                                                     ' 0.90)',
                                             }}>
                                            <SeriesBlock
                                                createdAt={playOffDetails.series[2].createdAt}
                                                playerOne={playOffDetails.series[2].playerOneNickName}
                                                teamOne={playOffDetails.series[2].teamOneName.name}
                                                teamOneLogo={playOffDetails.series[2].teamOneName.logo}
                                                playerTwo={playOffDetails.series[2].playerTwoNickName}
                                                teamTwo={playOffDetails.series[2].teamTwoName.name}
                                                teamTwoLogo={playOffDetails.series[2].teamTwoName.logo}
                                                completed={playOffDetails.series[2].completed}
                                                winner={playOffDetails.series[2].winner}></SeriesBlock>
                                        </div>
                                        <h3 className="fw-bold text-dark bg-warning p-2 px-5 rounded-5">FINAL</h3>
                                        <div key={playOffDetails.series[3].seriesId}
                                             onClick={() => navigate(`/series/${playOffDetails.series[3].seriesId}`)}
                                             className="responsive-width-series mb-4 rounded-5 p-3 text-white"
                                             style={{
                                                 cursor: 'pointer',
                                                 backgroundColor: playOffDetails.series[3].completed ? 'rgba(254, 186, 23, 0.90)' : 'rgba(0, 0, 0,' +
                                                     ' 0.90)',
                                             }}>
                                            <SeriesBlock
                                                createdAt={playOffDetails.series[3].createdAt}
                                                playerOne={playOffDetails.series[3].playerOneNickName}
                                                teamOne={playOffDetails.series[3].teamOneName.name}
                                                teamOneLogo={playOffDetails.series[3].teamOneName.logo}
                                                playerTwo={playOffDetails.series[3].playerTwoNickName}
                                                teamTwo={playOffDetails.series[3].teamTwoName.name}
                                                teamTwoLogo={playOffDetails.series[3].teamTwoName.logo}
                                                completed={playOffDetails.series[3].completed}
                                                winner={playOffDetails.series[3].winner}></SeriesBlock>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ) : (
                            <div className="finals"
                                 style={{
                                     display: 'flex', flexDirection: 'column', height: '100vh', width: '100vw',
                                 }}>
                                <div style={{flexGrow: 1, overflowY: 'auto', paddingBottom: '250px', width: '100%'}}>
                                    <div className="semifinals">
                                        <h3 className="fw-bold text-info bg-dark p-2 px-5 rounded-5">Semifinals</h3>
                                        {playOffDetails.series.map(series => (
                                            <div key={series.seriesId}
                                                 onClick={() => navigate(`/series/${series.seriesId}`)}
                                                 className="responsive-width-series mb-4 rounded-5 p-3 text-white"
                                                 style={{
                                                     cursor: 'pointer',
                                                     backgroundColor: series.completed ? 'rgba(30, 130, 76, 0.90)' : 'rgba(0, 0, 0, 0.90)',
                                                 }}>
                                                <SeriesBlock
                                                    createdAt={series.createdAt}
                                                    playerOne={series.playerOneNickName}
                                                    teamOne={series.teamOneName.name}
                                                    teamOneLogo={series.teamOneName.logo}
                                                    playerTwo={series.playerTwoNickName}
                                                    teamTwo={series.teamTwoName.name}
                                                    teamTwoLogo={series.teamTwoName.logo}
                                                    completed={series.completed}
                                                    winner={series.winner}></SeriesBlock>
                                            </div>
                                        ))}
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
