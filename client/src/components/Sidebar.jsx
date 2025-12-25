import {NavLink} from "react-router-dom";
import logo from "../assets/miniLogo.png"; // Adjust the path as necessary

const Sidebar = ({children}) => {
    return (
        <div className="container-fluid ">
            <div className="row ">
                <div className="col-sm-auto bg-dark sidebar">
                    <div className="d-flex flex-sm-column flex-row flex-nowrap bg-dark justify-content-between align-items-center h-100">
                        <ul className="nav flex-column d-flex text-center justify-content-around w-100">
                            <li>
                                <NavLink to="/" className="nav-link text-info p-4" title="Home" data-bs-toggle="tooltip" data-bs-placement="right"
                                         data-bs-original-title="Home">
                                    <img src={logo} alt="NHL Logo" style={{width: '50px', height: '50px'}}/>
                                </NavLink>
                            </li>
                            <li>
                                <NavLink to="/overall" title="All matches" data-bs-toggle="tooltip" data-bs-placement="right"
                                         data-bs-original-title="Matches"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }>
                                    <i className="bi bi-table fs-4"></i>
                                    <p>Matches</p>
                                </NavLink>
                            </li>
                            <li>
                                <NavLink to="/stats" title="Players Statistic" data-bs-toggle="tooltip" data-bs-placement="right" data-bs-original-title="Stats"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }>
                                    <i className="bi bi-bar-chart fs-4"></i>
                                    <p>Stats</p>
                                </NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink to="/series" title="Series" data-bs-toggle="tooltip" data-bs-placement="right" data-bs-original-title="Series"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }>
                                    <i className="bi bi-people fs-4"></i>
                                    <p>Series</p>
                                </NavLink>
                            </li>
                        </ul>
                        <ul className="nav flex-column d-flex text-center justify-content-around w-100">
                            <li>
                                <NavLink to="/playoff" title="Play-off" data-bs-toggle="tooltip" data-bs-placement="right"
                                         data-bs-original-title="Play-off"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }>
                                    <i className="bi bi-trophy-fill fs-4"></i>
                                    <p>Play-off</p>
                                </NavLink>
                            </li>
                        </ul>
                        <ul className="nav flex-column d-flex text-center justify-content-around w-100">
                            <li>
                                <NavLink to="/teams" title="NHL Teams" data-bs-toggle="tooltip" data-bs-placement="right" data-bs-original-title="Teams"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }
                                >
                                    <i className="bi bi-dice-5 fs-4"></i>
                                    <p>Teams</p>
                                </NavLink>
                            </li>
                            <li>
                                <NavLink to="/players" title="+ Add new player" data-bs-toggle="tooltip" data-bs-placement="right"
                                         data-bs-original-title="Add player"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }>
                                    <i className="bi bi-person-add fs-4"></i>
                                    <p>Add player</p>
                                </NavLink>
                            </li>
                        </ul>
                    </div>
                </div>
                <div className="main-content col-sm p-0 min-vh-100">
                    {children}
                </div>
            </div>
        </div>
    )
}

export default Sidebar