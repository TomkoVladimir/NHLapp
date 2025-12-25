import './App.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom"
import Homepage from './pages/Homepage'
import Matches from './pages/Matches.jsx'
import Stats from "./pages/Stats.jsx";
import Series from "./pages/Series.jsx";
import Playoff from "./pages/Playoff.jsx";
import Teams from "./pages/Teams.jsx";
import AddPlayer from "./pages/AddPlayer.jsx";
import {ErrorPage} from "./pages/ErrorPage.jsx";
import {SeriesDetail} from "./pages/SeriesDetail.jsx";
import {PlayOffDetail} from "./pages/PlayOffDetail.jsx";

const router = createBrowserRouter([
    {
        path: '/',
        errorElement: <ErrorPage/>,
        children: [
            {
                path: '/',
                element:
                    <Homepage/>
            },
            {
                path: '/overall',
                element:
                    <Matches/>
            },
            {
                path: '/stats',
                element:
                    <Stats/>
            },
            {
                path: '/series',
                element:
                    <Series/>
            },
            {
                path: '/series/:id',
                element: <SeriesDetail />
            },
            {
                path: '/playoff',
                element:
                    <Playoff/>
            },
            {
                path: '/playoff/:id',
                element:
                    <PlayOffDetail/>
            },
            {
                path: '/teams',
                element:
                    <Teams/>
            },
            {
                path: '/players',
                element:
                    <AddPlayer/>
            }
        ]
    }]
)

function App() {
    return (
        <RouterProvider router={router}>
        </RouterProvider>
    )
}

export default App
