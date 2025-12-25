import {useState} from "react";
import {NavLink} from "react-router-dom";
import logo from "../assets/miniLogo.png";

export const Navbar = ({children}) => {
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    const toggleMenu = () => {
        setIsMenuOpen(prev => !prev);
    };

    return (
        <div className="navbar-container">
            <div className="navbar m-0 p-2 bg-dark">
                <div className="navbar-top bg-dark d-flex justify-content-center align-items-center">
                    <div onClick={toggleMenu}>
                        <i className="bi bi-list" style={{ fontSize: '3rem', color: 'white' }}></i>
                    </div>
                </div>
                {isMenuOpen && (
                    <div className="fullscreen-menu d-flex flex-column justify-content-between align-items-center">
                        <ul className="nav flex-column d-flex text-center justify-content-around w-100">
                            <li>
                                <NavLink to="/" className="nav-link text-info p-4" title="Home" data-bs-toggle="tooltip" data-bs-placement="right"
                                         data-bs-original-title="Home">
                                    <img src={logo} alt="NHL Logo" style={{width: '70px', height: '70px'}}/>
                                </NavLink>
                            </li>
                        </ul>
                        <ul className="nav flex-column d-flex text-center justify-content-around w-100">
                            <li>
                                <NavLink to="/overall" title="All matches" data-bs-toggle="tooltip" data-bs-placement="right"
                                         data-bs-original-title="Matches"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }>
                                    <p className="m-0 fw-bold fs-3">Matches</p>
                                </NavLink>
                            </li>
                            <li>
                                <NavLink to="/stats" title="Players Statistic" data-bs-toggle="tooltip" data-bs-placement="right"
                                         data-bs-original-title="Stats"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }>
                                    <p className="m-0 fw-bold fs-3">Stats</p>
                                </NavLink>
                            </li>
                            <li className="nav-item">
                                <NavLink to="/series" title="Series" data-bs-toggle="tooltip" data-bs-placement="right"
                                         data-bs-original-title="Series"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }>
                                    <p className="m-0 fw-bold fs-3">Series</p>
                                </NavLink>
                            </li>
                            <li>
                                <NavLink to="/playoff" title="Play-off" data-bs-toggle="tooltip" data-bs-placement="right"
                                         data-bs-original-title="Play-off"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }>
                                    <p className="m-0 fw-bold fs-3">Play-off</p>
                                </NavLink>
                            </li>
                            <li>
                                <NavLink to="/teams" title="NHL Teams" data-bs-toggle="tooltip" data-bs-placement="right"
                                         data-bs-original-title="Teams"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }
                                >
                                    <p className="m-0 fw-bold fs-3">Teams</p>
                                </NavLink>
                            </li>
                            <li>
                                <NavLink to="/players" title="+ Add new player" data-bs-toggle="tooltip" data-bs-placement="right"
                                         data-bs-original-title="Add player"
                                         className={({isActive}) =>
                                             isActive ? "nav-link text-primary" : "nav-link text-light"
                                         }>
                                    <p className="m-0 fw-bold fs-3">Add player</p>
                                </NavLink>
                            </li>
                        </ul>
                        <button className="btn btn-light mb-4 bg-transparent border-0" onClick={toggleMenu}>
                            <i className="bi bi-x-lg mb-3 fw-bold" style={{ fontSize: '3rem', color: 'red' }}></i>
                        </button>
                    </div>
                )}
            </div>
            <div className="page-content">
                {children}
            </div>
        </div>
    )
}
